package org.kariya.gulimall.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.kariya.common.utils.BizCodeEnum;
import org.kariya.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//按照微服务的理解，全局异常应针对每个微服务，故不应该写在common中
@Slf4j
@RestControllerAdvice(basePackages = "org.kariya.gulimall.product.controller")
public class GulimallProductGlobalException {

    //用于监控数据校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        log.error("数据校验出现问题{},异常类型:{}",e.getMessage(),e.getClass());
        BindingResult bindingResult=e.getBindingResult();
        Map<String,String> errorMap=new HashMap<>();
        bindingResult.getFieldErrors().forEach((f)->{
            errorMap.put(f.getField(),f.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data",errorMap);
    }

}
