package envision_lang.packages.env.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;

public class Pow extends EnvisionFunction {
	
	public Pow() {
		super(Primitives.DOUBLE, "pow", Primitives.NUMBER, Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		double numA = ((EnvisionNumber) args[0]).doubleVal_i();
		double numB = ((EnvisionNumber) args[1]).doubleVal_i();
		double pow = Math.pow(numA, numB);
		EnvisionDouble d = EnvisionDoubleClass.valueOf(pow);
		ret(d);
	}
	
}
