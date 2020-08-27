package hp.boot.transaction.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SpringTransactionCommonConfig.class)
// TODO 自动配置的事务@Transactional还未去除
public @interface EnableTransactionCommonConfig {
	
	/**
	 *
	 * @Descripton 事务order为0，
	 * 自定义通知order<0表明在事务之内执行，order>0表明在事务之外执行
	 * @author 胡鹏
	 * @date 2020年8月20日 下午1:56:31
	 * @return
	 */
	int order() default 0;
}
