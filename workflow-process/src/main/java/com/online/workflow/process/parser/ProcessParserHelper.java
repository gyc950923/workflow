package com.online.workflow.process.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.online.workflow.process.WorkflowProcess;
import com.online.workflow.process.resources.XPDLNames;

public class ProcessParserHelper {
	public static String  ProcessToXML(WorkflowProcess process) {
		if (process == null)
			return "";

		Element root = DocumentHelper
				.createElement(XPDLNames.XPDL_WorkflowProcess);
		Document document = DocumentHelper.createDocument(root);

		WorkflowProcessParser parser = new WorkflowProcessParser();
		parser.DoSerialize(process, root);

		return document.asXML();

	}

	public static WorkflowProcess XMLToProcess(String strXml) {

		// 创建xml解析对象
		SAXReader reader = new SAXReader();
		// 定义一个文档
		Document document = null;
		// 将字符串转换为

		try {
			document=reader.read(new ByteArrayInputStream(strXml.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		// 获取根元素
		Element root = document.getRootElement();
		WorkflowProcess process=new WorkflowProcess();

		WorkflowProcessParser parser = new WorkflowProcessParser();
		parser.DoParse(root,process);

		return process;

	}

	public static void main(String[] args) {
		// 第二种方式:创建文档并设置文档的根元素节点
		Element root2 = DocumentHelper.createElement("student");
		Document document2 = DocumentHelper.createDocument(root2);

		// 添加属性
		root2.addAttribute("name", "zhangsan");
		// 添加子节点:add之后就返回这个元素
		Element helloElement = root2.addElement("hello");

		Element jpl = helloElement.addElement("gou");
		jpl.setText("ffdfdf");

		Element worldElement = root2.addElement("world");

		helloElement.setText("hello Text");
		worldElement.setText("world text");

		// 输出
		// 输出到控制台
		XMLWriter xmlWriter = new XMLWriter();
		try {
			xmlWriter.write(document2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
