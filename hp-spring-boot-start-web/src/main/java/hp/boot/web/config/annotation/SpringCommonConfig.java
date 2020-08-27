package hp.boot.web.config.annotation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import hp.boot.web.client.RestClient;



@Configuration
@EnableConfigurationProperties(OkHttpProperties.class)
public class SpringCommonConfig {
	
	@Autowired
	private OkHttpProperties okHttpProperties;

	
	@Bean
	public OkHttp3ClientHttpRequestFactory okHttp3ClientFactory() {
		OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
		factory.setConnectTimeout(okHttpProperties.getConnectTimeOut());
		factory.setReadTimeout(okHttpProperties.getReadTimeOut());
		factory.setWriteTimeout(okHttpProperties.getWriteTimeOut());
		return factory;
	}

	@Bean("restTemplate")
	@ConditionalOnMissingBean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(okHttp3ClientFactory());
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter(new ObjectMapper()));
		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}
	
	
	@Bean("restClient")
	@ConditionalOnMissingBean
	RestClient restClient() {
		return new RestClient();
	}

	
}
