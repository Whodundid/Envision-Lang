package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class Abs extends EnvisionFunction {
    
    public Abs() {
        super(EnvisionStaticTypes.NUMBER_TYPE, "abs", EnvisionStaticTypes.NUMBER_TYPE);
    }
    
    @Override
    public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        EnvisionNumber<?> num = (EnvisionNumber<?>) args[0];
        double abs = Math.abs(num.doubleVal_i());
        
        if (num.getDatatype() == EnvisionStaticTypes.INT_TYPE) {
            ret(EnvisionIntClass.valueOf((long) abs));
        }
        else if (num.getDatatype() == EnvisionStaticTypes.DOUBLE_TYPE) {
            ret(EnvisionDoubleClass.valueOf(abs));
        }
    }
    
}
