package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionDouble;
import envision.lang.datatypes.EnvisionDoubleClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.Primitives;

public class Pow extends EnvisionFunction {
	
	public Pow() {
		super(Primitives.DOUBLE, "pow", Primitives.NUMBER, Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		double numA = ((EnvisionNumber) args[0]).doubleVal_i();
		double numB = ((EnvisionNumber) args[1]).doubleVal_i();
		double pow = Math.pow(numA, numB);
		EnvisionDouble d = EnvisionDoubleClass.newDouble(pow);
		ret(d);
	}
	
}
