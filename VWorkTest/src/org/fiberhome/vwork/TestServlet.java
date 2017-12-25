package org.fiberhome.vwork;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fiberhome.util.http.HttpRequestUtil;
import org.fiberhome.util.http.SynchronizationUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Test Servlet doGet");
		
		HttpRequestUtil.getRequestInfo(req, true, true);

		JSONObject json = new JSONObject();
		try {
			json.put("msg", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(json.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Test Servlet doPost");

		req.setCharacterEncoding("utf-8");
		
		Map<String, Object> reqMap = HttpRequestUtil.getRequestInfo(req, true, true);
		if(reqMap.get(HttpRequestUtil.DATA) == null){
			return;
		}
		JSONObject reqDataJson = null;
		try {
			reqDataJson = new JSONObject(reqMap.get(HttpRequestUtil.DATA).toString());
			if(reqDataJson.getString("event").equals("synchronize")){
				SynchronizationUtil.synchronize(reqDataJson);
				return;
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		
	}

}
