package com.money.birtyday.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;


/**
 * 从后台下载文件
 * @author Administrator
 *
 */
public class HttpUtil {
	
	public static void sendHttpRequest() {
		new Thread(new Runnable() {
			private String xmlPath = Environment.getExternalStorageDirectory() + "/birthday/xml";
			@Override
			public void run() {
				HttpURLConnection connection = null;
				if (!(new File(xmlPath)).exists()) {
					new File(xmlPath).mkdirs();
				}
				try {
					
					URL url = new URL("http://192.168.2.105:8080/birthday/xml/user.xml");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					
					InputStream inputStream = connection.getInputStream();
					FileOutputStream outputStream = new FileOutputStream(xmlPath + "/user.xml");
					
					BufferedInputStream in = null;
					BufferedOutputStream out = null;
					
					byte[] buffer = new byte[8192];
					try {
						in = new BufferedInputStream(inputStream, buffer.length);
						out = new BufferedOutputStream(outputStream, buffer.length);
						int total = 0;
						
						for (int byteRead = 0; (byteRead = in.read(buffer)) != -1; ) {
							out.write(buffer, 0, byteRead);
							total += byteRead;
						}
					} finally {
						in.close();
						out.close();
					}
				} catch (Exception e) {
					
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
				
			}
		}).start();
	}
}
