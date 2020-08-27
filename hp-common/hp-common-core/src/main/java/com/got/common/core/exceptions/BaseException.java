package com.got.common.core.exceptions;

import org.apache.commons.lang3.StringUtils;

import com.got.common.core.enums.Enums;


public class BaseException extends Exception {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 异常错误代码
     */
    protected String errorCode;
    
    /**
     * 异常错误枚举
     */
    protected Enums errorEnums;

    /** 根异常，保持异常链 */
    protected Throwable caused;

    public BaseException(Enums errorEnums) {
        super(errorEnums.getDesc());
        this.errorEnums = errorEnums;
        this.errorCode = errorEnums.getCode();
    }
    
    public BaseException(Enums errorEnums, String errorMsg) {
        super(errorMsg);
        this.errorEnums = errorEnums;
        this.errorCode = errorEnums.getCode();
    }
    
    public BaseException(Enums errorEnums, Throwable caused) {
        super(errorEnums.getDesc(),caused);
        this.errorEnums = errorEnums;
        this.errorCode = errorEnums.getCode();
    }
    
    public BaseException(Enums errorEnums, String errorMsg, Throwable caused) {
        super(errorMsg, caused);
        this.errorEnums = errorEnums;
        this.errorCode = errorEnums.getCode();
    }
    
    public BaseException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, Throwable caused) {
        super(caused);
        this.errorCode = errorCode;
        this.caused = caused;
    }

    public BaseException(String errorCode, String errorMsg, Throwable caused) {
        super(errorMsg, caused);
        this.errorCode = errorCode;
        this.caused = caused;
    }

    /**
     * 获得异常的错误枚举
     * 
     * @return the errorEnums
     */
    public Enums getErrorEnums() {
        return errorEnums;
    }
    
    /**
     * 获得异常的错误代码
     * 
     * @return the errorCode
     */
    public String getErrorCode() {
        /** 如果异常定义了错误代码 */
        if (errorCode != null && !"".equals(errorCode.trim())) {
            return errorCode;
        }

        /**
         * 如果没有定义错误代码,并且该异常是一个间接异常 则返回根异常的错误代码
         */
        if (caused != null) {
            if (caused instanceof BaseException) {
                BaseException causedException = (BaseException) caused;
                return causedException.getErrorCode();
            } else {
                return errorCode;
            }
        }
        return errorCode;
    }

    @Override
    public String getMessage() {
        /** 如果异常定义了错误信息 */
        if (StringUtils.isNotEmpty(super.getMessage())) {
            return super.getMessage();
        }

        /**
         * 如果没有定义错误信息,并且该异常是一个间接异常 则返回根异常的错误信息
         */
        if (caused != null) {
            if (caused instanceof BaseException) {
                BaseException causedException = (BaseException) caused;
                return causedException.getMessage();
            }
        }

        return null;
    }
}
