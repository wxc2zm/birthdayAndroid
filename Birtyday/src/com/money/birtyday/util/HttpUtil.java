package com.money.birtyday.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Environment;
import android.util.Log;


/**
 * 与后台交互
 * @author Administrator
 *
 */
public class HttpUtil {
	private final static String TAG = "HttpUtil";
	private static final String serviceNameSpace = "http://service.appInterface.xfgl.money.com/";
	
	/**
	 * 下载文件
	 */
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
					
					URL url = new URL("http://192.168.78.166:8080/birthday/xml/user.xml");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					
					InputStream inputStream = connection.getInputStream();
					FileOutputStream outputStream = new FileOutputStream(xmlPath + "/user.xml");
					Log.i(HttpUtil.TAG, outputStream.toString());
					BufferedInputStream in = null;
					BufferedOutputStream out = null;
					
					byte[] buffer = new byte[8192];
					try {
						in = new BufferedInputStream(inputStream, buffer.length);
						out = new BufferedOutputStream(outputStream, buffer.length);
				
						
						for (int byteRead = 0; (byteRead = in.read(buffer)) != -1; ) {
							out.write(buffer, 0, byteRead);
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
	/**
	 * 与后台进行交互
	 */
	public static void webService(final Object[] data, final String method, final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// 实例化SoapObject对象
				Log.i(HttpUtil.TAG, "运行到number");
				SoapObject request = new SoapObject(serviceNameSpace,
						method);
				int  number= 0;
				
				for (Object d: data) {
					request.addProperty("arg" + number, d);
					number++;
					
				}
				Log.i(HttpUtil.TAG, "运行完number");
				// 获得序列化Envelope
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = request;
				envelope.dotNet = false;

				HttpTransportSE httpTransportSE = new HttpTransportSE(
						"http://192.168.78.166:8080/birthday/services/AppInterfaceService?wsdl");
				try {
					Log.i(HttpUtil.TAG, "开始向后台发数据");
					httpTransportSE.call(null, envelope);
					Log.i(HttpUtil.TAG, "向后台发数据完成");
					Object response = envelope.getResponse();
					response = envelope.getResponse();
					Log.i(HttpUtil.TAG, response.toString());
					if (response != null) {
						listener.onSuccess(response.toString());
					}
					
				} catch (IOException e) {
					listener.onError(e);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					listener.onError(e);
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
}
