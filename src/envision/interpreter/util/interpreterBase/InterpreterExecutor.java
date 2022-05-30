package envision.interpreter.util.interpreterBase;

import envision.WorkingDirectory;
import envision.exceptions.errors.NullVariableError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionNull;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public abstract class InterpreterExecutor {
	
	protected EnvisionInterpreter interpreter;
	
	protected InterpreterExecutor(EnvisionInterpreter in) {
		interpreter = in;
	}
	
	//---------------------
	// Interpreter Wrapers
	//---------------------
	
	public void execute(Statement s) { interpreter.execute(s); }
	public EnvisionObject evaluate(Expression e) { return interpreter.evaluate(e); }
	public void executeBlock(EArrayList<Statement> statements, Scope env) { interpreter.executeBlock(statements, env); }
	
	public boolean isTrue(EnvisionObject obj) { return interpreter.isTrue(obj); }
	public boolean isEqual(EnvisionObject a, EnvisionObject b) { return interpreter.isEqual_i(a, b); }
	
	public WorkingDirectory workingDir() { return interpreter.workingDir(); }
	public Scope global() { return interpreter.internalScope(); }
	public Scope scope() { return interpreter.scope(); }
	
	protected Scope pushScope() { return interpreter.pushScope(); }
	protected Scope popScope() { return interpreter.popScope(); }
	
	protected EnvisionObject lookUpVariable(String name) {
		return interpreter.lookUpVariable(name);
	}
	
	protected boolean isDefined(Token name) {
		return interpreter.isDefined(name.lexeme);
	}
	
	protected boolean isDefined(String name) {
		return interpreter.isDefined(name);
	}
	
	protected EnvisionObject forceDefine(String name, EnvisionObject object) {
		return interpreter.forceDefine(name, object);
	}
	
	protected EnvisionObject forceDefine(String name, EnvisionDatatype type, EnvisionObject object) {
		return interpreter.forceDefine(name, type, object);
	}
	
	protected EnvisionObject defineIfNot(String name, EnvisionObject obj) {
		return interpreter.defineIfNot(name, obj);
	}
	
	protected EnvisionObject defineIfNot(String name, EnvisionDatatype typeIn, EnvisionObject obj) {
		return interpreter.defineIfNot(name, typeIn, obj);
	}
	
	protected EnvisionObject updateOrDefine(String name, EnvisionObject obj) {
		return interpreter.updateOrDefine(name, obj);
	}
	
	protected EnvisionObject updateOrDefine(String name, EnvisionDatatype type, EnvisionObject obj) {
		return interpreter.updateOrDefine(name, type, obj);
	}

	
	/**
	 * Returns true if the given object is either null by Java terms,
	 * or is an instance of an EnvisionNullObject by Envision terms.
	 * 
	 * @param in The object being compared to null
	 * @return true if the given object is in fact null
	 */
	public static boolean isNull(EnvisionObject in) {
		return (in == null || in == EnvisionNull.NULL);
	}
	
	/**
	 * Returns true if the given datatype is either null by Java terms,
	 * or is equivalent to the static EnvisionDataType.NULL by Envision
	 * terms.
	 * 
	 * @param typeIn The datatype being compared to null
	 * @return true if the given datatype is in fact null
	 */
	public static boolean isNull(Primitives typeIn) {
		return (typeIn == null || typeIn == Primitives.NULL);
	}
	
	/**
	 * Returns true if the given datatype is either null by Java terms,
	 * or is equivalent to the static EnvisionDataType.NULL by Envision
	 * terms.
	 * 
	 * @param typeIn The datatype being compared to null
	 * @return true if the given datatype is in fact null
	 */
	public static boolean isNull(EnvisionDatatype typeIn) {
		return (typeIn == null || typeIn == EnvisionDatatype.NULL_TYPE);
	}
	
	/**
	 * This method will throw a NullVariableError if the given object 
	 * is found to actually be null by either Java or Envision:Java's
	 * terms.
	 * <p>
	 * If the given value is found to NOT be null, then this method
	 * will execute quietly and return to normal code execution.
	 * 
	 * @param in The object being compared to null.
	 */
	public static void assertNotNull(EnvisionObject in) {
		if (isNull(in)) throw new NullVariableError(in);
	}
	
}
