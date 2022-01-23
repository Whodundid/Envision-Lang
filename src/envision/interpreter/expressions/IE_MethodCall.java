package envision.interpreter.expressions;

import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidTargetError;
import envision.exceptions.errors.NotVisibleError;
import envision.exceptions.errors.UndefinedValueError;
import envision.exceptions.errors.VoidMethodError;
import envision.exceptions.errors.objects.AbstractInstantiationError;
import envision.exceptions.errors.objects.UnsupportedInstantiationError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.CastingUtil;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.interpreter.util.optimizations.ClassConstruct;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionMethod;
import envision.lang.objects.EnvisionVoidObject;
import envision.lang.util.InternalMethod;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.MethodCallExpression;

public class IE_MethodCall extends ExpressionExecutor<MethodCallExpression> {

	private MethodCallExpression e;
	private String name;
	private Object[] args;
	
	public IE_MethodCall(EnvisionInterpreter in) {
		super(in);
	}
	
	//-------------------------------------------------------------------------

	@Override
	public Object run(MethodCallExpression expression) {
		//System.out.println(expression + " : " + expression.callee);
		Object o = (expression.callee instanceof Expression) ? evaluate((Expression) expression.callee) : expression.callee;
		e = expression;
		name = (e.name != null) ? e.name.lexeme : null;
		
		//determine what type of object is being called
		if (o instanceof EnvisionObject) {
			args = new Object[e.args.size()];
			
			//parse the args
			for (int i = 0; i < args.length; i++) {
				args[i] = evaluate(e.args.get(i));
			}
			
			//determine the type
			if (o instanceof EnvisionCodeFile) return importCall((EnvisionCodeFile) o);
			//if (o instanceof EnvisionLangPackage)  return packageCall((EnvisionLangPackage) o);
			if (o instanceof ClassInstance) return instanceCall((ClassInstance) o);
			if (o instanceof EnvisionClass) return classCall((EnvisionClass) o);
			if (o instanceof EnvisionMethod) return methodCall((EnvisionMethod) o);
			if (o instanceof EnvisionObject) return objectMethodCall((EnvisionObject) o);
		}
		else if (o != null) {
			return run(expression.setCallee(ObjectCreator.createObject(o)));
		}
		
		throw new EnvisionError("TEMP: Invalid method call expression! " + e + " : " + o);
	}
	
	//-------------------------------------------------------------------------
	
	public static Object run(EnvisionInterpreter in, MethodCallExpression e) {
		return new IE_MethodCall(in).run(e);
	}
	
	//-------------------------------------------------------------------------
	
	private Object importCall(EnvisionCodeFile code) {
		EnvisionObject obj = code.getValue(name);
		
		if (obj == null) throw new UndefinedValueError(name);
		if (!obj.isPublic()) throw new NotVisibleError(obj);
		
		if (obj instanceof EnvisionClass) return classCall((EnvisionClass) obj);
		if (obj instanceof EnvisionMethod) return methodCall((EnvisionMethod) obj);
		if (obj instanceof EnvisionObject) return objectMethodCall(obj);
		
		throw new InvalidTargetError(name);
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
			InternalMethod im = ci.theClass.getInternalMethod(name, args);
			if (im != null) {
				try {
					im.call(interpreter, args);
				}
				catch (ReturnValue r) {
					if (e.next != null) { return run(e.applyNext(r.object)); }
					else {
						//make sure this method isn't supposed to return void
						if (im.isVoid()) { throw new VoidMethodError(im); }
						//check type
						if (e.name == null) {
							CastingUtil.checkType(im.getReturnType(), r.object);
						}
						return r.object;
					}
				}
			}
		}
		
		if (obj instanceof EnvisionClass) { return classCall((EnvisionClass) obj); }
		if (obj instanceof EnvisionMethod) { return methodCall((EnvisionMethod) obj); }
		if (obj instanceof EnvisionObject) { return objectMethodCall(obj); }
		
		throw new InvalidTargetError(name + " is not a method! Instead is: '" + obj + "'!");
	}
	
	//handle classes
	private Object classCall(EnvisionClass c) {
		try {
			if (e.name != null) {
				c.runInternalMethod(name, interpreter, args);
			}
			else if (c.isAbstract()) throw new AbstractInstantiationError(c);
			else if (c.isInstantiable()) {
				//utilize construct optimization
				ClassConstruct con = c.getClassConstruct();
				if (con != null) con.call(interpreter, args);
				//otherwise default to basic creation
				else c.call(interpreter, args);
			}
			else throw new UnsupportedInstantiationError(c);
		}
		catch (ReturnValue r) {
			return (e.next != null) ? run(e.applyNext(c)) : r.object;
		}
		
		throw new InvalidTargetError(name);
	}
	
	//handle methods
	private Object methodCall(EnvisionMethod m) {
		try {
			//System.out.println(name + " : " + m);
			try {
				m.call_I(interpreter, args);
			}
			catch (ReturnValue r) { throw r; }
			
			if (name != null) m.runInternalMethod(name, interpreter, args);
			//else m.call_I(interpreter, args);
		}
		catch (ReturnValue r) {
			if (e.next != null) {
				return run(e.applyNext(r.object));
			}
			else {
				//make sure this method isn't supposed to return void
				if (m.isVoid()) throw new VoidMethodError(m);
				//check type
				if (e.name == null) {
					CastingUtil.checkType(m.getReturnType(), r.object);
				}
				return r.object;
			}
		}
		
		return new EnvisionVoidObject();
	}
	
	//handle object method
	private Object objectMethodCall(EnvisionObject o) {
		try {
			if (e.name != null) o.runInternalMethod(name, interpreter, args);
			else o.runObjctConstructor(interpreter, args);
		}
		catch (ReturnValue r) {
			return (e.next != null) ? run(e.applyNext(r.object)) : r.object;
		}
		
		return new EnvisionVoidObject();
	}
	
}
