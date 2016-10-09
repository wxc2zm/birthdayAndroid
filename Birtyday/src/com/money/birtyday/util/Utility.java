package com.money.birtyday.util;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import android.text.TextUtils;

import com.money.birtyday.db.BirthdayDB;
import com.money.birtyday.model.User;

public class Utility {
	
	public static boolean handleUserResponse(BirthdayDB birthdayDB, String response) throws DocumentException {
		if (!TextUtils.isEmpty(response)) {
			
			Document document = null;
			document = DocumentHelper.parseText(response);
			Element root = document.getRootElement();
			Iterator iterator = root.elementIterator();
			while (iterator.hasNext()){
				Element element = (Element) iterator.next();
				User user = new User();
				//user.setId(element.elementText("id"));
				user.setName(element.elementText("name"));
				user.setGender(element.elementText("gender"));
				user.setMobile(element.elementText("mobile"));
				user.setBirthday(element.elementText("birthday"));
				user.setAddress(element.elementText("address"));
				user.setMemo(element.elementText("memo"));
				birthdayDB.saveUser(user);
			}
			return true;
		}
		return false;
	}
	
}
