package drivers.core;

import java.nio.file.Paths;

import framework.automation.selenium.core.TestEngine;

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
		this.engine.run();
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
