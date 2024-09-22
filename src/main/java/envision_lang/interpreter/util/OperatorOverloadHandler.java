package envision_lang.interpreter.util;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.error_types.NullVariableError;
import envision_lang.lang.language_errors.error_types.objects.UnsupportedOverloadError;
import envision_lang.lang.natives.ParameterData;
import envision_lang.tokenizer.Operator;

public class OperatorOverloadHandler {
    
    /**
     * Performs top-level class instance operator overload handles. If the
     * base class instance natively supports the given operator overload,
     * then attempt to directly run the given operator overload function.
     * 
     * @param  executor       The interpreter performing the execution
     * @param  left_scopeName The name of the object who the given
     *                        'operator' operation is being performed on
     *                        (if left-hand side a variable)
     * @param  operator       The operation (operator) that is happening
     * @param  left_object    The class instance that is going to handle
     *                        the given 'operator' on the target
     *                        'right_object'
     * @param  right_object   The incoming target that the 'left_object' is
     *                        going to try to process using the given
     *                        'operator'
     * 
     * @return                The resultant object from the 'left_object'
     *                        handling the operation
     *                        
     * @throws Exception      Thrown if anything goes wrong
     */
    public static EnvisionObject handleOverload(EnvisionInterpreter interpreter,
                                                String left_scopeName,
                                                Operator operator,
                                                ClassInstance left_object,
                                                EnvisionObject right_object)
    {
        // error on null base objects
        if (left_object == null) throw new NullVariableError();
        
        // if object is a primitive, handle native primitive overloads
        if (left_object.isPrimitive() || left_object instanceof EnvisionList) {
            // natively support right-handed string concatenations
            if (operator == Operator.ADD && right_object instanceof EnvisionString) {
                return EnvisionStringClass.concatenate(interpreter, left_object, right_object);
            }
            // otherwise, allow the primitive class to try and find a valid Operator:Object handle
            else {
                return left_object.handleOperatorOverloads(interpreter, left_scopeName, operator, right_object);
            }
        }
        // otherwise, attempt to find a user-defined operator overload function on a user-defined class
        // if the defined object directly supports the given operator, grab the operator function and execute it
        else if (left_object.supportsOperator(operator)) {
            EnvisionFunction op_func = getOperatorFunc(left_object, operator, right_object);
            //if the operator function is null -- skip this and jump to throwing error
            if (op_func != null) return op_func.invoke_r(interpreter, right_object);
        }
        // natively support '=='
        else if (operator == Operator.EQUALS) return interpreter.isEqual(left_object, right_object);
        // natively support '!='
        else if (operator == Operator.NOT_EQUALS) return interpreter.isEqual(left_object, right_object).negate();
        
        // otherwise, throw error
        String errorMsg = (right_object != null) ? right_object.getDatatype() + ":" + right_object : "";
        throw new UnsupportedOverloadError(left_object, operator, errorMsg);
    }
    
    //==================
    // Internal Methods
    //==================
    
    private static EnvisionFunction getOperatorFunc(ClassInstance c, Operator op, EnvisionObject b) {
        // first check if the class even has support for the given operator
        EnvisionFunction op_func = c.getOperator(op);
        if (op_func == null) return null;
        
        ParameterData params = ParameterData.from(b);
        EnvisionFunction theOverload = null;
        
        // check if the overload supports the given target parameter
        if (op_func.getParams().compare(params)) {
            theOverload = op_func;
        }
        // otherwise check if any of the overload's overloads support the parameter
        else {
            EnvisionFunction funcOverload = op_func.getOverload(params);
            if (funcOverload != null) theOverload = funcOverload;
        }
        
        // return the overload, even if null
        return theOverload;
    }
    
}
