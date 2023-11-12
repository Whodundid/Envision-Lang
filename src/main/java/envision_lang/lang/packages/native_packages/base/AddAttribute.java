package envision_lang.lang.packages.native_packages.base;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;

/**
 * Returns true if the given class instance supports the given operator overload.
 * Takes in an instance of an EnvisionClass and an operator.
 */
public class AddAttribute extends EnvisionFunction {
	
	public AddAttribute() {
		super(BOOLEAN, "addAttribute", VAR, STRING, VAR);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
	    EnvisionObject obj = args[0];
	    EnvisionString attributeName = (EnvisionString) args[1];
	    EnvisionObject attributeToAdd = args[2];
		
	    if (obj == null) {
	        throw new EnvisionLangError("Cannot add an attribute to a JAVA:NULL object!");
	    }
	    if (attributeToAdd == null) {
	        throw new EnvisionLangError("Cannot add the given attribute to '" + obj + "' because the attribute is JAVA::NULL!");
	    }
	    
		boolean exists = false;
		IScope scope = null;
		
		if (obj instanceof EnvisionCodeFile cf) {
		    exists = (scope = cf.scope()).exists(attributeName.string_val);
		}
		else if (obj instanceof EnvisionClass c) {
		    exists = (scope = c.getClassScope()).exists(attributeName.string_val);
		}
		else if (obj instanceof ClassInstance ci) {
		    exists = (scope = ci.getScope()).exists(attributeName.string_val);
		}
		
		// if it already exists, don't redefine over the existing value
		if (exists || scope == null) {
		    retBool(false);
		}
		
		scope.define(attributeName.string_val, attributeToAdd);
		
		retBool(true);
	}
	
}
