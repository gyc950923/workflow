package com.online.engine.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


public class PromptControl {
	
	private List<PromptControlItem> items = new ArrayList<PromptControlItem>();
	private String Comments;
	public List<PromptControlItem> getItems() {
		return items;
	}
	public void setItems(List<PromptControlItem> items) {
		this.items = items;
	}
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
	}
	
	

}
