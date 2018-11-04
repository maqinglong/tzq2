package com.tzq;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tzq.dao.ecsUsersMapper;
import com.tzq.model.ecsUsers;

/**
 *  主程序
 * @author 马庆龙
 *
 */
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class})
@EnableEurekaClient
@SpringBootApplication
@Configuration
@RequestMapping(value="/")
//@MapperScan(basePackages = {"com.tzq.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class ManageserviceApplication {
	private Logger logger = LoggerFactory.getLogger(ManageserviceApplication.class.getName());
	@Value("${mysql.database.url}")	
    private String dbUrl;
	@Autowired
	private ecsUsersMapper ecsUsersMapper1;
	
	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "{name: wenwu}" + dbUrl;
	}
	@RequestMapping("/ccc")
	@ResponseBody
	String ccc() {
		ecsUsers esuser = ecsUsersMapper1.selectByPrimaryKey(29) ;
		
		return "{name: ccc}" + esuser.getTeacher();
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(ManageserviceApplication.class, args);
	}
}
