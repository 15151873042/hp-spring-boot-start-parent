package hp.boot.web.config.annotation;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.okhttp")
public class OkHttpProperties {
	
	private static final int DEFAULT_CONNECT_TIME_OUT = 10000;
	
	private static final int DEFAULT_READ_TIME_OUT = 60000;
	
	private static final int DEFAULT_WRITE_TIME_OUT = 10000;
	
	/** 连接超时时间（单位：毫秒）  */
	private int connectTimeOut = DEFAULT_CONNECT_TIME_OUT;
	
	/** 读取超时时间（单位：毫秒）  */
	private int readTimeOut = DEFAULT_READ_TIME_OUT;
	
	/** 写入超时时间（单位：毫秒） */
	private int writeTimeOut = DEFAULT_WRITE_TIME_OUT;

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getWriteTimeOut() {
		return writeTimeOut;
	}

	public void setWriteTimeOut(int writeTimeOut) {
		this.writeTimeOut = writeTimeOut;
	}
}
