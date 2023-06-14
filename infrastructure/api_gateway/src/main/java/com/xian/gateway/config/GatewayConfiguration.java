package com.xian.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("service-acl",r -> r.path("/admin/**")
                        .uri("lb://service-acl"))
                .route("service-edu",r -> r.path("/edu/**")
                        .uri("lb://service-edu"))
                .route("service-ucenter",r -> r.path("/eduucenter/**")
                        .uri("lb://service-ucenter"))
                .route("service-order",r -> r.path("/serviceorder/**")
                        .uri("lb://service-order"))
                .route("service-statistics",r -> r.path("/statistics/**")
                        .uri("lb://service-statistics"))
                .route("service-oss",r -> r.path("/eduoss/**")
                        .uri("lb://service-oss"))
                .route("service-vod",r -> r.path("/eduvod/**")
                        .uri("lb://service-vod"))
                .route("service-cms",r -> r.path("/educms/**")
                        .uri("lb://service-cms"))
                .route("service-sms",r -> r.path("/edusms/**")
                        .uri("lb://service-sms"))
                .build();
    }
}
