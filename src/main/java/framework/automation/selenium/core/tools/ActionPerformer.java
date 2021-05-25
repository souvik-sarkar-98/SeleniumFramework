package framework.automation.selenium.core.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.automation.selenium.core.actions.GenericTestActionLibrary;
import framework.automation.selenium.core.config.PropertyCache;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public class ActionPerformer {
	
    private final Logger logger = LogManager.getLogger(this.getClass());
	protected List<Class<?>> classObjects = new ArrayList<Class<?>>();
	protected final WebDriver driver;
	protected List<String> classNameList=new ArrayList<String>();
	/**
	 * @throws ClassNotFoundException 
	 * 
	 */
	public ActionPerformer(final WebDriver driver) throws ClassNotFoundException {
		logger.traceEntry(" with driver {}",driver.toString());
		this.driver = driver;
		logger.debug("Added Internal action class "+GenericTestActionLibrary.class.getName());
		classObjects.add(GenericTestActionLibrary.class);
		
		Object[] all = PropertyCache.getProperties("ExternalActionClasses");
		for (Object className : all) {
			if(!className.toString().isBlank()) {
				classObjects.add(Class.forName(className.toString().trim()));
				logger.debug("Added External action class "+className);
			}
		}
		logger.traceExit();
	}
	
	private CMObject getMethod(String methodName) throws NoSuchMethodException, SecurityException{
		logger.traceEntry(" with {}",methodName);
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
			NoSuchMethodException e= new NoSuchMethodException("No Such method named '"+methodName+"' found in "+classNameList );
			logger.error(e);
			throw e;
		}
		CMObject o= new CMObject(cObj,mObj);
		logger.traceExit("returning {}",o);
		return o;
		
	}
	
	public Class<?>[] inspectParameters(String methodName) throws NoSuchMethodException, SecurityException{
		logger.traceEntry(" with {}",methodName);
		Class<?>[] ob= this.getMethod(methodName).getMethodObj().getParameterTypes();
		logger.traceExit("returning {}",ob);
		return ob;
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
		logger.traceEntry(" with {},{}",methodName,object.toString());
		CMObject obj=this.getMethod(methodName);
		Constructor<?> construct=obj.getClassObj().getConstructor(WebDriver.class);
		Object constObj=construct.newInstance(this.driver);
		obj.getMethodObj().invoke(constObj,object);
		logger.traceExit();
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
		logger.traceEntry(" with {},{}",methodName,data);
		CMObject obj=this.getMethod(methodName);
		Constructor<?> construct=obj.getClassObj().getConstructor(WebDriver.class);
		Object constObj=construct.newInstance(this.driver);
		obj.getMethodObj().invoke(constObj,data);
		logger.traceExit();
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
		logger.traceEntry(" with {},{},{}",methodName,object,data);
		CMObject obj=this.getMethod(methodName);
		Constructor<?> construct=obj.getClassObj().getConstructor(WebDriver.class);
		Object constObj=construct.newInstance(this.driver);
		obj.getMethodObj().invoke(constObj,object,data);
		logger.traceExit();
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
