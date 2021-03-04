package com.demo.config;

import com.qmw.mapper.UtilMapper;
import com.qmw.util.FileUtil;
import com.qmw.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

    @Resource
    private UtilMapper utilMapper;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(String.join(StringUtil.LINE_SEPARATOR,
                "数据库名称：" + utilMapper.database(),
                "数据库版本：" + utilMapper.version(),
                "表数量：" + utilMapper.tableCount(),
                "数据大小：" + FileUtil.formatFileSize(utilMapper.dataLength()),
                "索引大小：" + FileUtil.formatFileSize(utilMapper.indexLength())
        ));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("App stop...");
    }

}
