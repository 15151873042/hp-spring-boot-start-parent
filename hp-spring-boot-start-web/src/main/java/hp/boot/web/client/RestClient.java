package hp.boot.web.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.hp.common.core.util.BeanMapUtil;
import com.hp.common.core.util.DateUtil;
import com.hp.common.core.util.HmacUtil;
import com.hp.common.core.util.JacksonUtil;
import com.hp.common.core.util.StringUtil;
import com.hp.common.core.vo.JsonResult;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private static final String TOKEN_KEY = "Auth-Token";

    private static final String CONTENT_KEY = "Auth-Content";

    private static final String CONTENT_TYPE_KEY = "Content-Type";

    public static final String DEFAULT_PREFIX = "{";

    public static final String DEFAULT_SUFFIX = "}";

    public static final String URL_REGEX_PATTERN = "^((https|http)://)[^\\s]+(/[{][a-zA-z]+[}])+(/[a-zA-Z]+)*";

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        RestClient.restTemplate = restTemplate;
    }

    public static JsonResult get(String url) {
        return restRemoting(url, HttpMethod.GET, null, null);
    }

    public static <R> R get(String url, ParameterizedTypeReference<R> type) {
        return restRemoting(url, HttpMethod.GET, null, type);
    }

    public static <T> JsonResult get(String url, T paramObj) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.GET, paramObj, null);
    }

    public static <R, T> R get(String url, T paramObj, ParameterizedTypeReference<R> type) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.GET, paramObj, type);
    }

    public static <T> JsonResult post(String url, T paramObj) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.POST, paramObj, null);
    }

    public static <R, T> R post(String url, T paramObj, ParameterizedTypeReference<R> type) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.POST, paramObj, type);
    }

    public static <T> JsonResult put(String url, T paramObj) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.PUT, paramObj, null);
    }

    public static <R, T> R put(String url, T paramObj, ParameterizedTypeReference<R> type) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.PUT, paramObj, type);
    }

    public static <T> JsonResult delete(String url, T paramObj) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.DELETE, paramObj, null);
    }

    public static <R, T> R delete(String url, T paramObj, ParameterizedTypeReference<R> type) {
        if (paramObj == null) {
            throw new RuntimeException("远程调用传入参数错误！");
        }
        return restRemoting(url, HttpMethod.DELETE, paramObj, type);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <R, T> R restRemoting(String url, HttpMethod method, T paramObj,
                                         ParameterizedTypeReference type) {
        Map<String, Object> paramMap = null;
        HttpEntity requestEntity = null;
        if (paramObj != null && BeanUtils.isSimpleProperty(paramObj.getClass())) {
            throw new RuntimeException("远程调用传入参数错误！");
        } else if (paramObj == null || paramObj instanceof List) {
            requestEntity = getHttpEntity(url, paramObj);
        } else if (paramObj instanceof Map) {
            paramMap = (Map<String, Object>) paramObj;
        } else {
            paramMap = BeanMapUtil.beanToMap(paramObj);
        }

        if (paramMap != null) {
            url = urlPlaceholderReplace(url, paramMap);
            if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                try {
                    url = urlParamFormat(url, paramMap);
                    paramMap = null;
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException("远程调用参数封装失败！");
                }
            }
            requestEntity = getHttpEntity(url, paramMap);
        }
        if (type == null) {
            type = new ParameterizedTypeReference<JsonResult>() {};
        }
        logger.debug("RestClient接口调用，URL：{{}}，HttpMethod：{{}}，Request：{}",
            new Object[] { url, method, JacksonUtil.toJsonStr(requestEntity) });
        ResponseEntity<R> response;
        try {
            response = restTemplate.exchange(new URI(url), method, requestEntity, type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("远程调用失败！");
        }
        logger.debug("RestClient接口返回，Response：{}", new Object[] { JacksonUtil.toJsonStr(response) });
        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            logger.error("\n HttpStatus：" + response.getStatusCodeValue() + " " + response.getStatusCode()
                         + "\n ResponseBody：" + response.getBody());
            throw new RuntimeException("远程调用失败！");
        }
        return response.getBody();
    }

    /**
     * 添加HttpHeader
     * 
     * @param url
     * @param paramObj
     * @return
     */
    private static <T> HttpEntity<T> getHttpEntity(String url, T paramObj) {
        String token = HmacUtil.generateKey();
        String jsonReq = url;
        if (paramObj != null && paramObj != "") {
            jsonReq += JacksonUtil.toJsonStr(paramObj);
        }
        String content = HmacUtil.encryptHMAC(token, jsonReq);
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.set("Accept-Encoding", "*");
        headers.set("Cache-Control", "no-cache");
        headers.set("Pragma", "no-cache");
        headers.set(TOKEN_KEY, token);
        headers.set(CONTENT_KEY, content);
        headers.set(CONTENT_TYPE_KEY, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new HttpEntity<>(paramObj, headers);
    }

    /**
     * url参数拼接，用于GET、DELETE
     * 
     * @param url
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException 
     */
    private static String urlParamFormat(String url,
                                           Map<String, Object> paramMap) throws UnsupportedEncodingException {
        if (paramMap == null || paramMap.isEmpty()) {
            return url;
        }
        String value = "";
        StringBuilder queryStr = new StringBuilder();
        List<?> list;
        String[] valueAry;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            if (entry.getValue() == null || StringUtils.isBlank(String.valueOf(entry.getValue()))) {
                continue;
            }
            if (queryStr.length() == 0) {
                queryStr.append(url);
                queryStr.append("?");
            } else {
                queryStr.append("&");
            }
            queryStr.append(entry.getKey());
            queryStr.append("=");
            if (entry.getValue() instanceof Date) {
                value = DateUtil.formatDate((Date) entry.getValue(), DateUtil.FORMAT_SHORT);
                value = UriUtils.encodeQueryParam(value, CharEncoding.UTF_8);
                queryStr.append(value);
            } else if (entry.getValue() instanceof List) {
                list = (List<?>) entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        queryStr.append(",");
                    }
                    value = UriUtils.encodeQueryParam(list.get(i).toString(), CharEncoding.UTF_8);
                    queryStr.append(value);
                }
            } else if (entry.getValue() instanceof String[]) {
                valueAry = (String[]) entry.getValue();
                for (int i = 0; i < valueAry.length; i++) {
                    if (i != 0) {
                        queryStr.append(",");
                    }
                    value = UriUtils.encodeQueryParam(valueAry[i], CharEncoding.UTF_8);
                    queryStr.append(value);
                }
            } else {
                value = UriUtils.encodeQueryParam(String.valueOf(entry.getValue()), CharEncoding.UTF_8);
                queryStr.append(value);
            }
        }
        return queryStr.toString();
    }

    /**
     * url占位符变量替换
     * 
     * @param url
     * @param paramMap
     * @return
     */
    private static String urlPlaceholderReplace(String url, Map<String, Object> paramMap) {
        if (!url.matches(URL_REGEX_PATTERN) || paramMap == null || paramMap.isEmpty()) {
            return url;
        }
        String arg = "";
        List<String> keyList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            arg = DEFAULT_PREFIX + entry.getKey() + DEFAULT_SUFFIX;
            if (url.contains(arg)) {
                url = url.replace(arg, StringUtil.nullToEmpty(entry.getValue()));
                keyList.add(entry.getKey());
            }
        }
        for (String key : keyList) {
            paramMap.remove(key);
        }
        return url;
    }
}
