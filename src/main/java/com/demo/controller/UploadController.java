package com.demo.controller;

import com.qmw.exception.CustomException;
import com.qmw.util.ExcelUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
public class UploadController {

    @Resource
    private HttpServletResponse response;

    @RequestMapping("download")
    public void download() {
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition"); // 允许设置Content-Disposition
        ExcelUtil.download(new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("123", 321);
                put("aaa", 444);
                put("asdasd", "qwert");
            }});
        }}, response, null);
    }

    @PostMapping("upload")
    public void upload(@RequestParam(required = false) List<MultipartFile> file) throws IOException {
        if (file == null || file.isEmpty())
            throw new CustomException("请选择文件");

        List<LinkedHashMap<String, String>> list = null;

        for (MultipartFile multipartFile : file) {
            List<LinkedHashMap<String, String>> list2 = ExcelUtil.readAsList(multipartFile.getInputStream(), 3);
            if (list == null) {
                list = list2;
            } else {
                for (Map<String, String> i : list) {
                    for (Map<String, String> j : list2) {
                        if (i.get("组织").equals(j.get("组织"))) {
                            i.put("活跃学员", new BigDecimal(i.get("活跃学员")).add(new BigDecimal(j.get("活跃学员"))).toPlainString());
                            i.put("学员总数", new BigDecimal(i.get("学员总数")).add(new BigDecimal(j.get("学员总数"))).toPlainString());
                            i.put("总获得积分", new BigDecimal(i.get("总获得积分")).add(new BigDecimal(j.get("总获得积分"))).toPlainString());
                        }
                    }
                }
            }
        }
        List<LinkedHashMap<String, Object>> downLoadList = new ArrayList<>();

        if (list != null) {
            list.forEach(i -> {
                BigDecimal a = new BigDecimal(i.get("活跃学员"));
                BigDecimal b = new BigDecimal(i.get("学员总数"));
                BigDecimal c = new BigDecimal(i.get("总获得积分"));

                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("组织", i.get("组织"));
                map.put("活跃学员", i.get("活跃学员"));
                map.put("学员总数", i.get("学员总数"));
                map.put("总获得积分", i.get("总获得积分"));
                map.put("参与度", a.multiply(BigDecimal.valueOf(100)).divide(b, 2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                map.put("人均积分", c.divide(b, 2, BigDecimal.ROUND_HALF_UP).toPlainString());
                downLoadList.add(map);
            });
        }

        ExcelUtil.download(downLoadList, response, null);
    }

}
