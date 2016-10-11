package com.money.birtyday.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
				if (element.elementText("gender").equals("男")) {
					user.isGender(true);
				} else {
					user.isGender(false);
				}
				user.setMobile(element.elementText("mobile"));
				Date date = new Date(element.elementText("birthday"));
				user.setBirthday(date);
				user.setAddress(element.elementText("address"));
				user.setMemo(element.elementText("memo"));
				list.add(user);
			}
			return list;
	}
	
	/**
	 * 返回结果解析
	 * @param respose result:01
	 * @return
	 * 
	 * 01:表示登陆成功
	 * 02:表示登陆失败
	 * 001:更新成功
	 * 003:保存成功
	 * 005:删除成功
	 * 0001:客户端和服务端时间相同
	 * 0002:客户端和服务端时间不同
	 */
	public static int resolveResult(String respose) {
		String[] result = respose.split(":");
		return Integer.parseInt(result[1]);
	}
	
}
