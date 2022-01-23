package envision.interpreter;

import envision.EnvisionCodeFile;
import envision.WorkingDirectory;
import envision.exceptions.errors.UndefinedValueError;
import envision.interpreter.expressions.IE_Assign;
import envision.interpreter.expressions.IE_Binary;
import envision.interpreter.expressions.IE_Compound;
import envision.interpreter.expressions.IE_Domain;
import envision.interpreter.expressions.IE_Enum;
import envision.interpreter.expressions.IE_Generic;
import envision.interpreter.expressions.IE_Get;
import envision.interpreter.expressions.IE_Grouping;
import envision.interpreter.expressions.IE_Import;
import envision.interpreter.expressions.IE_Lambda;
import envision.interpreter.expressions.IE_ListIndex;
import envision.interpreter.expressions.IE_ListIndexSet;
import envision.interpreter.expressions.IE_ListInitializer;
import envision.interpreter.expressions.IE_Literal;
import envision.interpreter.expressions.IE_Logical;
import envision.interpreter.expressions.IE_MethodCall;
import envision.interpreter.expressions.IE_MethodDec;
import envision.interpreter.expressions.IE_Range;
import envision.interpreter.expressions.IE_Set;
import envision.interpreter.expressions.IE_Super;
import envision.interpreter.expressions.IE_Ternary;
import envision.interpreter.expressions.IE_This;
import envision.interpreter.expressions.IE_TypeOf;
import envision.interpreter.expressions.IE_Unary;
import envision.interpreter.expressions.IE_Var;
import envision.interpreter.expressions.IE_VarDec;
import envision.interpreter.statements.IS_Block;
import envision.interpreter.statements.IS_Case;
import envision.interpreter.statements.IS_Catch;
import envision.interpreter.statements.IS_Class;
import envision.interpreter.statements.IS_Enum;
import envision.interpreter.statements.IS_Exception;
import envision.interpreter.statements.IS_Expression;
import envision.interpreter.statements.IS_For;
import envision.interpreter.statements.IS_Generic;
import envision.interpreter.statements.IS_GetSet;
import envision.interpreter.statements.IS_If;
import envision.interpreter.statements.IS_Import;
import envision.interpreter.statements.IS_LambdaFor;
import envision.interpreter.statements.IS_LoopControl;
import envision.interpreter.statements.IS_MethodDeclaration;
import envision.interpreter.statements.IS_ModularMethod;
import envision.interpreter.statements.IS_Package;
import envision.interpreter.statements.IS_RangeFor;
import envision.interpreter.statements.IS_Return;
import envision.interpreter.statements.IS_Switch;
import envision.interpreter.statements.IS_Try;
import envision.interpreter.statements.IS_VarDec;
import envision.interpreter.statements.IS_While;
import envision.interpreter.util.Scope;
import envision.interpreter.util.TypeManager;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionMethod;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.packages.env.EnvPackage;
import envision.lang.variables.EnvisionBoolean;
import envision.lang.variables.EnvisionVariable;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.parser.expressions.types.AssignExpression;
import envision.parser.expressions.types.BinaryExpression;
import envision.parser.expressions.types.CompoundExpression;
import envision.parser.expressions.types.DomainExpression;
import envision.parser.expressions.types.EnumExpression;
import envision.parser.expressions.types.GenericExpression;
import envision.parser.expressions.types.GetExpression;
import envision.parser.expressions.types.GroupingExpression;
import envision.parser.expressions.types.ImportExpression;
import envision.parser.expressions.types.LambdaExpression;
import envision.parser.expressions.types.ListIndexExpression;
import envision.parser.expressions.types.ListIndexSetExpression;
import envision.parser.expressions.types.ListInitializerExpression;
import envision.parser.expressions.types.LiteralExpression;
import envision.parser.expressions.types.LogicalExpression;
import envision.parser.expressions.types.MethodCallExpression;
import envision.parser.expressions.types.MethodDeclarationExpression;
import envision.parser.expressions.types.RangeExpression;
import envision.parser.expressions.types.SetExpression;
import envision.parser.expressions.types.SuperExpression;
import envision.parser.expressions.types.TernaryExpression;
import envision.parser.expressions.types.ThisConExpression;
import envision.parser.expressions.types.ThisGetExpression;
import envision.parser.expressions.types.TypeOfExpression;
import envision.parser.expressions.types.UnaryExpression;
import envision.parser.expressions.types.VarDecExpression;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.types.BlockStatement;
import envision.parser.statements.types.CaseStatement;
import envision.parser.statements.types.CatchStatement;
import envision.parser.statements.types.ClassStatement;
import envision.parser.statements.types.EnumStatement;
import envision.parser.statements.types.ExceptionStatement;
import envision.parser.statements.types.ExpressionStatement;
import envision.parser.statements.types.ForStatement;
import envision.parser.statements.types.GenericStatement;
import envision.parser.statements.types.GetSetStatement;
import envision.parser.statements.types.IfStatement;
import envision.parser.statements.types.ImportStatement;
import envision.parser.statements.types.InterfaceStatement;
import envision.parser.statements.types.LambdaForStatement;
import envision.parser.statements.types.LoopControlStatement;
import envision.parser.statements.types.MethodDeclarationStatement;
import envision.parser.statements.types.ModularMethodStatement;
import envision.parser.statements.types.PackageStatement;
import envision.parser.statements.types.RangeForStatement;
import envision.parser.statements.types.ReturnStatement;
import envision.parser.statements.types.SwitchStatement;
import envision.parser.statements.types.TryStatement;
import envision.parser.statements.types.VariableStatement;
import envision.parser.statements.types.WhileStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

import java.util.HashMap;
import java.util.Map;

/** The head class that manages parsing script files. */
public class EnvisionInterpreter implements StatementHandler, ExpressionHandler {
	
	//--------
	// Fields
	//--------
	
	private final Scope global = new Scope(this);
	private Scope scope = global;
	private final Map<Expression, Integer> locals = new HashMap();
	private WorkingDirectory directory;
	private EnvisionCodeFile startingFile;
	public final String fileName;
	private final TypeManager typeManager = new TypeManager();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionInterpreter(EnvisionCodeFile codeFileIn) {
		startingFile = codeFileIn;
		fileName = startingFile.getFileName();
	}
	
	//---------------------------------------------------------------------------------
	
	//public static EnvisionInterpreter interpret(EnvisionCodeFile codeFile, WorkingDirectory dir) throws Exception {
	//	return new EnvisionInterpreter(codeFile, dir).interpretI();
	//}
	
	public EnvisionInterpreter interpret(WorkingDirectory dirIn) throws Exception {
		directory = dirIn;
		
		//define primitives:
		//typeManager.defineType("int", EnvisionInt.getBase());
		
		new EnvPackage().defineOn(this);
		//EnvisionPackages.defineAll(this);
		
		EArrayList<Statement> statements = startingFile.getStatements();
		
		//go through and parse all method and class declarations first
		
		
		int cur = 0;
		try {
			for (Statement s : statements) {
				//System.out.println(s);
				execute(s);
				cur++;
			}
			
			//System.out.println("Complete: " + codeFile() + " : " + scope);
		}
		catch (Exception error) {
			//such professional error handler
			System.out.println("(" + startingFile.getSystemFile() + ") error at: " + statements.get(cur));
			throw error;
		}
		
		return this;
	}
	
	//---------------------
	// Interpreter Methods
	//---------------------
	
	public void execute(Statement s) {
		//String c = (s != null) ? "'" + s + "' : " + s.getClass() + " " : "";
		
		//System.out.println("top: " + c + ": " + scope());
		//System.out.println(c);
		
		if (s != null) { s.execute(this); }
	}
	
	public Object evaluate(Expression e) {
		return (e != null) ? e.execute(this) : null;
	}
	
	public void resolve(Expression e, int depth) {
		locals.put(e, depth);
	}
	
	public void executeBlock(EArrayList<Statement> statements, Scope env) {
		Scope prev = scope;
		try {
			scope = env;
			for (Statement s : statements) {
				execute(s);
			}
		}
		finally {
			scope = prev;
		}
	}
	
	public WorkingDirectory workingDir() { return directory; }
	public EnvisionCodeFile codeFile() { return startingFile; }
	public Scope global() { return global; }
	public Scope scope() { return scope; }
	public Map<Expression, Integer> locals() { return locals; }
	public TypeManager getTypeManager() { return typeManager; }
	
	//----------------------------
	// Interpreter Helper Methods
	//----------------------------
	
	public Scope pushScope() { return scope = new Scope(scope); }
	public Scope popScope() { return scope = scope.getParentScope(); }
	
	public Object lookUpVariable(Token name/*, Expression expr*/) {
		//Integer distance = locals.get(expr);
		EnvisionObject object = null;
		
		//if (distance != null) {
		//	object = scope.getAt(distance, name.lexeme);
		//}
		//else {
			//System.out.println(codeFile() + " : " + scope());
			object = scope.get(name.lexeme);
			if (object == null) throw new UndefinedValueError(name.lexeme);
		//}
		
		return object;
	}
	
	public void checkNumberOperand(Token operator, Object operand) {
		if (operand instanceof Number) return;
		throw new RuntimeException(operator + " : Operand must be a number.");
	}
	
	public void checkNumberOperands(Token operator, Object left, Object right) {
		if (left instanceof Number && right instanceof Number) return;
		throw new RuntimeException("(" + left + " " + operator.lexeme + " " + right + ") : Operands must be numbers.");
	}
	
	public void checkNumberOperands(String operator, Object left, Object right) {
		if (left instanceof Number && right instanceof Number) return;
		throw new RuntimeException(operator + " : Operands must be numbers.");
	}
	
	public boolean isTruthy(Object object) {
		if (object == null) return false;
		if (object instanceof Boolean) return (boolean) object;
		if (object instanceof Number) return ((Number) object).doubleValue() != 0;
		if (object instanceof EnvisionBoolean) return ((EnvisionBoolean) object).isTrue();
		return false;
	}
	
	public boolean isEqual(Object a, Object b) {
		if (a instanceof EnvisionNullObject && b == null) return true;
		if (a == null && b instanceof EnvisionNullObject) return true;
		if (a == null && b == null) return true;
		if (a == null) return false;
		
		if (a instanceof Character) { a = a + ""; }
		if (b instanceof Character) { b = b + ""; }
		
		if (a instanceof EnvisionVariable) return EnvisionVariable.convert(a).equals(b);
		if (b instanceof EnvisionVariable) return EnvisionVariable.convert(b).equals(a);
		
		return a.equals(b);
	}
	
	public String stringify(Object object) {
		if (object == null) return "null";
		
		//if (object instanceof String) {
			//object = ((String) object).replace("\"", "");
		//}
		
		if (object instanceof Double) {
			String text = object.toString();
			if (text.endsWith(".0")) { text = text.substring(0, text.length() - 2); }
			return text;
		}
		
		if (object instanceof ClassInstance) {
			ClassInstance ci = (ClassInstance) object;
			EnvisionMethod toString = ci.getMethod("toString", new EArrayList());
			System.out.println("the method: " + toString);
		}
		
		return object.toString();
	}
	
	public boolean isDefined(Token name) {
		try { scope.get(name.lexeme); return true; } catch (UndefinedValueError e) { return false; }
	}
	
	public boolean isDefined(String name) {
		try { scope.get(name); return true; } catch (UndefinedValueError e) { return false; }
	}
	
	public EnvisionObject checkDefined(Token name) {
		EnvisionObject o = null;
		try { o = scope.get(name.lexeme); } catch (UndefinedValueError e) {}
		if (o != null) { return o; }
		throw new UndefinedValueError(name.lexeme);
	}
	
	public EnvisionObject checkDefined(String name) {
		EnvisionObject o = null;
		try { o = scope.get(name); } catch (UndefinedValueError e) {}
		if (o != null) { return o; }
		throw new UndefinedValueError(name);
	}
	
	public EnvisionObject defineIfNot(Token name, Object object) {
		EnvisionObject o = null;
		try { o = scope.get(name.lexeme); } catch (UndefinedValueError e) {}
		if (o == null) { return scope.define(name.lexeme, ObjectCreator.createObject(name.lexeme, object)); }
		return o;
	}
	
	public EnvisionObject defineIfNot(String name, Object object) {
		EnvisionObject o = null;
		try { o = scope.get(name); } catch (UndefinedValueError e) {}
		if (o == null) { return scope.define(name, ObjectCreator.createObject(name, object)); }
		return o;
	}
	
	public EnvisionObject defineIfNot(Token name, EnvisionObject object) {
		EnvisionObject o = null;
		try { o = scope.get(name.lexeme); } catch (UndefinedValueError e) {}
		if (o == null) { return scope.define(name.lexeme, object); }
		return o;
	}
	
	public EnvisionObject defineIfNot(String name, EnvisionObject object) {
		EnvisionObject o = scope.get(name);
		if (o == null) { return scope.define(name, object); }
		return o;
	}
	
	public EnvisionObject define(EnvisionMethod m) {
		return scope.define(m.getName(), m);
	}
	
	//------------
	// Statements
	//------------
	
	@Override public void handleBlockStatement(BlockStatement s) { IS_Block.run(this, s); }
	@Override public void handleLoopControlStatement(LoopControlStatement s) { IS_LoopControl.run(this, s); }
	@Override public void handleCatchStatement(CatchStatement s) { IS_Catch.run(this, s); }
	@Override public void handleCaseStatement(CaseStatement s) { IS_Case.run(this, s); }
	@Override public void handleClassStatement(ClassStatement s) { IS_Class.run(this, s); }
	@Override public void handleEnumStatement(EnumStatement s) { IS_Enum.run(this, s); }
	@Override public void handleExceptionStatement(ExceptionStatement s) { IS_Exception.run(this, s); }
	@Override public void handleExpressionStatement(ExpressionStatement s) { IS_Expression.run(this, s); }
	@Override public void handleForStatement(ForStatement s) { IS_For.run(this, s); }
	@Override public void handleGenericStatement(GenericStatement s) { IS_Generic.run(this, s); }
	@Override public void handleGetSetStatement(GetSetStatement s) { IS_GetSet.run(this, s); }
	@Override public void handleIfStatement(IfStatement s) { IS_If.run(this, s); }
	@Override public void handleImportStatement(ImportStatement s) { IS_Import.run(this, s); }
	@Override public void handleInterfaceStatement(InterfaceStatement s) {}
	@Override public void handleLambdaForStatement(LambdaForStatement s) { IS_LambdaFor.run(this, s); }
	@Override public void handleMethodStatement(MethodDeclarationStatement s) { IS_MethodDeclaration.run(this, s); }
	@Override public void handleModularMethodStatement(ModularMethodStatement s) { IS_ModularMethod.run(this, s); }
	@Override public void handlePackageStatement(PackageStatement s) { IS_Package.run(this, s); }
	@Override public void handleRangeForStatement(RangeForStatement s) { IS_RangeFor.run(this, s); }
	@Override public void handleReturnStatement(ReturnStatement s) { IS_Return.run(this, s); }
	@Override public void handleSwitchStatement(SwitchStatement s) { IS_Switch.run(this, s); }
	@Override public void handleTryStatement(TryStatement s) { IS_Try.run(this, s); }
	@Override public void handleVariableStatement(VariableStatement s) { IS_VarDec.run(this, s); }
	@Override public void handleWhileStatement(WhileStatement s) { IS_While.run(this, s); }
	
	//-------------
	// Expressions
	//-------------
	
	@Override public Object handleAssign_E(AssignExpression e) { return IE_Assign.run(this, e); }
	@Override public Object handleBinary_E(BinaryExpression e) { return IE_Binary.run(this, e); }
	@Override public Object handleCompound_E(CompoundExpression e) { return IE_Compound.run(this, e); }
	@Override public Object handleDomain_E(DomainExpression e) { return IE_Domain.run(this, e); }
	@Override public Object handleEnum_E(EnumExpression e) { return IE_Enum.run(this, e); }
	@Override public Object handleGeneric_E(GenericExpression e) { return IE_Generic.run(this, e); }
	@Override public Object handleGet_E(GetExpression e) { return IE_Get.run(this, e); }
	@Override public Object handleGrouping_E(GroupingExpression e) { return IE_Grouping.run(this, e); }
	@Override public Object handleImport_E(ImportExpression e) { return IE_Import.run(this, e); }
	@Override public Object handleLambda_E(LambdaExpression e) { return IE_Lambda.run(this, e); }
	@Override public Object handleListIndex_E(ListIndexExpression e) { return IE_ListIndex.run(this, e); }
	@Override public Object handleListInitializer_E(ListInitializerExpression e) { return IE_ListInitializer.run(this, e); }
	@Override public Object handleListIndexSet_E(ListIndexSetExpression e) { return IE_ListIndexSet.run(this, e); }
	@Override public Object handleLiteral_E(LiteralExpression e) { return IE_Literal.run(this, e); }
	@Override public Object handleLogical_E(LogicalExpression e) { return IE_Logical.run(this, e); }
	@Override public Object handleMethodCall_E(MethodCallExpression e) { return IE_MethodCall.run(this, e); }
	@Override public Object handleMethodDec_E(MethodDeclarationExpression e) { return IE_MethodDec.run(this, e); }
	//@Override public Object handleModular_E(ModularExpression e) { return IE_Modular.run(this, e); }
	@Override public Object handleRange_E(RangeExpression e) { return IE_Range.run(this, e); }
	@Override public Object handleSet_E(SetExpression e) { return IE_Set.run(this, e); }
	@Override public Object handleSuper_E(SuperExpression e) { return IE_Super.run(this, e); }
	@Override public Object handleTernary_E(TernaryExpression e) { return IE_Ternary.run(this, e); }
	@Override public Object handleThisCon_E(ThisConExpression e) { return null; }
	@Override public Object handleThisGet_E(ThisGetExpression e) { return IE_This.run(this, e); }
	@Override public Object handleTypeOf_E(TypeOfExpression e) { return IE_TypeOf.run(this, e); }
	@Override public Object handleUnary_E(UnaryExpression e) { return IE_Unary.run(this, e); }
	@Override public Object handleVarDec_E(VarDecExpression e) { return IE_VarDec.run(this, e); }
	@Override public Object handleVar_E(VarExpression e) { return IE_Var.run(this, e); }
	
}
