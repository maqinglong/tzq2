package com.tzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableEurekaClient
@EnableAutoConfiguration
@SpringBootApplication
@Configuration
@RequestMapping(value="/gatewaytowenwu")
public class GatewayserviceApplication {
	@RequestMapping("/aa")
	@ResponseBody
	String home() {
		return "{name: gatewayservice}" ;
	}
	public static void main(String[] args) {
		SpringApplication.run(GatewayserviceApplication.class, args);
	}
}
