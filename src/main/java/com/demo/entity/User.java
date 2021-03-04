package com.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class User {

    @TableId
    private Long id;
    private String mobile;
    @JSONField(serialize = false)
    private String password; // 返回时忽略此字段
    private String name;
    private Boolean valid;
    private Timestamp updateTime;
    private Timestamp createTime;

}
