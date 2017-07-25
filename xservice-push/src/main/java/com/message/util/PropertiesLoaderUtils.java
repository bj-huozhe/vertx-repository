package com.message.util;


import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

//import org.apache.log4j.Logger;


/**
 * @author  
 *
 */
public final class PropertiesLoaderUtils {

	private static Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtils.class);
	/**
	 * read properties
	 * 
	 * @param resourceName "/"
	 * @return Properties
	 */
	public static Properties loadUrlProperties(String resourceName) {
		
		String runModel = System.getProperty("car.runmodel");

		String fileName = "/resources/"+runModel+"/"+resourceName;

		Properties props = new Properties();
		InputStream is = null;

		logger.info("PropertiesLoaderUtils 系统读取配置" + fileName);
		URL url = PropertiesLoaderUtils.class.getResource(fileName);
		URLConnection con;
		try {
			con = url.openConnection();
			con.setUseCaches(false);
			is = con.getInputStream();
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}

	/**
	 * read properties
	 * @param resourceName "/"
	 * @return Properties
	 */
	public static Properties loadStreamProperties(String resourceName) {

		Properties props = new Properties();
		InputStream is = null;

		try {
			is = PropertiesLoaderUtils.class.getResourceAsStream(resourceName);
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return props;
	}
}
