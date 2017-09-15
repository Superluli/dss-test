package com.superluli.dsstest.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MyFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Used this filter to check how sevlet pre-parse paylaod of different content-type.
	 * multipart/form-data : will pre-parse and put to  HttpServletRequest.getPart(), so inputstream is consumed already
	 * application/x-www-form-urlencoded : form data will be processed and put to HttpServletRequest.getParameter(). BUT seems like binary data is dropped and no way to access it. 
	 * application/octet-stream (or whatever pre-degined mime type): no pre-parse, can get raw data from inputstream 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		InputStream in = httpServletRequest.getInputStream();
		int next = -1;
		byte[] buffer = new byte[128];

		//process raw data
		while ((next = in.read(buffer)) != -1) {
			System.err.println(Arrays.toString(buffer));
		}
		
		/**
		 * process form data
		 */
		System.err.println(request.getParameter("name"));
		System.err.println(request.getParameter("after"));
		System.err.println(request.getParameter("file"));
		
		
		Enumeration<String> attrNames = request.getParameterNames();
		while (attrNames.hasMoreElements()) {
			System.err.println(attrNames.nextElement());
		}
		
		
		
		System.err.println("raw input parsing done");
		
		/*
		 * This is for multi part
		 */
//		Part part = httpServletRequest.getPart("file");
//		in = part.getInputStream();
//		next = -1;
//		while ((next = in.read(buffer)) != -1) {
//			System.err.println(Arrays.toString(buffer));
//		}
//		in.close();
//		System.err.println("multipart parsing done");
		
		
		response.setContentType("text/plain");
		response.getWriter().println("LOL");
		in.close();
	}
	
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
