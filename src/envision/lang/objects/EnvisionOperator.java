package envision.lang.objects;

import envision.exceptions.errors.classErrors.UndefinedConstructorError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.Primitives;

public class EnvisionOperator extends EnvisionVariable {
	
	public EnvisionOperator(String nameIn) {
		super(Primitives.OPERATOR.toDatatype(), nameIn);
	}
	
	//public EnvisionOperator(String nameIn) { this(nameIn, false); }
	//public EnvisionOperator(String nameIn, boolean val) { super(new EnvisionDatatype(PrimitiveDatatypes.OPERATOR), nameIn, val); }
	//public EnvisionOperator(Keyword opIn) { this("noname", opIn); }
	//public EnvisionOperator(String nameIn, Keyword opIn) { super(PrimitiveDatatypes.OPERATOR, nameIn, Operator.of(opIn)); }
	//public EnvisionOperator(EnvisionOperator in) { this((Operator) in.get()); }
	//public EnvisionOperator(Operator in) { super(PrimitiveDatatypes.OPERATOR, "noname", in); }
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { throw new UndefinedConstructorError("Operator's must define a single, specific operator value."); }
		else if (args.length == 1) {
			//Object obj = args[0];
			//if (obj instanceof Boolean) { return new EnvisionOperator((Boolean) obj); }
			//if (obj instanceof EnvisionOperator) { return new EnvisionOperator((EnvisionOperator) obj); }
			//if (obj instanceof Integer) { return new EnvisionOperator(((Integer) obj).intValue() == 1); }
			//if (obj instanceof EnvisionInt) { return new EnvisionOperator((EnvisionInt) obj); }
		}
		return null;
	}
	
	//public static EnvisionOperator of(Operator val) { return new EnvisionOperator(val); }
	//public static EnvisionOperator of(String val) { return new EnvisionOperator(Operator.of(val)); }
	
}
