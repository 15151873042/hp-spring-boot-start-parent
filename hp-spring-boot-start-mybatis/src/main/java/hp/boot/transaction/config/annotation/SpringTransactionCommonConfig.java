package hp.boot.transaction.config.annotation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import hp.boot.mybatis.plugin.PagePlugin;

@Configuration
@EnableConfigurationProperties(TxProperties.class)
public class SpringTransactionCommonConfig implements ImportAware {
	
	/** 默认事务切点表达式 */
	static final String DEFULAT_POINT_CUT_EXPRESSION = "execution(* *..*Service.*(..))";
	
	/** 默认读写事务方法名称正则  */
	static final String[] DEFAULT_READ_WRITE_TRANSACTION_METHOD_PATTERN = {
			"save*", "*Save", "insert*", "*Insert", "add*", "*Add",
			"update*", "*Update", "edit*", "*Edit", "delete*",
			"*Delete", "del*", "*Del",
	};
	
	/** 默认只读事务方法名称正则  */
	static final String[] DEFAULT_READ_ONLY_TRANSACTION_METHOD_PATTERN = {"*"};
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private TxProperties transactionProperties;

	@Nullable
	protected AnnotationAttributes enableTx;
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableTx = AnnotationAttributes.fromMap(
				importMetadata.getAnnotationAttributes(EnableTransactionCommonConfig.class.getName(), false));
		if (this.enableTx == null) {
			throw new IllegalArgumentException(
					"@EnableTransactionCommonConfig is not present on importing class " + importMetadata.getClassName());
		}
	}

	
	/**
	 *
	 * @Descripton 事务拦截规则
	 * @author 胡鹏
	 * @date 2020年7月6日 下午4:57:51
	 * @return
	 */
	@Bean("myTransactionAttributeSource")
	public TransactionAttributeSource transactionAttributeSource() {
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		// 只读事务
		RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
		readOnlyTx.setReadOnly(true);
		readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		// 读写事务
		RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
		requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		//只要遇到异常都回滚
		requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		
		Map<String, TransactionAttribute> txMap = new LinkedHashMap<>();
		String[] readWriteTransactionMethodPatterns = transactionProperties.getReadWriteTransactionMethodPattern();
		for (String readWriteMethod : readWriteTransactionMethodPatterns) {
			txMap.put(readWriteMethod, requiredTx);
		}
		String[] readOnlyTransactionMethodPatterns = transactionProperties.getReadOnlyTransactionMethodPattern();
		for (String readOnlyMethod : readOnlyTransactionMethodPatterns) {
			txMap.put(readOnlyMethod, readOnlyTx);
		}
		source.setNameMap(txMap);
		return source;
	}
	
	
	/**
	 *
	 * @Descripton 定义事务拦截器
	 * @author 胡鹏
	 * @date 2020年7月6日 下午5:00:02
	 * @return
	 */
	@Bean("myTransactionInterceptor")
	TransactionInterceptor myTransactionInterceptor() {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		transactionInterceptor.setTransactionManager(transactionManager);
		transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource());
		return transactionInterceptor;
	}
	
	
	/**
	 *
	 * @Descripton 定义advisor
	 * @author 胡鹏
	 * @date 2020年7月6日 下午9:25:56
	 * @return
	 */
	@Bean("myTransactionAdvisor")
	AspectJExpressionPointcutAdvisor myTransactionAdvisor() {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setAdvice(myTransactionInterceptor());
		advisor.setExpression(transactionProperties.getPointCutExpression());
		advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
		return advisor;
	}
	
	/**
	 *
	 * @Descripton 分页插件
	 * @author 胡鹏
	 * @date 2020年8月24日 下午5:51:54
	 * @return
	 */
	@Bean("pagePlugin")
	Interceptor pagePlugin() {
		return new PagePlugin();
	}

}
