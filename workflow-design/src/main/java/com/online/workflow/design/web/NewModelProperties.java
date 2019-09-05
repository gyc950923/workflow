package com.online.workflow.design.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.online.workflow.process.WorkflowProcess;

/**
 * 
 * ===========================修改历史==========================<br>
 * 修改人:admin<br>
 * 修改时间:2016年12月10日 下午2:49:25<br>
 * 修改内容:创建本文件<br>
 * ------------------------------------------------------------<br>
 *
 * 功能:该类为原新建模板的相关操作servlet,现已不再调用<br>
 * 约束:与本类相关的约束<br>
 * @version 1.0
 * @author admin
 */
@WebServlet("/service/model/newModel")
public class NewModelProperties extends HttpServlet{

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String newModelId = request.getParameter("newModelId");
        
        String option = request.getParameter("option");
        if(Boolean.valueOf(option)){
            HttpSession session = request.getSession();
            session.removeAttribute("workflow");
            session.removeAttribute("id");
            session.removeAttribute("version");
            WorkflowProcess workflow = new WorkflowProcess();
            workflow.setChartId(newModelId);
            request.getSession().setAttribute("workflow", workflow);
        }
        
        response.getWriter().print("true");
    }
    

}
