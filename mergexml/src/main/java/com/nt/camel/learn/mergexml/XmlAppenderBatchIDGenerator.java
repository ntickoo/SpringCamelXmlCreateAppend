package com.nt.camel.learn.mergexml;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class XmlAppenderBatchIDGenerator {

	private int interval = 15;
	@SuppressWarnings("deprecation")
	public String getBatchId(){
		Date currDateTime = new Date();
	
		int min = (currDateTime.getMinutes() / interval ) * interval;
		
		Date batchDateStamp = new Date(currDateTime.getYear(), currDateTime.getMonth(), currDateTime.getDate(),currDateTime.getHours(), min, 0);
		
		
		SimpleDateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
		return df.format(batchDateStamp);
	}
}
