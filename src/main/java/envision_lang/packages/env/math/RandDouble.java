package envision_lang.packages.env.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.random.RandomUtil;

public class RandDouble extends EnvisionFunction {
	
	public RandDouble() {
		super(Primitives.DOUBLE, "randDouble");
		//support min and max range values
		addOverload(Primitives.DOUBLE, Primitives.NUMBER, Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		double low = Double.MIN_VALUE;
		double high = Double.MAX_VALUE;
		
		if (args.length != 0) {
			low = ((EnvisionNumber) args[0]).doubleVal_i();
			high = ((EnvisionNumber) args[1]).doubleVal_i();
		}
		
		double rand = RandomUtil.getRoll(low, high);
		EnvisionDouble randDouble = EnvisionDoubleClass.newDouble(rand);
		
		ret(randDouble);
	}
	
}
