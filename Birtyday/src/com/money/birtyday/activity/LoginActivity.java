package com.money.birtyday.activity;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.money.birtyday.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private static final String TAG_ACTIVITY = "LoginActivity";
	private EditText editText1;
	private EditText editText2;
	private Button button;
	private String username;
	private String password;
	private static final String serviceNameSpace = "http://appInterface.xfgl.money.com/";
	private static final String guestLogin = "guestLogin";
	private HttpTransportSE httpTransportSE;
	private SoapSerializationEnvelope envelope;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_admin);
		editText1 = (EditText) findViewById(R.id.username);
		editText2 = (EditText) findViewById(R.id.password);
		button = (Button) findViewById(R.id.login);
		Log.i(LoginActivity.TAG_ACTIVITY, "1111111111111");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = editText1.getText().toString();
				password = editText2.getText().toString();
				// 实例化SoapObject对象
				SoapObject request = new SoapObject(serviceNameSpace,
						guestLogin);
				request.addProperty("arg0", username);
				request.addProperty("arg1", password);
				Log.i(LoginActivity.TAG_ACTIVITY, "22222222222222222");

				// 获得序列化Envelope
				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				Log.i(LoginActivity.TAG_ACTIVITY, "333333333333333333");
				envelope.bodyOut = request;
				envelope.dotNet = false;

				httpTransportSE = new HttpTransportSE(
						"http://192.168.2.105:8080/Service/AppInterfaceService?wsdl");
				Log.i(LoginActivity.TAG_ACTIVITY, "444444444444444444");

				new Thread(new Runnable() {

					@Override
					public void run() {
						Log.i(LoginActivity.TAG_ACTIVITY,
								"55555555555555555");
						try {
							httpTransportSE.call(serviceNameSpace + guestLogin,
									envelope);
							Log.i(LoginActivity.TAG_ACTIVITY,
									"6666666666666666");
							Object response;
							response = envelope.getResponse();
							if (response != null) {
								String result = response.toString();
								Log.i(LoginActivity.TAG_ACTIVITY, "RESULT"
										+ result);

							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();

			}
		});
	}

}
