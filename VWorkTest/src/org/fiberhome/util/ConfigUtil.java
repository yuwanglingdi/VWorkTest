package org.fiberhome.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

	//TODO map 改为private 封装获取配置信息方法
	public static Map<String, String> configMap;
	private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	
	static {
		configMap = new HashMap<>();
		Properties prop = new Properties();     
        try {
            InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(in);
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key = it.next();
                String value = prop.getProperty(key);
                configMap.put(key, value);
                logger.info("{} : {}", key, value);
            }
            in.close();
            
            /*FileOutputStream oFile = new FileOutputStream("b.properties", true);
            prop.setProperty("phone", "10086");
            prop.store(oFile, "The New properties file");
            oFile.close();*/
        } catch(Exception e){
            e.printStackTrace();
        }
	}
	
}
