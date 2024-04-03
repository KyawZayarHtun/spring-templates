package com.example.model.aop;

import com.example.util.exception.DtoValidationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

@Aspect
@Configuration
public class ValidationAop {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void apiMethods(){}

    @Before(value = "apiMethods() && args(.., bindingResult)", argNames = "bindingResult")
    public void handle(BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new DtoValidationException(bindingResult);
    }

}
