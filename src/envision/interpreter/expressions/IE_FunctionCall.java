package envision.interpreter.expressions;

import envision._launch.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.UndefinedValueError;
import envision.exceptions.errors.objects.AbstractInstantiationError;
import envision.exceptions.errors.objects.UnsupportedInstantiationError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassConstruct;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.internal.EnvisionFunction;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_FunctionCall;

public class IE_FunctionCall extends ExpressionExecutor<Expr_FunctionCall> {

	private Expr_FunctionCall e;
	private String name;
	private EnvisionObject[] args;
	
	public IE_FunctionCall(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_FunctionCall e) {
		return new IE_FunctionCall(in).run(e);
	}
	
	//-------------------------------------------------------------------------

	@Override
	public EnvisionObject run(Expr_FunctionCall expression) {
		//System.out.println("IE_FUNC_CALL RUN: " + expression + " : " + expression.callee);
		EnvisionObject o = evaluate(expression.callee);
		e = expression;
		name = (e.name != null) ? e.name.lexeme : null;
		
		//determine what type of object is being called
		if (o instanceof EnvisionObject) {
			args = new EnvisionObject[e.args.size()];
			
			//parse the args
			for (int i = 0; i < args.length; i++) {
				Expression arg = e.args.get(i);
				EnvisionObject obj = evaluate(arg);
				args[i] = obj;
			}
			
			//determine the type
			if (o instanceof EnvisionCodeFile env_code) return importCall(env_code);
			//if (o instanceof FunctionPrototype env_proto) return protoCall(env_proto);
			if (o instanceof EnvisionFunction env_func) return functionCall(env_func);
			//if (o instanceof EnvisionLangPackage env_pkg)  return packageCall(env_pkg);
			if (o instanceof EnvisionClass env_class) return classCall(env_class);
			if (o instanceof ClassInstance env_inst) return instanceCall(env_inst);
			
			//if (o instanceof EnvisionObject env_obj) return objectFunctionCall(env_obj);
		}
		else if (o != null) {
			//return run(expression.setCallee(ObjectCreator.createObject(o)));
		}
		
		throw new EnvisionError("TEMP: Invalid function call expression! " + e + " : " + o);
	}
	
	//-------------------------------------------------------------------------
	
	private EnvisionObject importCall(EnvisionCodeFile code) {
		EnvisionObject env_obj = code.getValue(name);
		
		if (env_obj == null) throw new UndefinedValueError(name);
		//if (!env_obj.isPublic()) throw new NotVisibleError(env_obj);
		
		if (env_obj instanceof EnvisionClass env_class) return classCall(env_class);
		if (env_obj instanceof EnvisionFunction env_func) return functionCall(env_func);
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
	private EnvisionObject instanceCall(ClassInstance ci) {
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
	private EnvisionObject classCall(EnvisionClass c) {
		try {
			if (c.isAbstract()) throw new AbstractInstantiationError(c);
			else if (c.isInstantiable()) {
				//first check if it is actually 
				if (c.isPrimitive()) {
					//I don't like this code having to be evaluated here similar to a normal
					//assignment path. It feels like it should be calling one of the more
					//hard defined pathways instead.
					
					var value = (args.length == 1) ? args[0] : null;
					if (value instanceof EnvisionVariable env_var) value = env_var.get();
					return ObjectCreator.createObject(c.getDatatype(), value, false);
				}
				else {
					//System.out.println("\tthe instantiable object: " + c.getClass());
					//utilize construct optimization
					ClassConstruct con = c.getClassConstruct();
					if (con != null) con.call(interpreter, args);
					//otherwise default to basic creation
					else {
						c.newInstance(interpreter, args);
					}
				}
			}
			else throw new UnsupportedInstantiationError(c);
		}
		catch (ReturnValue r) {
			return r.result;	
			//return (e.next != null) ? run(e.applyNext(c)) : r.result;
		}
		
		throw new InvalidTargetError(name);
	}
	
	//handle functions
	private EnvisionObject functionCall(EnvisionFunction f) {
		return f.invoke_r(interpreter, args);
	}
	
}
