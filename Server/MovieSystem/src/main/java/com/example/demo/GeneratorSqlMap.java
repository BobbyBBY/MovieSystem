package com.example.demo;

import java.io.File;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 
* @ClassName: GeneratorSqlMap 
* @Description: TODO 自动生成mapper pojo ， mybatis 逆向工程
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午5:30:36 
*
 */

public class GeneratorSqlMap {
	@Test
	public void generator() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		// 指定配置文件
		File configFile = new File("./src/main/resources/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}

//	@Test
////	 执行main方法以生成代码
//	public static void main(String[] args) {
//		try {
//			GeneratorSqlMap generatorSqlmap = new GeneratorSqlMap();
//			generatorSqlmap.generator();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
