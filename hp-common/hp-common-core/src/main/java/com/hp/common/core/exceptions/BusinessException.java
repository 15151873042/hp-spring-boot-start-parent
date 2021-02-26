package com.hp.common.core.exceptions;

import com.hp.common.core.enums.Enums;

public class BusinessException extends BaseException {

    /**  */
    private static final long serialVersionUID = 1L;

    public BusinessException(String errorCode, String errorMsg, Throwable caused) {
        super(errorCode, errorMsg, caused);
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public BusinessException(String errorCode, Throwable caused) {
        super(errorCode, caused);
    }

    public BusinessException(Enums enumsCode) {
        super(enumsCode);
    }

    public BusinessException(Enums enumsCode, Throwable caused) {
        super(enumsCode, caused);
    }
    
    public BusinessException(Enums enumsCode, String errorMsg) {
        super(enumsCode, errorMsg);
    }

    public BusinessException(Enums enumsCode, String errorMsg, Throwable caused) {
        super(enumsCode, errorMsg, caused);
    }

    public BusinessException(Enums enumsCode, Object[] msgObjs) {
        super(enumsCode, enumsCode.getDesc(msgObjs));
    }

    public BusinessException(Enums enumsCode, Object[] msgObjs, Throwable caused) {
        super(enumsCode, enumsCode.getDesc(msgObjs), caused);
    }
}
