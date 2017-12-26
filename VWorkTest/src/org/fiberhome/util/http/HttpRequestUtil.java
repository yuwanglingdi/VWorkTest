package org.fiberhome.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtil {

	public static final String HEADER = "HEADER";
	public static final String PARAM = "PARAM";
	public static final String DATA = "DATA";
	
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);
	
	public static Map<String, Object> getRequestInfo(HttpServletRequest req, boolean returnFlag) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		Map<String, String> headerMap = returnFlag ? new HashMap<String, String>() : null;
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			logger.info("Header name {} value {}", headerName, req.getHeader(headerName));
			if(returnFlag){
				headerMap.put(headerName, req.getHeader(headerName));
			}
		}
		resultMap.put(HEADER, headerMap);
		
		Map<String, String> paramMap = returnFlag ? new HashMap<String, String>() : null;
		Enumeration<String> paramNames = req.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			logger.info("Parameter name {} value {}", paramName, req.getParameter(paramName));
			if(returnFlag){
				paramMap.put(paramName, req.getParameter(paramName));
			}
		}
		resultMap.put(PARAM, paramMap);
		
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = req.getReader();){
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String dataStr = null;
		try {
			dataStr = URLDecoder.decode(sb.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("Data {}", dataStr);
		if(returnFlag){
			resultMap.put(DATA, dataStr);
		}
		
		return resultMap;
	}
	
}
