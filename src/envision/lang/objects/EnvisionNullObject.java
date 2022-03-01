package envision.lang.objects;

import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.NullVariableError;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

public class EnvisionNullObject extends EnvisionVariable {
	
	public EnvisionNullObject() {
		super(Primitives.NULL.toDatatype());
		hasObjectMethods = false;
	}
	
	@Override
	public EnvisionDatatype getDatatype() {
		return EnvisionDatatype.prim_null();
	}
	
	@Override
	public String toString() {
		return "ENVISION:null";
	}
	
	@Override
	public EnvisionVariable set(Object valIn) throws FinalVarReassignmentError {
		throw new NullVariableError();
	}
	
}
