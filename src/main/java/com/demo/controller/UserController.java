package com.demo.controller;

import com.demo.entity.User;
import com.qmw.annotation.ResponseMessage;
import com.qmw.entity.PageX;
import com.qmw.util.StringUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping
    @ResponseMessage("查询成功")
    public Object list() {
        List<User> list = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            list.add(
                    new User()
                            .setId(i)
                            .setName(StringUtil.randomLetter(6))
                            .setMobile(StringUtil.randomNumber(13))
                            .setCreateTime(LocalDateTime.now())
                            .setUpdateTime(LocalDate.now())
            );
        }
        return new PageX<User>().setRecords(list);
    }

    @GetMapping("{id}")
    public Object detail(@PathVariable Long id) {
        return new User().setId(id);
    }

    @PostMapping
    public Object create() {
        return null;
    }

    @PutMapping
    public Object modify() {
        return null;
    }

    @DeleteMapping
    @ResponseMessage("删除成功")
    public Object delete() {
        return null;
    }

}
