package envision.interpreter.util.interpreterBase;

import envision.WorkingDirectory;
import envision.exceptions.errors.NullVariableError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionNullObject;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

import java.util.Map;

public abstract class InterpreterExecutor {
	
	protected EnvisionInterpreter interpreter;
	
	protected InterpreterExecutor(EnvisionInterpreter in) {
		interpreter = in;
	}
	
	//---------------------
	// Interpreter Wrapers
	//---------------------
	
	public void execute(Statement s) { interpreter.execute(s); }
	public Object evaluate(Expression e) { return interpreter.evaluate(e); }
	public void resolve(Expression e, int depth) { interpreter.resolve(e, depth); }
	public void executeBlock(EArrayList<Statement> statements, Scope env) { interpreter.executeBlock(statements, env); }
	
	public WorkingDirectory workingDir() { return interpreter.workingDir(); }
	public Scope global() { return interpreter.global(); }
	public Scope scope() { return interpreter.scope(); }
	public Map<Expression, Integer> locals() { return interpreter.locals(); }
	
	protected Scope pushScope() { return interpreter.pushScope(); }
	protected Scope popScope() { return interpreter.popScope(); }
	
	protected Object lookUpVariable(Token name/*, Expression expr*/) { return interpreter.lookUpVariable(name/*, expr*/); }
	
	protected void checkNumberOperand(Token operator, Object operand) { interpreter.checkNumberOperand(operator, operand); }
	protected void checkNumberOperands(Token operator, Object left, Object right) { interpreter.checkNumberOperands(operator, left, right); }
	protected void checkNumberOperands(String operator, Object left, Object right) { interpreter.checkNumberOperands(operator, left, right); }
	
	protected boolean isTruthy(Object object) { return interpreter.isTruthy(object); }
	protected boolean isEqual(Object a, Object b) { return interpreter.isEqual(a, b); }
	protected String stringify(Object object) { return interpreter.stringify(object); }
	
	protected boolean isDefined(Token name) { return interpreter.isDefined(name); }
	protected boolean isDefined(String name) { return interpreter.isDefined(name); }
	
	protected EnvisionObject checkDefined(Token name) { return interpreter.checkDefined(name); }
	protected EnvisionObject checkDefined(String name) { return interpreter.checkDefined(name); }
	
	protected EnvisionObject defineIfNot(Token name, Object object) { return interpreter.defineIfNot(name, object); }
	protected EnvisionObject defineIfNot(String name, Object object) { return interpreter.defineIfNot(name, object); }
	protected EnvisionObject defineIfNot(Token name, EnvisionObject object) { return interpreter.defineIfNot(name, object); }
	protected EnvisionObject defineIfNot(String name, EnvisionObject object) { return interpreter.defineIfNot(name, object); }
	
	public static void assertNull(Object in) {
		if (in == null || in instanceof EnvisionNullObject)
			throw new NullVariableError("The given object: '" + in + "' is null!");
	}
	
	public static boolean isNull(Object in) { return (in == null || in instanceof EnvisionNullObject); }
	
}
