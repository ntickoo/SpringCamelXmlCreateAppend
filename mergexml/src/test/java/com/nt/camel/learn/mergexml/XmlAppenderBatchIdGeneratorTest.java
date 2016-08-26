package com.nt.camel.learn.mergexml;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlAppenderBatchIdGeneratorTest {

	@Test
	public void testGetBatchId() {
		XmlAppenderBatchIDGenerator gen = new XmlAppenderBatchIDGenerator();
		
		String batchId = gen.getBatchId();
		
		System.out.println("batchId generated is " + batchId);
		assertEquals(batchId, "08262016000000");
				
	}

}
