package com.dodoyd.moyu.admin.exception;

import com.dodoyd.moyu.common.enums.ExceptionEnum;
import com.dodoyd.moyu.common.exception.BaseException;

/**
 * @author shisong02
 * @since 2024-01-03
 */
public class ParameterErrorException extends BaseException {

    public ParameterErrorException(String message) {
        super(ExceptionEnum.INVALID_PARAMETER.getCode(), message);
    }
}
