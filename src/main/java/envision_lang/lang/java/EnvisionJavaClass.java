package envision_lang.lang.java;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;

public class EnvisionJavaClass extends EnvisionClass {
	
	private IScope instanceScope;
	
	public EnvisionJavaClass(String classNameIn) {
		super(classNameIn);
	}
	
	/**
	 * Default class construction procedure. This path will only be taken if a
	 * ClassConstruct is not present.
	 * 
	 * @param interpreter The active working interpreter
	 * @param args Any arguments to be passed to the new object instance
	 * @return The newly created object instance
	 */
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		IScope buildScope = instanceScope.copy();
		ClassInstance instance = new ClassInstance(this, buildScope);
		
		//define 'this'
		buildScope.define("this", instance);
		
		//build the body of the new instance
		interpreter.executeBlock(bodyStatements, buildScope);
		
		//extract operator overloads from scope
		//set the overloaded operators onto the class instance
		buildScope.operators().forEach(op -> instance.addOperatorOverload(op.getOperator(), op));
		
		//execute constructor -- if there is one
		if (constructor != null) {
			constructor.setScope(buildScope);
			constructor.invoke_i(interpreter, args);
		}
		
		//define scope members
		defineScopeMembers(instance);
		
		return instance;
	}
	
	public void setNativeJavaScope(IScope scope) {
		instanceScope = scope;
	}
	
}
