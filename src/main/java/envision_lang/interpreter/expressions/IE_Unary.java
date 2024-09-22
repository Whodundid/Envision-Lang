package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.OperatorOverloadHandler;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Unary;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.tokenizer.Operator;

public class IE_Unary extends AbstractInterpreterExecutor {
    
    //==============================================================================================
    
    public static final String
    
    NULL_EITHER_ERROR = "Expected a literal value or non-null variable to perform %s operation on but got '%s' instead!",
    NULL_VAR_ERROR = "Expected a non-null variable in unary expression for %s operation!",
    INVALID_TARGET_ERROR = "Invalid target '%s' for unary operation: '%s'";
    
    //==============================================================================================
    
    public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Unary expression) {
        Operator op = expression.operator;
        ParsedExpression left = expression.left;
        ParsedExpression right = expression.right;
        
        switch (op) {
        case LOGICAL_NEGATE:
        case UNARY_ADD:
        case UNARY_SUB:
        {
            // ensure left isn't null
            errorIf((left == null && right != null), NULL_EITHER_ERROR, op, left);
            // ensure that the left target resolves to an actual variable in scope
            var left_value = interpreter.evaluate(left);
            assertNotNull(left_value);
            errorIf(!(left_value instanceof ClassInstance), INVALID_TARGET_ERROR, left, op);
            
            ClassInstance leftInstance = (ClassInstance) left_value;
            
            return OperatorOverloadHandler.handleOverload(interpreter, null, op, leftInstance, null);
        }
        case PRE_INC:
        case PRE_DEC:
        {
            // ensure left isn't null
            errorIf((left == null && right != null), NULL_VAR_ERROR, "pre increment/decrement");
            // ensure that the left target resolves to an actual variable in scope
            var left_value = interpreter.evaluate(left);
            assertNotNull(left_value);
            errorIf(!(left_value instanceof ClassInstance), INVALID_TARGET_ERROR, left, "pre increment/decrement");
            
            String scopeName = ((Expr_Var) left).getName();
            ClassInstance leftInstance = (ClassInstance) left_value;
            
            // check for class level operator overloading
            return OperatorOverloadHandler.handleOverload(interpreter, scopeName, op, leftInstance, null);
        }
        case POST_INC:
        case POST_DEC:
        {
            // ensure right isn't null
            errorIf(left != null && right == null, NULL_VAR_ERROR, "post increment/decrement");
            // make sure we're actually working with a variable
            errorIf(!(right instanceof Expr_Var), INVALID_TARGET_ERROR, right, "post increment/decrement");
            // ensure that the right target resolves to an actual variable in scope
            var right_value = interpreter.evaluate(right);
            assertNotNull(right_value);
            errorIf(!(right_value instanceof ClassInstance), INVALID_TARGET_ERROR, right, "post increment/decrement");
            
            String scopeName = ((Expr_Var) right).getName();
            ClassInstance rightInstance = (ClassInstance) right_value;
            
            // check for class level operator overloading
            return OperatorOverloadHandler.handleOverload(interpreter, scopeName, op, rightInstance, null);
        }
        default:
            throw new EnvisionLangError("Invalid operator for unary expression! '" + op + "'");
        }
        
        
        
        
        
        
        
        
//        // handle left hand operator (!x or -x)
//        if (left == null) {
//            // check for class level operator overloading
//            var right_value = interpreter.evaluate(right);
//            if (right_value instanceof ClassInstance inst) {
//                return OperatorOverloadHandler.handleOverload(interpreter, null, op, inst, inst);
//            }
//        }
//        // check for post increment/decrement (x++)
//        else if (left instanceof Expr_Var var) {
//            String scopeName = var.getName();
//            EnvisionObject l = interpreter.evaluate(var);
//            
//            // check for class level operator overloading
//            if (l instanceof ClassInstance class_inst) {
//                return OperatorOverloadHandler.handleOverload(interpreter, scopeName, Operator.makePost(op), class_inst, null);
//            }
//        }
//        else if (left instanceof Expr_Compound ce) {
//            EList<ParsedExpression> expressions = ce.expressions;
//            EnvisionList l = EnvisionListClass.newList();
//            
//            for (ParsedExpression e : expressions) {
//                if (e == null) throw new EnvisionLangError("The expression is null! This really shouldn't be possible!");
//                if (!(e instanceof Expr_Var)) throw new InvalidTargetError("Expected a Variable Expression but got a " + e.getClass().getSimpleName() + "!");
//                EnvisionObject result = interpreter.evaluate(e);
//                l.add(result);
//            }
//            
//            return l;
//        }
        
//        throw new InvalidTargetError("Cannot perform the given unary operator '" + op + "' on the given objects!");
    }
    
}