package test.test;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class TestTest {
  @Test(priority=1,dataProvider = "dp",dependsOnMethods= {"f1"})
  public void f(Integer n, String s) {
	  System.out.println("I am main Test");
  }
  
  @Test(priority=0)
  public void f1() {
	  System.out.println("I am Test1");
  }
  
  @Test(priority=2,dependsOnMethods= {"f1"})
  public void f2() {
	  System.out.println("I am  Test2");
  }

  @DataProvider
  public Object[][] dp() {
    return new Object[][] {
      new Object[] { 1, "a" },
      new Object[] { 2, "b" },
    };
  }
  @BeforeTest
  public void beforeTest() {
	  System.out.println("I am before Test");
  }

  @AfterTest
  public void afterTest() {
	  System.out.println("I am after Test");
  }

}
