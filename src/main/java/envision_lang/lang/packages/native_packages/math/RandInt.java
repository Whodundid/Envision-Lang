package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.random.ERandomUtil;

public class RandInt extends EnvisionFunction {
	
	public RandInt() {
		super(Primitives.INT, "randInt");
		//support min and max range
		addOverload(Primitives.INT, Primitives.INT, Primitives.INT);
		//support producing 'n' number of random ints
		addOverload(Primitives.INT, Primitives.INT);
		//support producing 'n' number of random ints with min and max range
		addOverload(Primitives.INT, Primitives.INT, Primitives.INT, Primitives.INT);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		long low = Long.MIN_VALUE;
		long high = Long.MAX_VALUE;
		
		// produce 1 random int within [MIN_VALUE, MAX_VALUE]
		if (args.length == 0) {
			long rand = ERandomUtil.getRoll(low, high);
			ret(EnvisionIntClass.valueOf(rand));
		}
		// produce 'n' number of random ints each within [MIN_VALUE, MAX_VALUE]
		else if (args.length == 1) {
			int n = (int) ((EnvisionInt) args[0]).int_val;
			
			EnvisionList ints = EnvisionListClass.newList(n);
			for (int i = 0; i < n; i++) {
				long rand = ERandomUtil.getRoll(low, high);
				ints.add(EnvisionIntClass.valueOf(rand));
			}
			ret(ints);
		}
		// produce 1 random int within the specified [low, high] range
		else if (args.length == 2) {
			low = ((EnvisionInt) args[0]).int_val;
			high = ((EnvisionInt) args[1]).int_val;
			
			long rand = ERandomUtil.getRoll(low, high);
			ret(EnvisionIntClass.valueOf(rand));
		}
		// produce 'n' number of random ints each within [low, high]
		else if (args.length == 3) {
			int n = (int) ((EnvisionInt) args[0]).int_val;
			
			low = ((EnvisionInt) args[1]).int_val;
			high = ((EnvisionInt) args[2]).int_val;
			
			EnvisionList ints = EnvisionListClass.newList(n);
			for (int i = 0; i < n; i++) {
				long rand = ERandomUtil.getRoll(low, high);
				ints.add(EnvisionIntClass.valueOf(rand));
			}
			ret(ints);
		}
		
		
		throw new EnvisionLangError("Impossible function state!");
	}
	
}
