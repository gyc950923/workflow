package com.online.workflow.design.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.online.workflow.design.workflow.bean.FlowNodeProperties;

/**
 * @author Administrator
 * 节点属性操作类
 *
 */
public class NodePropertiesUtils {

	//重复集合 
	private static Map<String,List<FlowNodeProperties>> redoMap = new HashMap<String,List<FlowNodeProperties>>();
	//撤销集合,初始化时将流程全部属性载入
	private static Map<String,List<FlowNodeProperties>> undoMap = new HashMap<String,List<FlowNodeProperties>>();
	
	
	/**
	 * 初始化
	 * @param modelId
	 * @param list
	 */
	public static void init(String modelId,List<FlowNodeProperties> list){
		
		if(null != list){
			undoMap.put(modelId, list);
		}else{
			List<FlowNodeProperties> undoList = new ArrayList<FlowNodeProperties>();
			undoMap.put(modelId, undoList);
		}
		
	}
	
	/**
	 * 得到指定流程的所有属性
	 * @param modelId
	 * @return
	 */
	public static List<FlowNodeProperties> getCurrentPropertiesList(String modelId){
		
		List<FlowNodeProperties> list = undoMap.get(modelId);
		if(null != list){
			return list;
		}else{
			return null;
		}
	}
	
	/**
	 * 得到指定流程下指定节点的属性
	 * @param modelId
	 * @param resourceId
	 * @return
	 */
	public static FlowNodeProperties getUndoFlowNodePropertiesById(String modelId,String resourceId){
		
		List<FlowNodeProperties> list = undoMap.get(modelId);
		FlowNodeProperties fnp = getFlowNodePropertiesById(list, resourceId);
		
		return fnp;
	}
	
	/**
	 * 删除属性
	 * @param modelId
	 * @param resourceId
	 * @return
	 */
	public static void delFlowNodeProperties(String modelId,String resourceId){
		
	
		List<FlowNodeProperties> list = undoMap.get(modelId);//获取undo的list
		FlowNodeProperties nodePro = getFlowNodePropertiesById(list, resourceId);//获取对应的元素
		List<FlowNodeProperties> redoList = getRedoList(modelId);//获取redo的list
		
		delFlowNodePropertiesById(redoList, resourceId);//redoList中如果存在resourceId的元素,删除
		
		if(null != nodePro){
			redoList.add(nodePro);
			list.remove(nodePro);
		}
	}
	
	/**
	 * undo新增,适用于新增组件时增加默认属性或表单属性提交
	 * @param modelId
	 * @param fp
	 */
	public static void undoAdd(String modelId,FlowNodeProperties fp){
		
		List<FlowNodeProperties> list = undoMap.get(modelId);//获取undo的list
		List<FlowNodeProperties> redoList = getRedoList(modelId);//获取redo的list
		
		//清理老数据
		delFlowNodePropertiesById(list, fp.getResourceId());
		delFlowNodePropertiesById(redoList, fp.getResourceId());
		
		list.add(fp);
	}
	
	/**
	 * 已有属性的撤消操作
	 * @param modelId
	 * @param resourceId
	 */
	public static void undoAdd(String modelId,String resourceId){
		
		List<FlowNodeProperties> list = undoMap.get(modelId);//获取undo的list
		List<FlowNodeProperties> redoList = getRedoList(modelId);//获取redo的list
		FlowNodeProperties nodePro = getFlowNodePropertiesById(redoList, resourceId);//获取对应的元素
		
		//清理老数据
		delFlowNodePropertiesById(list,resourceId);
		delFlowNodePropertiesById(redoList,resourceId);
		
		if(null != nodePro){
			list.add(nodePro);
		}
	}
	
	/**
	 * 已有属性的重复操作
	 * @param modelId
	 * @param resourceId
	 */
	public static void redoAdd(String modelId,String resourceId){
		
		List<FlowNodeProperties> list = undoMap.get(modelId);//获取undo的list
		List<FlowNodeProperties> redoList = getRedoList(modelId);//获取redo的list
		FlowNodeProperties nodePro = getFlowNodePropertiesById(list, resourceId);//获取对应的元素
		
		//清理老数据
		delFlowNodePropertiesById(list,resourceId);
		delFlowNodePropertiesById(redoList,resourceId);
		
		if(null != nodePro){
			redoList.add(nodePro);
		}
	}
	
	/**
	 * 删除指定节点属性
	 * @param modelId
	 * @param resourceId
	 * @return
	 */
	private static boolean delFlowNodePropertiesById(List<FlowNodeProperties> list,String resourceId){
		
		boolean ret = false;
	
		for(FlowNodeProperties fn : list){
			
			if(fn.getResourceId().equals(resourceId)){
				ret = true;
				list.remove(fn);
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 删除指定节点属性
	 * @param modelId
	 * @param resourceId
	 * @return
	 */
	private static FlowNodeProperties getFlowNodePropertiesById(List<FlowNodeProperties> list,String resourceId){
		
		FlowNodeProperties ret = null;
	
		for(FlowNodeProperties fn : list){
			
			if(fn.getResourceId().equals(resourceId)){
			
				ret = fn;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 得到当前流程id下的redoList
	 * @param modelId
	 * @return
	 */
	private static List<FlowNodeProperties> getRedoList(String modelId){
		
		List<FlowNodeProperties> redoList = redoMap.get(modelId);
		if(null == redoList){
			
			redoList = new ArrayList<FlowNodeProperties>();
			redoMap.put(modelId, redoList);
		}
		
		return redoList;
	}

	/**
	 * 
	 * 功能:首次保存modelId不存在是，替换默认的modelId<br>
	 * 约束:与本函数相关的约束<br>
	 * @param oldModelId
	 * @param newModelId
	 */
    public static void change(String oldModelId, String newModelId) {
        undoMap.put(newModelId, undoMap.get(oldModelId));
        undoMap.remove(oldModelId);
    }
	
}
