package envision.interpreter.util.optimizations;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

import java.util.Map;

/**
 * ClassConstructs are intended to optimize class interpreting by caching
 * relavant static and instance based members for new instances.
 * 
 * New class instances will be created from this construct implementation
 * so that common class members do not need to be reconstructed everytime.
 * 
 * This process reduces instance creation time by removing the need to re-interpret
 * the class's body each time a new instance is built. Therefore, instance members
 * are only ever interpreted once.
 * 
 * Upon instance creation, a new version of each member is copied onto the resulting
 * instance scope. This method is significantly faster than re-interpreting the same
 * body statements over and over again because member types are already known.
 * 
 * @author Hunter
 */
public class ClassConstruct {
	
	private final String constructName;
	
	/** The class for which instances of this construct are based on. */
	private EnvisionClass theClass;
	/** The scope for which base instance members are interpreted into. */
	private Scope internalScope;
	
	private EnvisionFunction constructor;
	private EArrayList<EnvisionObject> fields;
	private EArrayList<EnvisionFunction> methods;
	
	/** Pulling scope map out for fast reference. */
	private Map<String, Box2<EnvisionDatatype, EnvisionObject>> internal_scope_values;
	
	//--------------
	// Constructors
	//--------------
	
	public ClassConstruct(EnvisionInterpreter interpreter, EnvisionClass c) {
		theClass = c;
		constructName = c.getTypeString();
		internalScope = new Scope(c.getScope());
		internal_scope_values = internalScope.values;
		constructor = c.getConstructor();
		
		buildInstanceScope(interpreter);
	}
	
	/** 
	 * Upon creation of the construct, a cached instance scope will be created.
	 * This scope will be given to all created instances.
	 */
	private void buildInstanceScope(EnvisionInterpreter interpreter) {
		interpreter.executeBlock(theClass.getBody(), internalScope);
		
		//extract members from scope
		fields = internalScope.fields();
		methods = internalScope.methods();
	}
	
	//------------------
	// Instance Builder
	//------------------
	
	public void call(EnvisionInterpreter interpreter, Object[] args) {	
		throw new ReturnValue(buildInstance(interpreter, args));
	}
	
	/**
	 * Builds and returns an instnace of this class.
	 * IF CALLED FROM A METHOD CALL, USE THE 'CALL' METHOD INSTEAD!
	 * 
	 * @param interpreter
	 * @param args
	 * @return
	 */
	public ClassInstance buildInstance(EnvisionInterpreter interpreter, Object[] args) {
		Scope buildScope = new Scope(theClass.getScope());
		ClassInstance inst = new ClassInstance(theClass, buildScope);
		
		//create copies of fields
		buildScope.define("this", inst);
		for (EnvisionObject f : fields) {
			buildScope.define(f.getName(), f.getDatatype(), f.copy());
		}
		
		//create copies of methods
		for (EnvisionFunction m : methods) {
			EnvisionFunction copy = m.copy().setScope(buildScope);
			
			//extract operators
			if (m.isOperator()) inst.addOperator(m.getOperator(), copy);
			
			//copy the method
			buildScope.define(m.getName(), Primitives.FUNCTION, copy);
		}
		
		//init constructor
		if (constructor != null) {
			EnvisionFunction copyConstructor = constructor.copy();
			copyConstructor.setScope(buildScope);
			copyConstructor.invoke(interpreter, args);
		}
		
		System.out.println("construct: " + inst.getDatatype());
		
		return inst;
	}

	public String getName() { return constructName; }
	public EnvisionClass getStaticClass() { return theClass; }
	
}
