package hp.boot.reids.config.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class SpringRedisCommonConfig {
	
	
	@Bean("redisTemplate")
	@ConditionalOnMissingBean()
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		// redis key 用String序列化
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		// redis value 用jackson序列化
		GenericJackson2JsonRedisSerializer jacksonRedisSerializer = new GenericJackson2JsonRedisSerializer(new ObjectMapper());
		redisTemplate.setValueSerializer(jacksonRedisSerializer);
		redisTemplate.setHashValueSerializer(jacksonRedisSerializer);
		return redisTemplate;
	}

}
