package hp.boot.transaction.config.annotation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import static hp.boot.transaction.config.annotation.SpringTransactionCommonConfig.*;

@ConfigurationProperties(prefix = "spring.tx")
public class TxProperties {
	
	/** 切点表达式 */
	private String pointCutExpression = DEFULAT_POINT_CUT_EXPRESSION;
	
	/** 读写事务方法名称正则 */
	private String[] readWriteTransactionMethodPattern = DEFAULT_READ_WRITE_TRANSACTION_METHOD_PATTERN;
	
	/** 只读事务方法名称正则 */
	private String[] readOnlyTransactionMethodPattern = DEFAULT_READ_ONLY_TRANSACTION_METHOD_PATTERN;

	public String getPointCutExpression() {
		return pointCutExpression;
	}

	public void setPointCutExpression(String pointCutExpression) {
		this.pointCutExpression = pointCutExpression;
	}

	public String[] getReadWriteTransactionMethodPattern() {
		return readWriteTransactionMethodPattern;
	}

	public void setReadWriteTransactionMethodPattern(String[] readWriteTransactionMethodPattern) {
		this.readWriteTransactionMethodPattern = readWriteTransactionMethodPattern;
	}

	public String[] getReadOnlyTransactionMethodPattern() {
		return readOnlyTransactionMethodPattern;
	}

	public void setReadOnlyTransactionMethodPattern(String[] readOnlyTransactionMethodPattern) {
		this.readOnlyTransactionMethodPattern = readOnlyTransactionMethodPattern;
	}
}
