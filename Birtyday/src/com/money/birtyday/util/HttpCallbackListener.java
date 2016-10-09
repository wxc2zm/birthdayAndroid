package com.money.birtyday.util;

public interface HttpCallbackListener {
	
	void onFinish(String response);
	
	void onError(Exception e);
}
