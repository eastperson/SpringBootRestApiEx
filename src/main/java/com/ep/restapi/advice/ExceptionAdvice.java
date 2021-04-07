package com.ep.restapi.advice;

import com.ep.restapi.advice.exception.CUserNotFoundException;
import com.ep.restapi.model.response.CommonResult;
import com.ep.restapi.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor

// 패키지를 지정할 수도 있다.
//@RestControllerAdvice(basePackages = "com.rest.api")
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e){
        // 예외 처리의 메시지를 MessageSource에서 가져오도록
        return responseService.getFaileResult(Integer.valueOf(getMessage("unKnown.code")),getMessage("unKnown.msg"));
    }

    // code정보에 해당하는 메시지로 조회합니다.
    private String getMessage(String code) {
        return getMessage(code,null);
    }


    // code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code,Object[] args) {
        return messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e){
        return responseService.getFaileResult(Integer.valueOf(getMessage("userNotFound.code")),getMessage("userNotFound.msg"));
    }

}
