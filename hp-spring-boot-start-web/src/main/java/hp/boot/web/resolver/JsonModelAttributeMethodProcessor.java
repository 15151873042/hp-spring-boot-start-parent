package hp.boot.web.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

public class JsonModelAttributeMethodProcessor extends ModelAttributeMethodProcessor {

    private static final Object NO_VALUE = new Object();

    private static final Set<HttpMethod> SUPPORTED_METHODS = EnumSet.of(HttpMethod.POST, HttpMethod.PUT);

    private HttpMessageConverter<?> converter;

    /**
     * @param annotationNotRequired if "true", non-simple method arguments and
     * return values are considered model attributes with or without a
     * {@code @ModelAttribute} annotation
     */
    public JsonModelAttributeMethodProcessor(boolean annotationNotRequired,
                                             HttpMessageConverter<?> converter) {
        super(annotationNotRequired);
        this.converter = converter;
    }

    /**
     * Instantiate the model attribute from a URI template variable or from a
     * request parameter if the name matches to the model attribute name and
     * if there is an appropriate type conversion strategy. If none of these
     * are true delegate back to the base class.
     * @see #createAttributeFromRequestValue
     */
	@Override
	@SuppressWarnings("deprecation")
    protected final Object createAttribute(String attributeName, MethodParameter methodParam,
                                           WebDataBinderFactory binderFactory,
                                           NativeWebRequest request) throws Exception {

        String value = getRequestValueForAttribute(attributeName, request);
        if (value != null) {
            Object attribute = createAttributeFromRequestValue(value, attributeName, methodParam,
                binderFactory, request);
            if (attribute != null) {
                return attribute;
            }
        }

        Object org = null;

        HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        MediaType contentType;
        try {
            contentType = inputMessage.getHeaders().getContentType();
        } catch (InvalidMediaTypeException ex) {
            throw new HttpMediaTypeNotSupportedException(ex.getMessage());
        }
        HttpMethod httpMethod = ((HttpRequest) inputMessage).getMethod();

        if (MediaType.APPLICATION_JSON_UTF8.equals(contentType) && httpMethod != null
            && SUPPORTED_METHODS.contains(httpMethod) && inputMessage.getBody() != null) {
            org = readWithMessageConverters(inputMessage, methodParam,
                methodParam.getNestedGenericParameterType(), contentType);
        }

        if (org == null) {
            org = super.createAttribute(attributeName, methodParam, binderFactory, request);
        }

        return org;
    }

    /**
     * Obtain a value from the request that may be used to instantiate the
     * model attribute through type conversion from String to the target type.
     * <p>The default implementation looks for the attribute name to match
     * a URI variable first and then a request parameter.
     * @param attributeName the model attribute name
     * @param request the current request
     * @return the request value to try to convert or {@code null}
     */
    protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
        Map<String, String> variables = getUriTemplateVariables(request);
        if (StringUtils.hasText(variables.get(attributeName))) {
            return variables.get(attributeName);
        } else if (StringUtils.hasText(request.getParameter(attributeName))) {
            return request.getParameter(attributeName);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables = (Map<String, String>) request
            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return (variables != null ? variables : Collections.<String, String> emptyMap());
    }

    /**
     * Create a model attribute from a String request value (e.g. URI template
     * variable, request parameter) using type conversion.
     * <p>The default implementation converts only if there a registered
     * {@link Converter} that can perform the conversion.
     * @param sourceValue the source value to create the model attribute from
     * @param attributeName the name of the attribute, never {@code null}
     * @param methodParam the method parameter
     * @param binderFactory for creating WebDataBinder instance
     * @param request the current request
     * @return the created model attribute, or {@code null}
     * @throws Exception
     */
    protected Object createAttributeFromRequestValue(String sourceValue, String attributeName,
                                                     MethodParameter methodParam,
                                                     WebDataBinderFactory binderFactory,
                                                     NativeWebRequest request) throws Exception {

        DataBinder binder = binderFactory.createBinder(request, null, attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(methodParam);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue, methodParam.getParameterType(), methodParam);
            }
        }
        return null;
    }

    /**
     * This implementation downcasts {@link WebDataBinder} to
     * {@link ServletRequestDataBinder} before binding.
     * @see ServletRequestDataBinderFactory
     */
    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
    }

    @SuppressWarnings("unchecked")
    protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter,
                                                   Type targetType, MediaType contentType) throws IOException,
                                                                    HttpMessageNotReadableException {

        Class<?> contextClass = (parameter != null ? parameter.getContainingClass() : null);
        Class<T> targetClass = (targetType instanceof Class ? (Class<T>) targetType : null);
        if (targetClass == null) {
            ResolvableType resolvableType = (parameter != null ? ResolvableType.forMethodParameter(parameter)
                : ResolvableType.forType(targetType));
            targetClass = (Class<T>) resolvableType.resolve();
        }

        Object body = NO_VALUE;

        try {
            inputMessage = new EmptyBodyCheckingHttpInputMessage(inputMessage);
            GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
            if (genericConverter.canRead(targetType, contextClass, contentType)) {
                if (inputMessage.getBody() != null) {
                    body = genericConverter.read(targetType, contextClass, inputMessage);
                }
            }
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex, inputMessage);
        }

        if (body == NO_VALUE) {
            return null;
        }

        return body;
    }

    private static class EmptyBodyCheckingHttpInputMessage implements HttpInputMessage {

        private final HttpHeaders headers;

        private final InputStream body;

        public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
            this.headers = inputMessage.getHeaders();
            InputStream inputStream = inputMessage.getBody();
            if (inputStream == null) {
                this.body = null;
            } else if (inputStream.markSupported()) {
                inputStream.mark(1);
                this.body = (inputStream.read() != -1 ? inputStream : null);
                inputStream.reset();
            } else {
                PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
                int b = pushbackInputStream.read();
                if (b == -1) {
                    this.body = null;
                } else {
                    this.body = pushbackInputStream;
                    pushbackInputStream.unread(b);
                }
            }
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.headers;
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.body;
        }
    }

}
