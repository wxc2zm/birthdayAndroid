package com.money.birtyday.util;

public interface HttpCallbackListener {
	
	void onSuccess(String respose);
	
	void onError(Exception e);
}
