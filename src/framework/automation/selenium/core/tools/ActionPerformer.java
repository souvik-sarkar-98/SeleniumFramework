package framework.automation.selenium.core.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.automation.selenium.core.config.PropertyCache;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public class ActionPerformer {

	protected List<Class<?>> classObjects = new ArrayList<Class<?>>();
	protected final WebDriver driver;
	protected List<String> classNameList=new ArrayList<String>();
	/**
	 * @throws ClassNotFoundException 
	 * 
	 */
	public ActionPerformer(final WebDriver driver) throws ClassNotFoundException {
		this.driver = driver;
		Object[] all = PropertyCache.getProperties("ActionClasses");
		for (Object className : all) {
			if(!className.toString().isBlank()) {
				classObjects.add(Class.forName(className.toString().trim()));
			}
		}
	}
	
	private CMObject getMethod(String methodName) throws NoSuchMethodException, SecurityException{
		Method mObj=null;
		Class<?> cObj=null;
		classNameList.clear();
		for( int i=0;i<classObjects.size();i++) {
			classNameList.add(classObjects.get(i).getName());
			for(Method method: classObjects.get(i).getDeclaredMethods()) {
				if(methodName.equalsIgnoreCase(method.getName())) {
					mObj=method;
					cObj=classObjects.get(i);
					break;
				}
			}
		}
		if(mObj==null) {
			throw new NoSuchMethodException("No Such method named '"+methodName+"' found in "+classNameList );
		}
		return new CMObject(cObj,mObj);
		
	}
	
	public Class<?>[] inspectParameters(String methodName) throws NoSuchMethodException, SecurityException{
		return this.getMethod(methodName).getMethodObj().getParameterTypes();
	}

	/**
	 * @purpose 
	 * @date 28-Mar-2021
	 * @param action
	 * @param object
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void perform(String methodName, WebElement object) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CMObject obj=this.getMethod(methodName);
		Constructor<?> construct=obj.getClassObj().getConstructor(WebDriver.class);
		Object constObj=construct.newInstance(this.driver);
		obj.getMethodObj().invoke(constObj,object);
	}
	

	/**
	 * @purpose 
	 * @date 28-Mar-2021
	 * @param action
	 * @param object
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void perform(String methodName, String data) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CMObject obj=this.getMethod(methodName);
		Constructor<?> construct=obj.getClassObj().getConstructor(WebDriver.class);
		Object constObj=construct.newInstance(this.driver);
		obj.getMethodObj().invoke(constObj,data);
	}
	
	/**
	 * @purpose 
	 * @date 28-Mar-2021
	 * @param action
	 * @param object
	 * @param data
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void perform(String methodName, WebElement object, String data) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CMObject obj=this.getMethod(methodName);
		Constructor<?> construct=obj.getClassObj().getConstructor(WebDriver.class);
		Object constObj=construct.newInstance(this.driver);
		obj.getMethodObj().invoke(constObj,object,data);
	}
	
	

}

class CMObject{
	private Class<?> cObj;
	private Method mObj;
	
	CMObject(Class<?> cObj,Method mObj){
		this.cObj=cObj;
		this.mObj=mObj;
	}
	
	/**
	 * @return the cObj
	 */
	public Class<?> getClassObj() {
		return cObj;
	}
	/**
	 * @return the mObj
	 */
	public Method getMethodObj() {
		return mObj;
	}
}
