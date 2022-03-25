package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.random.RandomUtil;

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
		
		long rand = RandomUtil.getRoll(low, high);
		EnvisionInt randInt = EnvisionIntClass.newInt(rand);
		
		ret(randInt);
	}
	
}
