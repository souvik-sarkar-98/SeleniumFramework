package oldmutual.servicing;

import drivers.core.TestDriver;

public class OmServicingTest extends TestDriver{
	public static void main(String[] args) {
		start();
	}
	
	
	@Override
	public void init() {
		System.out.println("parent init");
	}

	

	
}
