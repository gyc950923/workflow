package com.online.workflow.design.web;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String PATH = "d:/flow_design/data";
		String DEL_PATH = "d:/flow_design/del";
		
		File srcFile = new File(PATH + "/sid-B28CF5D1-50E1-4BF1-80A8-6E102B3247D1.json");
		File destFile = new File(DEL_PATH + "/sid-B28CF5D1-50E1-4BF1-80A8-6E102B3247D1.json");
		
		try {
		
			FileUtils.moveFile(srcFile, destFile);
			System.out.println("11111111");
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
