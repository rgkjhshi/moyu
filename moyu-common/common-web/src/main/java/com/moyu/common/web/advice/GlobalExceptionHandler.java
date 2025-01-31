package com.moyu.common.web.advice;


import cn.hutool.json.JSONUtil;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * <p>@ControllerAdvice和@RestControllerAdvice都可以指向控制器的一个子集</p>
 * <pre>
 *   如:
 *   指向所有带 @RestController 注解的控制器: @ControllerAdvice(annotations = RestController.class)
 *   指向所有指定包中的控制器: @ControllerAdvice("org.example.controllers")
 * </pre>
 * <p>@ControllerAdvice和@RestControllerAdvice的区别在于:</p>
 * <pre>
 *   ControllerAdvice     中@ExceptionHandler注解的函数返回值不会自动加@ResponseBody语义
 *   RestControllerAdvice 中@ExceptionHandler注解的函数返回值会自动加@ResponseBody语义(就像RestController一样)
 * </pre>
 *
 * @author shisong
 * @since 2022-09-08
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    /**
     * 异常捕捉处理
     * 若为ControllerAdvice     处理异常后返回本函数的返回值
     * 若为RestControllerAdvice 处理异常后返回本函数的返回值的restful结果(就像加了@ResponseBody一样)
     */
    @ExceptionHandler(value = Exception.class)
    public BaseResponse<?> exceptionHandler(Exception exception) {
        BaseResponse<?> response = new BaseResponse<>();
        if (exception instanceof BindException) {
            // 使用@Valid和@Validated 会抛出 MethodArgumentNotValidException extends BindException
            BindingResult bindingResult = ((BindException) exception).getBindingResult();
            String message = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(";"));
            log.error(message);
            response.setCode(ExceptionEnum.INVALID_PARAMETER.getCode());
            response.setMessage(message);
        } else if (exception instanceof ServletRequestBindingException) {
            MethodArgumentNotValidException e;
            // ServletRequestBindingException是请求参数绑定到JavaBean或模型属性时出现的异常，如必传参数缺失
            log.error(exception.getMessage());
            response.setCode(ExceptionEnum.INVALID_PARAMETER.getCode());
            response.setMessage(ExceptionEnum.INVALID_PARAMETER.getMessage());
        } else if (exception instanceof HttpMessageConversionException) {
            // json格式参数进行参数类型转换时，参数转换失败则HttpMessageConversionException
            log.error(exception.getMessage());
            response.setCode(ExceptionEnum.INVALID_PARAMETER.getCode());
            response.setMessage(exception.getMessage());
        } else if (exception instanceof ConstraintViolationException) {
            // 参数校验失败则ConstraintViolationException
            log.error(exception.getMessage());
            response.setCode(ExceptionEnum.INVALID_PARAMETER.getCode());
            response.setMessage(exception.getMessage());
        } else if (exception instanceof IllegalArgumentException) {
            log.error(exception.getMessage(), exception);
            response.setCode(ExceptionEnum.BUSINESS_ERROR.getCode());
            response.setMessage(exception.getMessage());
        } else if (exception instanceof BaseException) {
            log.error(exception.getMessage());
            response.setCode(((BaseException) exception).getCode());
            response.setMessage(exception.getMessage());
        } else {
            log.error("系统异常", exception);
            response.setCode(ExceptionEnum.SYSTEM_ERROR.getCode());
            response.setMessage(ExceptionEnum.SYSTEM_ERROR.getMessage());
        }
        log.info("异常捕捉处理后返回结果为:{}", JSONUtil.toJsonStr(response));
        return response;
    }
}
