package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionTupleClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.error_types.InvalidTargetError;
import envision_lang.lang.natives.ParameterData;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class IE_Lambda extends AbstractInterpreterExecutor {
    
    public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Lambda e) {
        Expr_Compound inputs = e.inputs;
        Expr_Compound targets = e.production;
        
        if (targets.isEmpty()) throw new InvalidTargetError("Lambda Error: No production expression defined!");
        //if (!production.hasOne()) throw new InvalidTargetError("A lambda expression can only define one production!");
        
        EnvisionObject rVal = EnvisionNull.NULL;
        
        EList<String> inputNames = inputs.expressions.map(p -> p.toString());
        EList<EnvisionObject> processedTargets = processAllTargets(interpreter, targets);
        // determine the inputs based on the target
        EList<EnvisionObject[]> processedInputs = processAllInputs(interpreter, inputs, processedTargets);
        
        rVal = executeTargets(interpreter, inputNames, processedInputs, processedTargets);
        
        return rVal;
    }
    
    //==================
    // Internal Methods
    //==================
    
    private static EnvisionObject executeTargets(EnvisionInterpreter interpreter,
                                                 EList<String> inputNames,
                                                 EList<EnvisionObject[]> inputs,
                                                 EList<EnvisionObject> targets)
    {
        if (inputs.hasOne() && targets.hasOne()) {
            return executeForSingle(interpreter, inputNames, inputs, targets);
        }
        return executeForMultiple(interpreter, inputNames, inputs, targets);
    }
    
    //-----------------------------------------------------------------------------------------
    
    private static EnvisionObject executeForSingle(EnvisionInterpreter interpreter,
                                                   EList<String> inputNames,
                                                   EList<EnvisionObject[]> inputs,
                                                   EList<EnvisionObject> targets)
    {
        EnvisionObject rVal = EnvisionNull.NULL;
        
        if (inputs.size() > 1) throw new IllegalStateException("Lambda expected no more than 1 input!");
        if (targets.size() > 1) throw new IllegalStateException("Lambda expected no more than 1 target!");
        
        String inputName = inputNames.get(0);
        EnvisionObject[] args = inputs.get(0);
        EnvisionObject target = targets.get(0);
        
        rVal = executeTarget(interpreter, inputName, args, target);
        
        return rVal;
    }
    
    //-----------------------------------------------------------------------------------------
    
    private static EnvisionTuple executeForMultiple(EnvisionInterpreter interpreter,
                                                    EList<String> inputNames,
                                                    EList<EnvisionObject[]> inputs,
                                                    EList<EnvisionObject> targets)
    {
        EnvisionTuple tuple = EnvisionTupleClass.newTuple();
        
        for (int i = 0; i < inputs.size(); i++) {
            for (var target : targets) {
                final var name = inputNames.get(i);
                final var args = inputs.get(i);
                EnvisionObject result = executeTarget(interpreter, name, args, target);
                tuple.add(result);
            }
        }
        
        return tuple;
    }
    
    //-----------------------------------------------------------------------------------------
    
    private static EnvisionObject executeTarget(EnvisionInterpreter interpreter,
                                                String inputName,
                                                EnvisionObject[] inputs,
                                                EnvisionObject target)
    {
        EnvisionObject result = EnvisionNull.NULL;
        
        try {
            if (target instanceof EnvisionFunction f) {
                result = IE_FunctionCall.functionCall(interpreter, f, inputs);
            }
            else if (target instanceof EnvisionClass c) {
                result = IE_FunctionCall.classCall(interpreter, null, c, inputs);
            }
            // inject inputs into lambda string target
            else if (target instanceof EnvisionString s) {
                String formatted = EnvisionStringFormatter.injectLambdaInputs(interpreter, inputName, inputs, s);
                result = EnvisionStringClass.valueOf(formatted);
            }
            // for all other cases, simply return the target itself
            else if (target != null) {
                result = target;
            }
        }
        catch (ReturnValue r) {
            result = r.result;
        }
        
        return result;
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    private static void invalidTargetCheck(Object target, Object check) {
        if (target != check) return;
        String targetString;
        if (target == null) targetString = "Java::NULL";
        else targetString = String.valueOf(target);
        throw new InvalidTargetError(targetString + " is not a valid lambda target!");
    }
    
    private static EList<EnvisionObject> processAllTargets(EnvisionInterpreter interpreter, Expr_Compound targets) {
        EList<EnvisionObject> r = new EArrayList<>(targets.size());
        for (var expr : targets.expressions) {
            r.add(processTarget(interpreter, expr));
        }
        return r;
    }
    
    private static EList<EnvisionObject[]> processAllInputs(EnvisionInterpreter interpreter,
                                                            Expr_Compound inputs,
                                                            EList<EnvisionObject> targets)
    {
        EList<EnvisionObject> processedInputs = new EArrayList<>(inputs.size());
        for (var expr : inputs.expressions) {
            processedInputs.add(processInput(interpreter, expr));
        }
        
        EList<EnvisionObject[]> r = new EArrayList<>(processedInputs.size());
        for (var target : targets) {
            boolean expand = true;
            
            if (target instanceof EnvisionFunction f) {
                var inputParams = ParameterData.fromObjects(processedInputs);
                expand = !f.hasOverload(inputParams);
            }
            else if (target instanceof EnvisionClass c) {
                var con = c.getConstructor();
                if (con == null) expand = true;
                else {
                    var inputParams = ParameterData.fromObjects(processedInputs);
                    expand = !con.hasOverload(inputParams);
                }
            }
            else {
                // I have no idea :shrug:
            }
            
            if (expand) {
                for (var i : processedInputs) {
                    r.add(expandArgs(i));
                }
            }
            else {
                r.add(processedInputs.toArray(new EnvisionObject[0]));
            }
            
        }
        return r;
    }
    
    private static EnvisionObject processTarget(EnvisionInterpreter interpreter, ParsedExpression target) {
        EnvisionObject result = interpreter.evaluate(target);
        return result;
    }
    
    private static EnvisionObject processInput(EnvisionInterpreter interpreter, ParsedExpression input) {
        return interpreter.evaluate(input);
    }
    
    private static EnvisionObject[] expandArgs(EnvisionObject result) {
        if (result instanceof EnvisionList l) return l.toArray();
        else if (result instanceof EnvisionTuple t) return t.toArray();
        else return new EnvisionObject[] { result };
    }
    
}
