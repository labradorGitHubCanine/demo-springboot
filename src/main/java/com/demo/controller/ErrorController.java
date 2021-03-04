package com.demo.controller;

import com.qmw.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @RequestMapping("error/{status}")
    public void error(@PathVariable int status) {
        for (HttpStatus value : HttpStatus.values())
            if (value.value() == status)
                throw new CustomException(value.toString());
        throw new CustomException("请求失败：" + status);
    }

}
