package com.hiekn.plantdata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@MapperScan(basePackages = "com.hiekn.plantdata.mapper")
@EntityScan(basePackages="com.hiekn.plantdata.Entity")
@EnableTransactionManagement
public class DataGraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataGraphApplication.class, args);
	}
}
