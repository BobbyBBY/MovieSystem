package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.management.CycleManagner;

/**
 * 
* @ClassName: MovieSystemApplication 
* @Description: TODO
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午6:58:06 
*
 */
@SpringBootApplication
public class MovieSystemApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(MovieSystemApplication.class, args);
		CycleManagner.getInstance();
	}

}
