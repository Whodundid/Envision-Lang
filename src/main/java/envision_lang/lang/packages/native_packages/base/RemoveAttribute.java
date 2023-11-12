package envision_lang.lang.packages.native_packages.base;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionVoid;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.java.NativeField;
import envision_lang.lang.java.NativeFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.RestrictedAccessError;

/**
 * Remove the given attribute from the given object by name.
 * The old attribute (if there was one) will be returned.
 */
public class RemoveAttribute extends EnvisionFunction {
	
	public RemoveAttribute() {
		super(VAR, "removeAttribute", VAR, STRING);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
	    EnvisionObject obj = args[0];
	    EnvisionString attributeName = (EnvisionString) args[1];
		
		String name = attributeName.string_val;
		boolean exists = false;
		IScope scope = null;
		
		// figure out what we are looking at and grab the scope out of it
		if (obj instanceof EnvisionCodeFile cf) {
		    exists = (scope = cf.scope()).exists(name);
		}
		else if (obj instanceof EnvisionClass c) {
		    exists = (scope = c.getClassScope()).exists(name);
		}
		else if (obj instanceof ClassInstance ci) {
		    exists = (scope = ci.getScope()).exists(name);
		}
		
		// if it already exists, don't redefine over the existing value
		// if there wasn't a scope on the given object, then we can't modify anything to begin with
		if (!exists || scope == null) {
		    ret(EnvisionVoid.VOID);
		}
		
		// check if the attribute that is to be remove can even be removed (could be restricted/native)
		var theObj = scope.get(name);
		if (theObj == null) {
		    throw new EnvisionLangError("Removing attribute '" + name + "' somehow produced a null value!");
		}
		
		// native fields and functions should NEVER be modifiable
		if (theObj instanceof NativeField) {
		    throw new RestrictedAccessError(theObj);
		}
		else if (theObj instanceof NativeFunction) {
		    throw new RestrictedAccessError(theObj);
		}
		// if the object itself is just quite literally restricted (native File object)
		// don't allow any modifications to the original object class
		else if (theObj.isRestricted()) {
		    throw new RestrictedAccessError(theObj);
		}
		
		// actually remove the attribute from the object's scope
		ScopeEntry existingEntry = scope.values().remove(name);
		if (existingEntry == null) {
		    throw new EnvisionLangError("Removing attribute '" + name + "' somehow produced a null value!");
		}
		
		ret(existingEntry.getObject());
	}
	
}
