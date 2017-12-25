package org.fiberhome.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {

	public static final String HEADER = "HEADER";
	public static final String PARAM = "PARAM";
	public static final String DATA = "DATA";
	
	public static Map<String, Object> getRequestInfo(HttpServletRequest req, boolean printFlag, boolean returnFlag) {
		
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
			if(printFlag){
				System.out.println("Header name " + headerName + " value " + req.getHeader(headerName));
			}
			if(returnFlag){
				headerMap.put(headerName, req.getHeader(headerName));
			}
		}
		resultMap.put(HEADER, headerMap);
		
		Map<String, String> paramMap = returnFlag ? new HashMap<String, String>() : null;
		Enumeration<String> paramNames = req.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if(printFlag){
				System.out.println("Parameter name " + paramName + " value " + req.getParameter(paramName));
			}
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
		if(printFlag){
			System.out.println("Data " + dataStr);
		}
		if(returnFlag){
			resultMap.put(DATA, dataStr);
		}
		
		return resultMap;
	}
	
}
