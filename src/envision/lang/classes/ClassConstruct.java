package envision.lang.classes;

import java.util.Map;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.IDatatype;
import envision.lang.util.StaticTypes;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;

/**
 * ClassConstructs are intended to optimize class instance creation by
 * caching relevant static and instance based members during
 * interpretation. This process reduces instance creation time by
 * removing the need to re-interpret the class's body every time a new
 * instance is built. Ultimately, this means that instance members are
 * only ever interpreted once.
 * 
 * <p>
 * New object instances should be created using this construct
 * implementation to drastically improve instance creation
 * performance. The overall performance impact this approach will have
 * on instance creation is directly proportional to the number of
 * instances created using this construct.
 * 
 * <p>
 * Upon instance creation, a new version of each member is copied onto
 * the resulting instance scope. This method is significantly faster
 * than re-interpreting the same body statements over and over again
 * because member types are already known.
 * 
 * @author Hunter Bragg
 */
public class ClassConstruct {
	
	private final String constructName;
	
	/** The class for which instances of this construct are based on. */
	private EnvisionClass theClass;
	/** The scope for which base instance members are interpreted into. */
	private Scope internalScope;
	
	private EnvisionFunction constructor;
	private BoxList<String, EnvisionObject> fields;
	private EArrayList<EnvisionFunction> functions;
	
	/** Pulling scope map out for fast reference. */
	private Map<String, Box2<IDatatype, EnvisionObject>> internal_scope_values;
	
	//--------------
	// Constructors
	//--------------
	
	public ClassConstruct(EnvisionInterpreter interpreter, EnvisionClass c) {
		theClass = c;
		constructName = c.getTypeString();
		internalScope = new Scope(c.getClassScope());
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
		fields = internalScope.named_fields();
		functions = internalScope.functions();
	}
	
	//------------------
	// Instance Builder
	//------------------
	
	public void call(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw ReturnValue.create(buildInstance(interpreter, args));
		//throw new ReturnValue(buildInstance(interpreter, args));
	}
	
	/**
	 * Builds and returns an instance of this class.
	 * IF CALLED FROM A METHOD CALL, USE THE 'CALL' METHOD INSTEAD!
	 * 
	 * @param interpreter
	 * @param args
	 * @return
	 */
	public ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		Scope buildScope = new Scope(theClass.getClassScope());
		ClassInstance inst = new ClassInstance(theClass, buildScope);
		
		//create copies of fields
		buildScope.define("this", inst.getDatatype(), inst);
		for (int i = 0; i < fields.size(); i++) {
			Box2<String, EnvisionObject> f = fields.get(i);
			String field_name = f.getA();
			EnvisionObject the_field = f.getB();
			buildScope.define(field_name, the_field.getDatatype(), the_field.copy());
		}
		
		//define scope members
		theClass.defineScopeMembers(inst);
		
		//create copies of functions
		for (EnvisionFunction f : functions) {
			EnvisionFunction copy = f.copy().setScope(buildScope);
			//extract operators
			if (f.isOperator()) inst.addOperatorOverload(f.getOperator(), copy);
			
			//copy the method
			buildScope.define(f.getFunctionName(), StaticTypes.FUNC_TYPE, copy);
		}
		
		//init constructor
		if (constructor != null) {
			EnvisionFunction copyConstructor = constructor.copy();
			copyConstructor.setScope(buildScope);
			copyConstructor.invoke(interpreter, args);
		}
		
		return inst;
	}

	public String getName() { return constructName; }
	public EnvisionClass getStaticClass() { return theClass; }
	
}
