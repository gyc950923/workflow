package com.online.workflow.design.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


/**
 * 加载左侧菜单
 */
@WebServlet("/service/editor/stencilset")
public class StencilsetRestResource extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		  response.setContentType("text/html;charset=UTF-8");	
		 // InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
		  InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset_cn.json");
		  String tmp = ""; 
		  
		  tmp = IOUtils.toString(stencilsetStream, "utf-8");
		  
		  response.getWriter().write(tmp);
		  
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
