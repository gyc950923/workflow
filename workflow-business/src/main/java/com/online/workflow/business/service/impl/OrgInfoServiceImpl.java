package com.online.workflow.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.online.engine.model.Org;
import com.online.workflow.business.dao.IBusinessDao;
import com.online.workflow.business.service.IOrgInfoService;
import com.online.workflow.util.TreeNode;

@Service("orgInfoService")
public class OrgInfoServiceImpl implements IOrgInfoService{

    @Resource(name="businessDao")
    private IBusinessDao businessDao;
    
    @Override
    public List<TreeNode> getDeptInfoTree(TreeNode treeNode) {
        List<Org> orgs = businessDao.getOrgTree(treeNode);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (Org org : orgs) {
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(org.getId()));
            node.setName(org.getDepartName());
            treeNodes.add(node);
        }
        return treeNodes;
    }
    

}
