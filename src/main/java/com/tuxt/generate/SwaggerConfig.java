package com.tuxt.generate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).enable(true).apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.tuxt.generate.gen.controller")).apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build().pathMapping("/");
    }

    // 配置swagger基本信息
    private ApiInfo apiInfo(){
        Contact contact = new Contact("xxx", "hyc.com", "3132774018@qq.com");
        return new ApiInfo(
                "XXX的swagger",
                "签名",
                "1.0",
                "hyc.com",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

}