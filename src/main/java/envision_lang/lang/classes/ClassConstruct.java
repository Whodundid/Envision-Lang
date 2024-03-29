package envision_lang.lang.classes;

import java.util.Map;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;

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
	private final EnvisionClass theClass;
	/** The scope for which base instance members are interpreted into. */
	private final IScope internalScope;
	
	private final EnvisionFunction constructor;
	private BoxList<String, ScopeEntry> fields;
	private EList<EnvisionFunction> functions;
	
	/** Pulling scope map out for fast reference. */
	private final Map<String, ScopeEntry> internal_scope_values;
	
	//--------------
	// Constructors
	//--------------
	
	public ClassConstruct(EnvisionInterpreter interpreter, EnvisionClass c) {
		theClass = c;
		constructName = c.getTypeString();
		internalScope = new Scope(c.getClassScope());
		internal_scope_values = internalScope.values();
		constructor = c.getConstructor();
		
		buildInstanceScope(interpreter);
	}
	
	/** 
	 * Upon creation of the construct, a cached instance scope will be created.
	 * This scope will be given to all created instances.
	 */
	private void buildInstanceScope(EnvisionInterpreter interpreter) {
		interpreter.executeStatements(theClass.getBody(), internalScope);
		
		//extract members from scope
		fields = internalScope.named_fields();
		functions = internalScope.functions();
	}
	
	//------------------
	// Instance Builder
	//------------------
	
	public void call(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw ReturnValue.wrap(buildInstance(interpreter, args));
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
			var box = fields.get(i);
			String field_name = box.getA();
			ScopeEntry the_entry = box.getB();
			buildScope.define(field_name, the_entry.deepCopy());
		}
		
		//define scope members
		theClass.defineScopeMembers(inst);
		
		//create copies of functions
		for (EnvisionFunction f : functions) {
			EnvisionFunction copy = f.copy();
			copy.setScope(buildScope);
			//extract operators
			if (f.isOperator()) inst.addOperatorOverload(f.getOperator(), copy);
			
			//copy the method
			buildScope.define(f.getFunctionName(), EnvisionStaticTypes.FUNC_TYPE, copy);
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
