package drivers.core;

public class TestDriver implements TestMethods{
	
	public void init() {
		System.out.println("parent init");
	}
	
	public final static void start() {
		//init();
		System.out.println("parent start");

	}

}
