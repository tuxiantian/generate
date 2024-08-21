package com.tuxt.generate;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Scanner;

/**
 * mybatis plus 提供的代码生成器
 * 可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码
 *
 * @link https://mp.baomidou.com/guide/generator.html
 */
public class CodeGenerator {

    // 数据库 URL
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    // 数据库驱动
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    // 数据库用户名
    private static final String USERNAME = "root";
    // 数据库密码
    private static final String PASSWORD = "123456";
    // @author 值
    private static final String AUTHOR = "tuxiantian";
    // 包的基础路径
    private static final String BASE_PACKAGE_URL = "com.fawkes.project.user.common";
    // xml文件路径
    private static final String XML_PACKAGE_URL = "/src/main/resources/mapper/";
    // xml 文件模板
    private static final String XML_MAPPER_TEMPLATE_PATH = "templates/mapper.xml";
    // mapper 文件模板
    private static final String MAPPER_TEMPLATE_PATH = "templates/mapper.java";
    // entity 文件模板
    private static final String ENTITY_TEMPLATE_PATH = "templates/entity.java";
    // service 文件模板
    private static final String SERVICE_TEMPLATE_PATH = "templates/service.java";
    // serviceImpl 文件模板
    private static final String SERVICE_IMPL_TEMPLATE_PATH = "templates/serviceImpl.java";
    // controller 文件模板
    private static final String CONTROLLER_TEMPLATE_PATH = "templates/controller.java";

    public static void main(String[] args) {


        boolean continueRunning = true;
        Scanner scanner = new Scanner(System.in);
        while (continueRunning) {
            System.out.println("请输入表名，输入'exit'退出程序：");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                continueRunning = false;
                System.out.println("程序已退出。");
                break;
            }
            AutoGenerator generator = new AutoGenerator();
            //project_change_manage
            // 全局配置
            GlobalConfig globalConfig = new GlobalConfig();
            String projectPath = System.getProperty("user.dir");
            projectPath="C:\\workspace\\cybereng-user-api";
            globalConfig.setOutputDir(projectPath + "/src/main/java");
            globalConfig.setAuthor(AUTHOR);
            globalConfig.setOpen(false);
            globalConfig.setFileOverride(false);
            globalConfig.setSwagger2(true);
            globalConfig.setBaseColumnList(true);
            globalConfig.setBaseResultMap(true);
            generator.setGlobalConfig(globalConfig);

            // 数据源配置
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setUrl(URL);
            dataSourceConfig.setDriverName(DRIVER_NAME);
            dataSourceConfig.setUsername(USERNAME);
            dataSourceConfig.setPassword(PASSWORD);
            generator.setDataSource(dataSourceConfig);

            // 包配置
            PackageConfig packageConfig = new PackageConfig();
            packageConfig.setModuleName("gen");
            packageConfig.setParent(BASE_PACKAGE_URL);
            generator.setPackageInfo(packageConfig);

            // 配置自定义代码模板
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setXml(XML_MAPPER_TEMPLATE_PATH);
            templateConfig.setMapper(MAPPER_TEMPLATE_PATH);
            templateConfig.setEntity(ENTITY_TEMPLATE_PATH);
            templateConfig.setService(SERVICE_TEMPLATE_PATH);
            templateConfig.setServiceImpl(SERVICE_IMPL_TEMPLATE_PATH);
            templateConfig.setController(CONTROLLER_TEMPLATE_PATH);
            generator.setTemplate(templateConfig);

            generator.setTemplateEngine(new FreemarkerTemplateEngine());

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            strategy.setEntityLombokModel(true);
            strategy.setRestControllerStyle(true);
//        strategy.setSuperEntityColumns("id");
            strategy.setControllerMappingHyphenStyle(false);
            strategy.setTablePrefix(packageConfig.getModuleName() + "_");
            strategy.setLogicDeleteFieldName("delete_flag");
            strategy.setInclude(input);
            generator.setStrategy(strategy);

            generator.execute();
        }
        scanner.close();
    }

}

