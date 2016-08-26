package com.nt.camel.learn.mergexml;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class XmlAppenderProcessor implements Processor {

	private final String FILE_PREFIX="allMsgs_";
	
	private String batchFileLocation;

	public String getBatchFileLocation() {
		return "input";
	}

	public void setBatchFileLocation(String batchFileLocation) {
		System.out.println("Setting location to" + batchFileLocation);
		this.batchFileLocation = batchFileLocation;
	}
	
	private String getBatchFileName(){
		return 
				FILE_PREFIX 
			 + (new XmlAppenderBatchIDGenerator().getBatchId()) 
			 + ".xml";
	}
	
	private String getBatchFilePath(){
		return getBatchFileLocation() + File.separator + getBatchFileName();
	}

	public void process(Exchange exchange) throws Exception {
		String toMergeXml = exchange.getIn().getBody(String.class);
		
		System.out.println("XML is " + toMergeXml);
		
		String batchFile = getBatchFilePath();
		
		try {
			new XmlAppender().Append(batchFile, toMergeXml);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
