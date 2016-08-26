package com.nt.camel.learn.mergexml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class XmlAppender {
	private final String TOP_ELEMENT = "rows";

	private String defaultXml = "<?xml version=\"1.0\" ?><" + TOP_ELEMENT + "></" + TOP_ELEMENT + ">";

	void doNothing() throws Exception {
		XMLInputFactory inFactory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream("1.xml"));

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(""));
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			writer.add(event);
			if (event.getEventType() == XMLEvent.START_ELEMENT) {
				if (event.asStartElement().getName().toString().equalsIgnoreCase("book")) {
					writer.add(eventFactory.createStartElement("", null, "index"));
					writer.add(eventFactory.createEndElement("", null, "index"));
				}
			}
		}
		writer.close();
	}

	public void Append(String appendToFilePath, String xmlToAppend) throws Throwable {

		File appendToFile = new File(appendToFilePath);
		if (!appendToFile.exists()) {
			CreateOutputFileWithDefaultContent(appendToFile);
		}
		long startTime = System.nanoTime();
		AppendToXml(appendToFile, xmlToAppend);
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		System.out.println("Time taken " + duration/1000000);
		
	}

	private void AppendToXml(File outputFile, String xmlToAppend) throws Throwable {
		final String originalFilePath = outputFile.getAbsolutePath();

		Path origPath = Paths.get(originalFilePath);
		Path tmpPath = Paths.get(originalFilePath + ".tmp");

		Files.move(origPath, tmpPath, StandardCopyOption.REPLACE_EXISTING);

		System.out.println("TMP FILE IS Valid " + (isValidXml(tmpPath.toFile().getAbsolutePath())));
		
		try (Writer outputWriter = new FileWriter(originalFilePath);) {
			XMLOutputFactory xmlOutFactory = XMLOutputFactory.newFactory();
			XMLEventWriter xmlEventWriter = xmlOutFactory.createXMLEventWriter(outputWriter);

			XMLInputFactory inFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream(tmpPath.toFile()));
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				xmlEventWriter.add(event);
				if (event.getEventType() == XMLEvent.START_ELEMENT) {
					if (event.asStartElement().getName().toString().equals(TOP_ELEMENT)) {
						byte[] byteArray = xmlToAppend.getBytes("UTF-8");
						ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

						XMLEventReader xmlEventReader = inFactory.createXMLEventReader(inputStream);
						XMLEvent eventStringXml = xmlEventReader.nextEvent();
						// Skip ahead in the input to the opening document
						// element
						while (eventStringXml.getEventType() != XMLEvent.START_ELEMENT) {
							eventStringXml = xmlEventReader.nextEvent();
						}

						do {
							xmlEventWriter.add(eventStringXml);
							eventStringXml = xmlEventReader.nextEvent();
						} while (eventStringXml.getEventType() != XMLEvent.END_DOCUMENT);
						xmlEventReader.close();
					}
				}
			}
			outputWriter.flush();
		}
		Files.delete(tmpPath);
	}

	private boolean isValidXml(String path) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.parse(new File(path));
			System.out.println("XML sent is " + db.toString());
			return true;
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
			return false;
		}
	}
	
	private void printFile(String path) throws Throwable {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
	}
	
	private void CreateOutputFileWithDefaultContent(File outputFile)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"))) {
			writer.write(defaultXml);
			writer.flush();
		}
	}
}
