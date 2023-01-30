package envision_lang.packages.env.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.random.ERandomUtil;

public class RandInt extends EnvisionFunction {
	
	public RandInt() {
		super(Primitives.INT, "randInt");
		//support min and max range
		addOverload(Primitives.INT, Primitives.NUMBER, Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		long low = Long.MIN_VALUE;
		long high = Long.MAX_VALUE;
		
		if (args.length != 0) {
			low = ((EnvisionNumber) args[0]).intVal_i();
			high = ((EnvisionNumber) args[1]).intVal_i();
		}
		
		long rand = ERandomUtil.getRoll(low, high);
		EnvisionInt randInt = EnvisionIntClass.valueOf(rand);
		
		ret(randInt);
	}
	
}
