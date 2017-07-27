package util;


import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.*;
import java.util.Properties;

//import org.apache.log4j.Logger;


/**
 * @author  
 *
 */
public final class PropertiesLoaderUtils {

	private static Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtils.class);
	private static String PUSH_CONFIG = "/push-config.properties";
	private static String[] resources={"/push-config.properties", "/activemq.properties","/mysql.properties","/redis.properties"};
	public static Properties multiProp;
	public static Properties singleProp;
	
	static{
		multiProp=loadMultiProperties();
		singleProp=loadSingleProperties();
	}


	/**
	 *
	 * @return
	 */
	private static Properties loadSingleProperties() {
		if(singleProp!=null){
			return singleProp;
		}

		String config = System.getProperty("push.config", PUSH_CONFIG) ;

		singleProp=new Properties();
		InputStream is = null;

		try {
			is = PropertiesLoaderUtils.class.getResourceAsStream(config);
			singleProp.load(is);
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
		return singleProp;
	}
	
	private static Properties loadMultiProperties() {
		if(multiProp!=null){
			return multiProp;
		}	
		multiProp=new Properties();
		InputStream is = null;

		try {
			for(String location:resources){
				is = PropertiesLoaderUtils.class.getResourceAsStream(location);
				multiProp.load(is);
			}
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
		return multiProp;
	}

	/**
	 *
	 * @return
	 */
	public static Properties loadProperties(String filePath) {
		Properties prop = new Properties();
		InputStream is = null;

		try {
			is = PropertiesLoaderUtils.class.getResourceAsStream(filePath);
			prop.load(is);
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
		return prop;
	}

	/**
	 * 加载json文件 ， 例 jdbc.json
	 * @param configPath
	 * @return
	 */
	public static JsonObject getJsonConf(String configPath) {
		logger.info("config Path: " + configPath);
		JsonObject conf = new JsonObject();
		ClassLoader ctxClsLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = ctxClsLoader.getResourceAsStream(configPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
		try {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			conf = new JsonObject(sb.toString());
			logger.info("Loaded config file from [" + configPath + "] : " + conf.toString());
		} catch (Exception e) {
			logger.error("Failed to load config file" + e);
		}
		return conf;
	}

	public static void main(String[] args) {
		System.out.println(PropertiesLoaderUtils.multiProp.getProperty("xiaomi.packagename"));
	}
}
