package com.xinge.demo;

import com.google.common.collect.Sets;
import com.xinge.demo.core.entity.SuccessResult;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author BG343674
 * created by BG343674 on 2019/1/23
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {

        // 只返回200
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(HttpStatus.SC_OK).message("OK").responseModel(new ModelRef(SuccessResult.class.getSimpleName())).build());
        Set<String> produces = Sets.newHashSet();
        produces.add(MediaType.APPLICATION_JSON_VALUE);
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(produces)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Swagger2.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档")
                .description("接口文档")
                .termsOfServiceUrl("")
                .contact(new Contact("developer", "", ""))
                .version("1.0.0")
                .build();
    }

}
