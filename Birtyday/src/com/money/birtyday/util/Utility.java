package com.money.birtyday.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.util.Log;

import com.money.birtyday.activity.LoginActivity;
import com.money.birtyday.model.User;

public class Utility {
	
	private static final String TAG = "utility";
	/**
	 * 解析xml文件
	 * @param response
	 * @return
	 * @throws DocumentException
	 * @throws ParseException 
	 */
	public static List<User> handleUserResponse(String response) throws DocumentException, ParseException{
		
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
				Log.i(Utility.TAG, element.elementText("gender"));
				if (element.elementText("gender").equals("男")) {
					user.setGender(true);
				} else {
					user.setGender(false);
				}
				user.setMobile(element.elementText("mobile"));
				Log.i(Utility.TAG, element.elementText("birthday"));
				//Date date = new Date(Date.parse(element.elementText("birthday").split(" ")[0]));
				//Log.i(Utility.TAG, date.toString());
				user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(element.elementText("birthday")));
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
