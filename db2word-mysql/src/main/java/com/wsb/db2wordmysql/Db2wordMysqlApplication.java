package com.wsb.db2wordmysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.wsb.db2wordmysql.dao")
public class Db2wordMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(Db2wordMysqlApplication.class, args);
	}

}
