package com.demo.config;

import com.alibaba.fastjson.JSON;
import com.qmw.annotation.ResponseMessage;
import com.qmw.entity.ResponseResult;
import com.qmw.entity.ResponseStatus;
import com.qmw.exception.CustomException;
import com.qmw.util.FileUtil;
import com.qmw.util.ParamsPrinter;
import com.qmw.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private HttpServletRequest request;

    @ExceptionHandler
    public ResponseResult<?> handle(Throwable e) {
        if (log.isErrorEnabled())
            ParamsPrinter.print(request);
        log.error(e.getMessage(), e);
        return ResponseResult.error(ResponseStatus.ERROR);
    }

    @ExceptionHandler
    public ResponseResult<?> handle(CustomException e) { // 普通校验异常
        return ResponseResult.error(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler
    public ResponseResult<?> handle(HttpRequestMethodNotSupportedException e) { // method不支持
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler
    public ResponseResult<?> handle(MultipartException e) { // MaxUploadSizeExceededException
        Throwable t = e.getCause().getCause();
        String msg;
        if (t instanceof FileSizeLimitExceededException) {
            msg = "单个文件大小不能超过" + FileUtil.formatFileSize(((FileSizeLimitExceededException) t).getPermittedSize());
        } else if (t instanceof SizeLimitExceededException) {
            msg = "总文件大小不能超过" + FileUtil.formatFileSize(((SizeLimitExceededException) t).getPermittedSize());
        } else {
            msg = e.getMessage();
            if (msg != null && msg.contains("the request was rejected because no multipart boundary was found"))
                msg = "请选择要上传的文件";
            else
                msg = "文件上传失败";
        }
        return ResponseResult.error(msg);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(false); // 空字符串不转null
        binder.registerCustomEditor(String.class, editor);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return (factory -> {
            for (HttpStatus value : HttpStatus.values())
                factory.addErrorPages(new ErrorPage(value, "/error/" + value.value()));
        });
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object result,
                                  MethodParameter parameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        String msg = "";
        ResponseMessage message = parameter.getMethodAnnotation(ResponseMessage.class);
        if (message != null)
            msg = message.value();
        if (!(result instanceof ResponseResult))
            result = ResponseResult.ok(result, msg);
        log.info("[{}][{}]执行结果：{}{}",
                this.request.getMethod(),
                this.request.getRequestURI(),
                StringUtil.LINE_SEPARATOR,
                JSON.toJSONString(result, true));
        return result;
    }
}
