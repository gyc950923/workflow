package com.online.workflow.business.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.workflow.business.service.IBusinessService;
import com.online.workflow.business.service.IOrgInfoService;
import com.online.workflow.process.forms.Form;
import com.online.workflow.process.resources.DeptInfo;
import com.online.workflow.process.resources.MetaData;
import com.online.workflow.process.resources.RoleInfo;
import com.online.workflow.process.resources.UserInfo;
import com.online.workflow.util.DataGridModel;
import com.online.workflow.util.PageModel;
import com.online.workflow.util.TreeNode;

@Controller
@RequestMapping("/business")
public class BusinessController {

    @Resource(name = "busiService")
    private IBusinessService businessService;
    @Resource(name = "orgInfoService")
    private IOrgInfoService orgInfoService;
    
    @RequestMapping("/getAllUsersByPage")
    @ResponseBody
    public DataGridModel getAllUsersByPage(UserInfo userInfo,PageModel pageModel){
        List<UserInfo> userInfos = businessService.getAllUsersByPage(userInfo,pageModel);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(userInfos);
        dataGridModel.setTotal(2);
        return dataGridModel;
    }
    @RequestMapping("/getAllDeptsByPage")
    @ResponseBody
    public DataGridModel getAllDeptsByPage(DeptInfo deptInfo, PageModel pageModel){
        List<DeptInfo> deptInfos = businessService.getAllDeptsByPage(deptInfo, pageModel);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(deptInfos);
        dataGridModel.setTotal(pageModel.getTotal());
        return dataGridModel;
    }
    
    @RequestMapping("/getAllRolesByPage")
    @ResponseBody
    public DataGridModel getAllRolesByPage(RoleInfo roleInfo , PageModel pageModel){
        List<RoleInfo> roleInfos = businessService.getAllRolesByPage(roleInfo, pageModel);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(roleInfos);
        dataGridModel.setTotal(pageModel.getTotal());
        return dataGridModel;
    }
    
    @RequestMapping("/getDeptInfoTree")
    @ResponseBody
    public List<TreeNode> getDeptInfoTree(TreeNode treeNode){
        List<TreeNode> deptInfos = orgInfoService.getDeptInfoTree(treeNode);
        return deptInfos;
    }
    
    @RequestMapping("/getAllFormsByPage")
    @ResponseBody
    public DataGridModel getAllFroms(Form form,PageModel pageModel){
        List<Form> forms = businessService.getAllFromsByPage(form, pageModel);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(forms);
        dataGridModel.setTotal(pageModel.getTotal());
        return dataGridModel;
    }
    
    @RequestMapping("/getAllEntitysByPage")
    @ResponseBody
    public DataGridModel getAllEntitys(MetaData entity,PageModel pageModel){
        List<MetaData> entitys = businessService.getAllEntitysByPage(entity, pageModel);
        DataGridModel dataGridModel = new DataGridModel();
        dataGridModel.setRows(entitys);
        dataGridModel.setTotal(pageModel.getTotal());
        return dataGridModel;
    }
    

}
