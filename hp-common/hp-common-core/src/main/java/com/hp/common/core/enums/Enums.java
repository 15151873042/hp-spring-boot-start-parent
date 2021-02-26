package com.hp.common.core.enums;

import java.text.MessageFormat;


/**
 * 错误码枚举类
 * 错误码枚举由5位数字字符串组成,前2位表示业务类型，后3位业务错误码
 * @ClassName Enums
 * @Author Create By lijunhui 2016年11月30日 下午2:37:36
 * @Version v0.1
 */
public interface Enums {

	public String getCode();

	public String getDesc();
	
	public String getDesc(Object ... objs);

	/**
	 * 系统码：00开头表示成功，99开头表示错误
	 */
	public enum SysCode implements Enums {
	    SUCCESS("000000", "处理成功！"),
	    GET_ERROR("990001", "获取数据失败，请刷新重试！"),
	    ADD_ERROR("990002", "插入数据失败，请刷新重试！"),
	    EDIT_ERROR("990003", "更新数据失败，请刷新重试！"),
	    DEL_ERROR("990004", "删除数据失败，请刷新重试！"),
	    PARAM_ERROR("990005", "参数有误！【{0}】"),
	    NO_AUTH("990006","无权访问!"),
	    UPLOAD_ERROR("990007","文件上传失败!"),
	    REST_AUTH_ERROR("990008","鉴权失败!"),
	    REST_REMOTING_ERROR("990009","远程调用失败!"),
	    USER_NOT_LOGIN("990010", "用户未登录!"),
	    SYS_ERROR("999999","系统错误，请联系管理员！");

		private String code;
		private String desc;

		private SysCode(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		@Override
        public String getCode() {
			return code;
		}

		@Override
        public String getDesc() {
			return desc;
		}

		@Override
        public String getDesc(Object ... objs) {
			return MessageFormat.format(this.desc,objs);
		}
		
		public boolean equalsCode(String code) {
	        return this.code.equals(code);
	    }
		
		//通过value获取对应的枚举对象
        public static SysCode getEnums(String value) {
            for (SysCode code : SysCode.values()) {
                if (code.getCode().equals(value)) {
                    return code;
                }
            }
            return null;
        }
	}
}
