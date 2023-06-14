package com.atguigu.demo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeGeneratorApplication {
    public static void main(String[] args) {

        List<String> tables = new ArrayList<>();
        tables.add("edu_comment");

        // 项目路径
        String projectPath = "E:\\BootProject\\guli_parent\\service\\service_edu";

        // 代码生成器
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/guli?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai", "root", "root")
                // 全局配置
                .globalConfig(builder -> {
                    // 作者
                    builder.author("xian")
                            // 输出路径
                            .outputDir(projectPath + "/src/main/java")
                            // 禁止打开输出目录
                            .disableOpenDir()
                            // 开启swagger
                            .enableSwagger()
                            // 注释日期
                            .commentDate("yyyy/MM/dd HH:mm")
                            .dateType(DateType.ONLY_DATE)
                            // 开启覆盖之前生成的文件
                            //.fileOverride()
                    ;
                })
                .packageConfig(builder -> {
                    builder.parent("com.xian")
                            .moduleName("eduservice")
                            .entity("entities")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper/"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            // 增加过滤表前缀
                            .addTablePrefix("edu_")
                            // service策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            // entity策略配置
                            .entityBuilder()
                            // 数据库表映射到实体的命名策略
                            .naming(NamingStrategy.underline_to_camel)
                            // 数据库表字段映射到实体的命名策略
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // 开启lombok模型
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            // controller策略设置
                            .controllerBuilder()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .enableHyphenStyle()
                            // mapper策略设置
                            .mapperBuilder()
                            // 生成通用的resultMap
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
