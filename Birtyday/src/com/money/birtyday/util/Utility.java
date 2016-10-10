package com.money.birtyday.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.money.birtyday.model.User;

public class Utility {
	
	/**
	 * 解析xml文件
	 * @param response
	 * @return
	 * @throws DocumentException
	 */
	public static List<User> handleUserResponse(String response) throws DocumentException{
		
			List<User> list = new ArrayList<User>();
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(response));
			//document = DocumentHelper.parseText(response);
			Element root = document.getRootElement();
			@SuppressWarnings("rawtypes")
			Iterator iterator = root.elementIterator();
			while (iterator.hasNext()){
				Element element = (Element) iterator.next();
				User user = new User();
				user.setId(element.elementText("id"));
				user.setName(element.elementText("name"));
				user.setGender(element.elementText("gender"));
				user.setMobile(element.elementText("mobile"));
				user.setBirthday(element.elementText("birthday"));
				user.setAddress(element.elementText("address"));
				user.setMemo(element.elementText("memo"));
				list.add(user);
			}
			return list;
	}
	
}
