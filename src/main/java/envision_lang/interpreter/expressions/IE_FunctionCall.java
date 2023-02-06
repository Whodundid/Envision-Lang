package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassConstruct;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.InvalidTargetError;
import envision_lang.lang.exceptions.errors.UndefinedValueError;
import envision_lang.lang.exceptions.errors.objects.AbstractInstantiationError;
import envision_lang.lang.exceptions.errors.objects.UnsupportedInstantiationError;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_FunctionCall;
import eutil.debug.InDevelopment;
import eutil.debug.Inefficient;

/**
 * Performs function calls on valid function expression targets.
 * 
 * @author Hunter Bragg
 */
public class IE_FunctionCall extends AbstractInterpreterExecutor {
	
	@Inefficient(reason="Pass-by-value --> 'obj.copy' is very slow!")
	@InDevelopment("Copying should only occur on already existing objects -- Not ones that were potentially just created!")
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_FunctionCall expression) {
		EnvisionObject o = interpreter.evaluate(expression.callee);
		String name = (expression.name != null) ? expression.name.getLexeme() : null;
		EnvisionObject[] args;
		
		// determine what type of object is being called
		if (o instanceof EnvisionObject) {
			args = new EnvisionObject[expression.args.size()];
			
			// parse the args
			for (int i = 0; i < args.length; i++) {
				ParsedExpression arg = expression.args.get(i);
				EnvisionObject obj = interpreter.evaluate(arg);
				//-------------------------------------------------------
				if (obj.isPassByValue()) obj = obj.copy();
				//-------------------------------------------------------
				args[i] = obj;
			}
			
			// determine the type
			if (o instanceof EnvisionCodeFile env_code) return importCall(interpreter, name, args, env_code);
			if (o instanceof EnvisionFunction env_func) return functionCall(interpreter, name, args, env_func);
			if (o instanceof EnvisionClass env_class) return classCall(interpreter, name, args, env_class);
			if (o instanceof ClassInstance env_inst) return instanceCall(interpreter, name, args, env_inst);
		}
		
		throw new EnvisionLangError("Invalid function call expression! " + expression + " : " + o);
	}
	
	//=================================================================================================================
	
	private static EnvisionObject importCall(EnvisionInterpreter interpreter,
											 String name,
											 EnvisionObject[] args,
											 EnvisionCodeFile code)
	{
		EnvisionObject env_obj = code.getValue(name);
		
		if (env_obj == null) throw new UndefinedValueError(name);
		if (env_obj instanceof EnvisionClass env_class) return classCall(interpreter, name, args, env_class);
		if (env_obj instanceof EnvisionFunction env_func) return functionCall(interpreter, name, args, env_func);
		
		return null;
	}
	
	//=================================================================================================================
	
	// handle class instances
	private static EnvisionObject instanceCall(EnvisionInterpreter interpreter,
		 							    	   String name,
		 							    	   EnvisionObject[] args,
		 							    	   ClassInstance ci)
	{
		return ci.executeFunction(name, interpreter, args);
	}
	
	//=================================================================================================================
	
	// handle classes
	private static EnvisionObject classCall(EnvisionInterpreter interpreter,
		 							 		String name,
		 							 		EnvisionObject[] args,
		 							 		EnvisionClass c)
	{
		try {
			if (c.isAbstract()) throw new AbstractInstantiationError(c);
			if (!c.isInstantiable()) throw new UnsupportedInstantiationError(c);
			
			ClassConstruct con = c.getClassConstruct();
			
			// utilize construct optimization if available
			if (con != null) {
				con.call(interpreter, args);
			}
			// otherwise default to basic creation
			else {
				return c.newInstance(interpreter, args);
			}
		}
		catch (ReturnValue r) {
			return r.result;	
		}
		
		throw new InvalidTargetError(name);
	}
	
	//=================================================================================================================
	
	// handle standard function calls
	private static EnvisionObject functionCall(EnvisionInterpreter interpreter,
		 									   String name,
		 									   EnvisionObject[] args,
		 									   EnvisionFunction f)
	{
		return f.invoke_r(interpreter, args);
	}
	
}
