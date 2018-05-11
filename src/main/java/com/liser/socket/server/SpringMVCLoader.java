package com.liser.socket.server;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;

public class SpringMVCLoader {

	private static Logger logger = Logger.getLogger(SpringMVCLoader.class);
	private static ApplicationContext rootContext;
	private static XmlWebApplicationContext mvcContext = null;
	private static DispatcherServlet dispatcherServlet = null;

	public SpringMVCLoader(ApplicationContext rootContext){
		this.rootContext = rootContext;
	}

	public static final void init() throws ServletException {

		if (mvcContext == null) {
			mvcContext = new XmlWebApplicationContext();
			mvcContext.setConfigLocation("classpath:/spring/spring-mvc.xml");
			mvcContext.setParent(rootContext);

			MockServletConfig servletConfig = new MockServletConfig(SpringMVCLoader.getMvcContext().getServletContext(), "springMVC");
			dispatcherServlet = new DispatcherServlet(SpringMVCLoader.getMvcContext());
			dispatcherServlet.init(servletConfig);

			logger.info("springMVC loading success");
		}
	}

	public static WebApplicationContext getMvcContext() {
		return mvcContext;
	}

	public static DispatcherServlet getDispatcherServlet() {
		return dispatcherServlet;
	}
}
