package envision.interpreter.expressions;

import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.NotVisibleError;
import envision.exceptions.errors.UndefinedValueError;
import envision.exceptions.errors.VoidFunctionError;
import envision.exceptions.errors.objects.AbstractInstantiationError;
import envision.exceptions.errors.objects.UnsupportedInstantiationError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.CastingUtil;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionFunction;
import envision.lang.objects.EnvisionVoidObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InternalFunction;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_FunctionCall;

public class IE_FunctionCall extends ExpressionExecutor<Expr_FunctionCall> {

	private Expr_FunctionCall e;
	private String name;
	private Object[] args;
	
	public IE_FunctionCall(EnvisionInterpreter in) {
		super(in);
	}
	
	public static Object run(EnvisionInterpreter in, Expr_FunctionCall e) {
		return new IE_FunctionCall(in).run(e);
	}
	
	//-------------------------------------------------------------------------

	@Override
	public Object run(Expr_FunctionCall expression) {
		//System.out.println("IE_FUNCCALL RUN: " + expression + " : " + expression.callee);
		Object o = (expression.callee instanceof Expression expr) ? evaluate(expr) : expression.callee;
		e = expression;
		name = (e.name != null) ? e.name.lexeme : null;
		
		//System.out.println("IE_METHODCALL: THE OBJECT: " + o);
		//if (o != null) System.out.println("\tThe object being called: " + o.getClass());
		
		if (o != null && !(o instanceof EnvisionObject)) {
			EnvisionDatatype o_type = EnvisionDatatype.dynamicallyDetermineType(o);
			o = ObjectCreator.createObject(EnvisionObject.DEFAULT_NAME, o_type, o, false);
		}
		
		//determine what type of object is being called
		if (o instanceof EnvisionObject) {
			args = new Object[e.args.size()];
			
			//parse the args
			for (int i = 0; i < args.length; i++) {
				var arg = e.args.get(i);
				//System.out.println(arg + " : " + arg.getClass());
				args[i] = evaluate(arg);
			}
			
			//determine the type
			if (o instanceof EnvisionCodeFile env_code) return importCall(env_code);
			//if (o instanceof EnvisionLangPackage env_pkg)  return packageCall(env_pkg);
			if (o instanceof ClassInstance env_inst) return instanceCall(env_inst);
			if (o instanceof EnvisionClass env_class) return classCall(env_class);
			if (o instanceof EnvisionFunction env_func) return functionCall(env_func);
			if (o instanceof EnvisionObject env_obj) return objectFunctionCall(env_obj);
		}
		else if (o != null) {
			//return run(expression.setCallee(ObjectCreator.createObject(o)));
		}
		
		throw new EnvisionError("TEMP: Invalid method call expression! " + e + " : " + o);
	}
	
	//-------------------------------------------------------------------------
	
	private Object importCall(EnvisionCodeFile code) {
		EnvisionObject env_obj = code.getValue(name);
		
		if (env_obj == null) throw new UndefinedValueError(name);
		if (!env_obj.isPublic()) throw new NotVisibleError(env_obj);
		
		if (env_obj instanceof EnvisionClass env_class) return classCall(env_class);
		if (env_obj instanceof EnvisionFunction env_func) return functionCall(env_func);
		return objectFunctionCall(env_obj);
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
	private Object instanceCall(ClassInstance ci) {
		EnvisionObject obj = ci.get(name);
		
		//if the object is not a field or class method, check for internal method
		if (obj == null) {
			InternalFunction im = ci.theClass.getInternalFunction(name, args);
			if (im != null) {
				try {
					im.invoke(interpreter, args);
				}
				catch (ReturnValue r) {
					if (e.next != null) return run(e.applyNext(r.object));
					else {
						//make sure this method isn't supposed to return void
						if (im.isVoid()) throw new VoidFunctionError(im);
						//check type
						if (e.name == null) {
							EnvisionDatatype im_type = im.getReturnType();
							EnvisionDatatype r_type = EnvisionDatatype.dynamicallyDetermineType(r.object);
							CastingUtil.assert_expected_datatype(im_type, r_type);
						}
						return r.object;
					}
				}
			}
		}
		
		if (obj instanceof EnvisionClass env_class) return classCall(env_class);
		if (obj instanceof EnvisionFunction env_func) return functionCall(env_func);
		if (obj instanceof EnvisionObject) return objectFunctionCall(obj);
		
		throw new InvalidTargetError(name + " is not a function! Instead is: '" + obj + "'!");
	}
	
	//handle classes
	private Object classCall(EnvisionClass c) {
		try {
			if (e.name != null) {
				c.runInternalFunction(name, interpreter, args);
			}
			else if (c.isAbstract()) throw new AbstractInstantiationError(c);
			else if (c.isInstantiable()) {
				//first check if it is actually 
				if (c.isPrimitive()) {
					//I don't like this code having to be evaluated here similar to a normal
					//assignment path. It feels like it should be calling one of the more
					//hard defined pathways instead.
					
					var value = (args.length == 1) ? args[0] : null;
					if (value instanceof EnvisionVariable env_var) value = env_var.get();
					return ObjectCreator.createObject(name, c.getDatatype(), value, false);
				}
				else {
					//System.out.println("\tthe instantiable object: " + c.getClass());
					//utilize construct optimization
					ClassConstruct con = c.getClassConstruct();
					if (con != null) con.call(interpreter, args);
					//otherwise default to basic creation
					else c.invoke_I(null, interpreter, args);
				}
			}
			else throw new UnsupportedInstantiationError(c);
		}
		catch (ReturnValue r) {
			return (e.next != null) ? run(e.applyNext(c)) : r.object;
		}
		
		throw new InvalidTargetError(name);
	}
	
	//handle methods
	private Object functionCall(EnvisionFunction m) {
		try {
			try {
				m.invoke_I(name, interpreter, args);
			}
			catch (ReturnValue r) { throw r; }
		}
		catch (ReturnValue r) {
			if (e.next != null) {
				return run(e.applyNext(r.object));
			}
			else {
				//make sure this method isn't supposed to return void
				if (m.isVoid()) throw new VoidFunctionError(m);
				//if it's a construtor return the created object
				if (m.isConstructor()) return r.object;
				//check type
				if (e.name == null) {
					EnvisionDatatype im_type = m.getReturnType();
					EnvisionDatatype r_type = EnvisionDatatype.dynamicallyDetermineType(r.object);
					CastingUtil.assert_expected_datatype(im_type, r_type);
				}
				return r.object;
			}
		}
		
		return new EnvisionVoidObject();
	}
	
	//handle object method
	private Object objectFunctionCall(EnvisionObject o) {
		try {
			if (e.name != null) o.runInternalFunction(name, interpreter, args);
			else o.runObjctConstructor(interpreter, args);
		}
		catch (ReturnValue r) {
			return (e.next != null) ? run(e.applyNext(r.object)) : r.object;
		}
		
		return new EnvisionVoidObject();
	}
	
}
