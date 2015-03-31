package com.growthbeat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.growthbeat.model.Intent;
	
public class CustomIntent implements IntentHandler {

	public static Map<String, String> initWithMap(Map<String, String> extra) {
		
		Map<String, String> extraReture = new HashMap<String, String>();
		
        for (Entry<String, String> entry : extra.entrySet()) {
            if (entry != null && entry.getValue().equals("extra")) {
            	extraReture.put(entry.getKey(), entry.getValue());
            }
        }
		return extraReture;
	}
	
	@Override
	public boolean handleIntent(Intent intent) {
		
		return false;
	}
	

}
