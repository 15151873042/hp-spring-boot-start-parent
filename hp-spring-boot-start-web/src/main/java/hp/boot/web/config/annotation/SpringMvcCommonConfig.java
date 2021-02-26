package hp.boot.web.config.annotation;

import java.util.List;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hp.common.core.serializer.jackson.JacksonObjectMapper;

import hp.boot.web.converter.StringToDateConverter;
import hp.boot.web.resolver.JsonModelAttributeMethodProcessor;


/**
 * springMVC通用配置
 * @author 胡鹏
 *
 */
@Configuration
public class SpringMvcCommonConfig implements WebMvcConfigurer {
	
	/**
	 * 默认数据类型转换器在如下方法中：
	 * @see ApplicationConversionService#configure(FormatterRegistry)
	 * */
	// 添加自定义数据类型转换器
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToDateConverter());
	}
	
	
	// 添加消息转换器
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter	 jacksonConverter = mappingJackson2HttpMessageConverter();
		// 这边要将自定义的jsonConver放在第一个，这样处理数据的时候就会先用自定义的
		converters.add(0, jacksonConverter);
	}
	
	
	
	// 添加自定义请求参数解析器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    	MappingJackson2HttpMessageConverter jacksonConverter = mappingJackson2HttpMessageConverter();
        argumentResolvers.add(new JsonModelAttributeMethodProcessor(true, jacksonConverter));
    }
    
    
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    	JacksonObjectMapper objectMapper = new JacksonObjectMapper();
    	MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
    	return jacksonConverter;
    }

}
