package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;

/**
 * An EnvisionObject intended to bridge the gap between Java and
 * Envision:Java. A JavaObjectWrapper, as the name implies, is used to
 * wrap the entirety of a Java Object in a format that is
 * reference-able within the Envision:Java Scripting Language.
 * 
 * <p>
 * JavaObjectWrappers should be treated as inherently internal meaning
 * that while technically reference-able, they are ultimately intended
 * to be used for specialized Envision objects which directly make use
 * of Java objects. For instance, the EnvisionFile datatype uses a
 * JavaObjectWrapper to wrap a Java:File object to perform all file
 * system operations. Furthermore, the wrapped Java:File is placed
 * onto the EnvisionFile's instance scope as a RESTRICTED variable to
 * ensure the validity this relationship.
 * 
 * @see EnvisionVis.RESTRICTED
 * 
 * @author Hunter Bragg
 */
public class InternalJavaObjectWrapper extends EnvisionObject {
	
	//--------
	// Fields
	//--------
	
	/**
	 * The wrapped underlying Java object for special cross-language purposes.
	 * <p>
	 * For instance, File handling in Envision makes direct use of Java's 'File' object.
	 * The Java 'File' object is then wrapped directly into an EnvisionObject so that
	 * it can be used normally within the Envision:Java Scripting Language.
	 */
	public final Object javaObject;
	
	//--------------
	// Constructors
	//--------------
	
	public InternalJavaObjectWrapper(Object javaObjectIn) {
		super(EnvisionStaticTypes.VAR_TYPE);
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
