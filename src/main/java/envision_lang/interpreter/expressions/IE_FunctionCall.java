package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.InvalidTargetError;
import envision_lang.exceptions.errors.UndefinedValueError;
import envision_lang.exceptions.errors.objects.AbstractInstantiationError;
import envision_lang.exceptions.errors.objects.UnsupportedInstantiationError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassConstruct;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.EnvisionFunction;
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
		//System.out.println("IE_FUNC_CALL RUN: " + expression + " : " + expression.callee);
		EnvisionObject o = interpreter.evaluate(expression.callee);
		String name = (expression.name != null) ? expression.name.getLexeme() : null;
		EnvisionObject[] args;
		
		//determine what type of object is being called
		if (o instanceof EnvisionObject) {
			args = new EnvisionObject[expression.args.size()];
			
			//parse the args
			for (int i = 0; i < args.length; i++) {
				ParsedExpression arg = expression.args.get(i);
				EnvisionObject obj = interpreter.evaluate(arg);
				//-------------------------------------------------------
				// NOTE: The following method should only be called if
				//       an evaluated object already existing before
				//       the evaluation step in line 39. This will
				//       prevent variables that were just created from
				//       the ObjectCreator needing to be immediately
				//       created (via copying) again.
				//
				//       Not 100% sure how to implement such logic at
				//       this time though.
				//-------------------------------------------------------
				if (obj.isPassByValue()) obj = obj.copy();
				//-------------------------------------------------------
				args[i] = obj;
			}
			
			//String funcName = (name != null) ? name : String.valueOf(o);
			//String argsOut = EStringUtil.toString(args, a -> ((a != null) ? a + a.getHexHash() : ""));
			//String debugOut = " THE FUNC: " + funcName + "(" + argsOut + ")  :  " + o + " ";
			
			//System.out.println(scope());
			//DebugToolKit.debugPrintWithTitle("IE_FunctionCall Debug", debugOut);
			
			//determine the type
			if (o instanceof EnvisionCodeFile env_code) return importCall(interpreter, name, args, env_code);
			//if (o instanceof FunctionPrototype env_proto) return protoCall(env_proto);
			if (o instanceof EnvisionFunction env_func) return functionCall(interpreter, name, args, env_func);
			//if (o instanceof EnvisionLangPackage env_pkg) return packageCall(env_pkg);
			if (o instanceof EnvisionClass env_class) return classCall(interpreter, name, args, env_class);
			if (o instanceof ClassInstance env_inst) return instanceCall(interpreter, name, args, env_inst);
			
			//if (o instanceof EnvisionObject env_obj) return objectFunctionCall(env_obj);
		}
		else if (o != null) {
			//return run(expression.setCallee(ObjectCreator.createObject(o)));
		}
		
		throw new EnvisionLangError("TEMP: Invalid function call expression! " + expression + " : " + o);
	}
	
	//-------------------------------------------------------------------------
	
	private static EnvisionObject importCall(EnvisionInterpreter interpreter,
											 String name,
											 EnvisionObject[] args,
											 EnvisionCodeFile code)
	{
		EnvisionObject env_obj = code.getValue(name);
		
		if (env_obj == null) throw new UndefinedValueError(name);
		//if (!env_obj.isPublic()) throw new NotVisibleError(env_obj);
		
		if (env_obj instanceof EnvisionClass env_class) return classCall(interpreter, name, args, env_class);
		if (env_obj instanceof EnvisionFunction env_func) return functionCall(interpreter, name, args, env_func);
		//return objectFunctionCall(env_obj);
		
		return null;
	}
	
	/*
	private Object packageCall(EnvisionLangPackage p) {
		p.
		
		if (obj == null) { throw new UndefinedValueError(name); }
		if (!obj.isPublic()) { throw new NotVisibleError(obj); }
		
		if (obj instanceof EnvisionClass) { return classCall((EnvisionClass) obj); }
		if (obj instanceof EnvisionMethod) { return methodCall((EnvisionMethod) obj); }
		if (obj instanceof EnvisionObject) { return objectMethodCall(obj); }
		
		throw new InvalidTargetError(name);
	}
	*/
	
	//handle class instances
	private static EnvisionObject instanceCall(EnvisionInterpreter interpreter,
		 							    String name,
		 							    EnvisionObject[] args,
		 							    ClassInstance ci)
	{
		return ci.executeFunction(name, interpreter, args);
		/*
		EnvisionObject obj = ci.get(name);
		
		if (obj instanceof EnvisionClass env_class) return classCall(env_class);
		if (obj instanceof EnvisionFunction env_func) return functionCall(env_func);
		//if (obj instanceof EnvisionObject) return objectFunctionCall(obj);
		*/
		//throw new InvalidTargetError(name + " is not a function! Instead is: '" + obj + "'!");
	}
	
	//handle classes
	private static EnvisionObject classCall(EnvisionInterpreter interpreter,
		 							 String name,
		 							 EnvisionObject[] args,
		 							 EnvisionClass c)
	{
		try {
			if (c.isAbstract()) throw new AbstractInstantiationError(c);
			if (!c.isInstantiable()) throw new UnsupportedInstantiationError(c);
			
			ClassConstruct con = c.getClassConstruct();
			
			//utilize construct optimization if available
			if (con != null) {
				con.call(interpreter, args);
			}
			//otherwise default to basic creation
			else {
				return c.newInstance(interpreter, args);
			}
		}
		catch (ReturnValue r) {
			return r.result;	
			//return (e.next != null) ? run(e.applyNext(c)) : r.result;
		}
		
		throw new InvalidTargetError(name);
	}
	
	//handle functions
	private static EnvisionObject functionCall(EnvisionInterpreter interpreter,
		 									   String name,
		 									   EnvisionObject[] args,
		 									   EnvisionFunction f)
	{
		return f.invoke_r(interpreter, args);
	}
	
}
