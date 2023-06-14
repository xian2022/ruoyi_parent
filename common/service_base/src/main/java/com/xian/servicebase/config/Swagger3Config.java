package com.xian.servicebase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger3Config {

    @Bean
    public Docket restA() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select() // 扫描路径
                .paths(PathSelectors.any())
                .build()
                ;
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("网站-课程中心API文档")
                .description("本文档描述了课程中心微服务接口定义")
                .version("3.0.0")
                .contact(new Contact("xian", "http://www.xian.com",
                        "1834206437@qq.com"))
                .build()
                ;
    }
}
