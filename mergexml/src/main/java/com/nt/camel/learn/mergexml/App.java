package com.nt.camel.learn.mergexml;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/routes.xml");
		CamelContext camelContext = SpringCamelContext.springCamelContext(appContext, false);
		try {
			camelContext.start();
		} finally {
			Thread.sleep(10 * 1000);
			camelContext.stop();
		}
	}
}
