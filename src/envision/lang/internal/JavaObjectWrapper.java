package envision.lang.internal;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.VisibilityType;

/**
 * An EnvisionObject intended to bridge the gap between Java and
 * Envision:Java. A JavaObjectWrapper, as the name implies, is used to
 * wrap the entirety of a Java Object in a format that is referencable
 * within the Envision:Java Scripting Language.
 * 
 * <p>
 * JavaObjectWrappers should be treated as inherently internal meaning
 * that while technically referencable, they are ultimately intended
 * to be used for specialized Envision objects which directly make use
 * of Java objects. For instance, the EnvisionFile datatype uses a
 * JavaObjectWrapper to wrap a Java:File object to perform all
 * filesystem operations. Furthermore, the wrapped Java:File is placed
 * onto the EnvisionFile's instance scope as a RESTRICTED variable to
 * ensure the validity this relationship.
 * 
 * @see VisibilityType.RESTRICTED
 * 
 * @author Hunter Bragg
 */
public class JavaObjectWrapper extends EnvisionObject {
	
	//--------
	// Fields
	//--------
	
	/**
	 * Wrapped with an underlying Java object for special cross language purposes.
	 * <p>
	 * For instance, File handling in Envision makes direct use of Java's 'File' object.
	 * The Java 'File' object is then wrapped directly into an EnvisionObject so that
	 * it can be used normally within the Envision:Java language.
	 */
	public final Object javaObject;
	
	//--------------
	// Constructors
	//--------------
	
	public JavaObjectWrapper(Object javaObjectIn) {
		super(EnvisionDatatype.VAR_TYPE);
		javaObject = javaObjectIn;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Special getter used to grab a wrapped Object in Java into Envision.
	 */
	public Object getJavaObject() {
		return javaObject;
	}
	
}
