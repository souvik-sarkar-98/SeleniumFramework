package com.oldmutual.testautomation.driver;

import java.nio.file.Paths;

import framework.automation.selenium.core.TestEngine;
import framework.automation.selenium.core.helpers.TestActionListener;

public final class TestDriver {

	private TestEngine engine;

	private void setUpTest(Class<?> class1) throws Exception {
		try {
			TestEngine.setPropertyFile(Paths.get(class1.getResource("property.xml").toURI()).toString());
		} catch (NullPointerException e) {
			throw new RuntimeException("No 'property.xml' file found at '"
					+ class1.getPackage().getName().replace(".", "\\")
					+ "' under test resources folder..");
		}
	}
	
	//hello

	private final void startTest(Class<?> class1) throws Exception {
		this.engine = new TestEngine(class1);
		TestActionListener tl=new TestActionListener() {
			
			@Override
			public void uploadEvidence(String fileName) {
				System.err.println("uploading file ");
				
			}
			
			@Override
			public void passTestCase(String testCaseName, String message) {
				System.err.println(testCaseName+" is "+message);
				
			}
			
			@Override
			public void failTestCase(String testCaseName, String message, Exception e) {
				System.err.println(testCaseName+" is "+message+" due to "+ e);
				
			}
		};
		this.engine.run(tl);
	}

	private void endTest() {
		this.engine.stop();
	}

	public static void start(Class<?> class1) {
		TestDriver td = new TestDriver();
		try {
			td.setUpTest(class1);
			td.startTest(class1);
			//System.err.println("ok66");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			td.endTest();
			//System.err.println("ok");
		}
	}

}
