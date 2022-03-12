package envision.interpreter;

import envision.EnvisionCodeFile;
import envision.WorkingDirectory;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.UndefinedValueError;
import envision.interpreter.expressions.IE_Assign;
import envision.interpreter.expressions.IE_Binary;
import envision.interpreter.expressions.IE_Compound;
import envision.interpreter.expressions.IE_Domain;
import envision.interpreter.expressions.IE_Enum;
import envision.interpreter.expressions.IE_FuncDef;
import envision.interpreter.expressions.IE_FunctionCall;
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
import envision.interpreter.statements.IS_FuncDef;
import envision.interpreter.statements.IS_Generic;
import envision.interpreter.statements.IS_GetSet;
import envision.interpreter.statements.IS_If;
import envision.interpreter.statements.IS_Import;
import envision.interpreter.statements.IS_LambdaFor;
import envision.interpreter.statements.IS_LoopControl;
import envision.interpreter.statements.IS_ModularFunc;
import envision.interpreter.statements.IS_Package;
import envision.interpreter.statements.IS_RangeFor;
import envision.interpreter.statements.IS_Return;
import envision.interpreter.statements.IS_Switch;
import envision.interpreter.statements.IS_Try;
import envision.interpreter.statements.IS_VarDec;
import envision.interpreter.statements.IS_While;
import envision.interpreter.util.TypeManager;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBoolean;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.packages.env.EnvPackage;
import envision.lang.util.EnvisionDatatype;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.parser.expressions.expression_types.Expr_Assign;
import envision.parser.expressions.expression_types.Expr_Binary;
import envision.parser.expressions.expression_types.Expr_Compound;
import envision.parser.expressions.expression_types.Expr_Domain;
import envision.parser.expressions.expression_types.Expr_Enum;
import envision.parser.expressions.expression_types.Expr_FuncDef;
import envision.parser.expressions.expression_types.Expr_FunctionCall;
import envision.parser.expressions.expression_types.Expr_Generic;
import envision.parser.expressions.expression_types.Expr_Get;
import envision.parser.expressions.expression_types.Expr_Grouping;
import envision.parser.expressions.expression_types.Expr_Import;
import envision.parser.expressions.expression_types.Expr_Lambda;
import envision.parser.expressions.expression_types.Expr_ListIndex;
import envision.parser.expressions.expression_types.Expr_ListInitializer;
import envision.parser.expressions.expression_types.Expr_Literal;
import envision.parser.expressions.expression_types.Expr_Logic;
import envision.parser.expressions.expression_types.Expr_Range;
import envision.parser.expressions.expression_types.Expr_Set;
import envision.parser.expressions.expression_types.Expr_SetListIndex;
import envision.parser.expressions.expression_types.Expr_Super;
import envision.parser.expressions.expression_types.Expr_Ternary;
import envision.parser.expressions.expression_types.Expr_This;
import envision.parser.expressions.expression_types.Expr_TypeOf;
import envision.parser.expressions.expression_types.Expr_Unary;
import envision.parser.expressions.expression_types.Expr_VarDef;
import envision.parser.expressions.expression_types.Expr_Var;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statement_types.Stmt_Block;
import envision.parser.statements.statement_types.Stmt_SwitchCase;
import envision.parser.statements.statement_types.Stmt_Catch;
import envision.parser.statements.statement_types.Stmt_Class;
import envision.parser.statements.statement_types.Stmt_EnumDef;
import envision.parser.statements.statement_types.Stmt_Exception;
import envision.parser.statements.statement_types.Stmt_Expression;
import envision.parser.statements.statement_types.Stmt_For;
import envision.parser.statements.statement_types.Stmt_FuncDef;
import envision.parser.statements.statement_types.Stmt_Generic;
import envision.parser.statements.statement_types.Stmt_GetSet;
import envision.parser.statements.statement_types.Stmt_If;
import envision.parser.statements.statement_types.Stmt_Import;
import envision.parser.statements.statement_types.Stmt_InterfaceDef;
import envision.parser.statements.statement_types.Stmt_LambdaFor;
import envision.parser.statements.statement_types.Stmt_LoopControl;
import envision.parser.statements.statement_types.Stmt_ModularFuncDef;
import envision.parser.statements.statement_types.Stmt_Package;
import envision.parser.statements.statement_types.Stmt_RangeFor;
import envision.parser.statements.statement_types.Stmt_Return;
import envision.parser.statements.statement_types.Stmt_SwitchDef;
import envision.parser.statements.statement_types.Stmt_Try;
import envision.parser.statements.statement_types.Stmt_VarDef;
import envision.parser.statements.statement_types.Stmt_While;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

/** The head class that manages parsing script files. */
public class EnvisionInterpreter implements StatementHandler, ExpressionHandler {
	
	//--------
	// Fields
	//--------
	
	private final Scope global = new Scope(this);
	private Scope scope = global;
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
		
		new EnvPackage().defineOn(this);
		//EnvisionPackages.defineAll(this);
		
		EArrayList<Statement> statements = startingFile.getStatements();
		
		//go through and parse all method and class declarations first
		
		
		int cur = 0;
		try {
			for (Statement s : statements) {
				execute(s);
				cur++;
			}
			
			//System.out.println("Complete: " + codeFile() + " : " + scope);
		}
		catch (Exception error) {
			//such professional error handler
			System.out.println("(" + startingFile.getSystemFile() + ") error at: " + statements.get(cur));
			//error.printStackTrace();
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
		
		//if (s != null)
		s.execute(this);
	}
	
	public Object evaluate(Expression e) {
		//return (e != null) ? e.execute(this) : null;
		return e.execute(this);
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
	public TypeManager getTypeManager() { return typeManager; }
	
	//----------------------------
	// Interpreter Helper Methods
	//----------------------------
	
	public Scope pushScope() { return scope = new Scope(scope); }
	public Scope popScope() { return scope = scope.getParentScope(); }
	
	public Object lookUpVariable(Token name) {
		EnvisionObject object = null;
		
		object = scope.get(name.lexeme);
		if (object == null) throw new UndefinedValueError(name.lexeme);
		
		return object;
	}
	
	public void checkNumberOperand(Operator operator, Object operand) {
		if (operand instanceof Number) return;
		throw new RuntimeException(operator + " : Operand must be a number.");
	}
	
	public void checkNumberOperands(Operator operator, Object left, Object right) {
		if (left instanceof Number && right instanceof Number) return;
		throw new RuntimeException("(" + left + " " + operator.chars + " " + right + ") : Operands must be numbers.");
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
		
		if (a instanceof Character) return a.equals(b);
		if (b instanceof Character) return b.equals(a);
		
		if (a instanceof EnvisionVariable) return EnvisionObject.convert(a).equals(b);
		if (b instanceof EnvisionVariable) return EnvisionObject.convert(b).equals(a);
		
		return a.equals(b);
	}
	
	public boolean isDefined(Token name) { return isDefined(name.lexeme); }
	public boolean isDefined(String name) {
		try {
			scope.get(name);
			return true;
		}
		catch (UndefinedValueError e) { return false; }
	}
	
	/**
	 * This method will either define or overwrite an already defined value
	 * within this interpreter's current scope with the given 'object' value.
	 * <p>
	 * In the event that the given variable name is not already defined, the
	 * standard variable declaration process will be used. However, if the value
	 * is currently defined, the existing value will be completely overwritten,
	 * regardless of scope or visibility.
	 * <p>
	 * In terms of validity, it must be understood that this method will not
	 * attempt to check for logical variable overwrites, I.E. forcefully changing
	 * defined variable datatypes. Furthermore, this method will not attempt to preserve
	 * already existing values under the same name as existing values are completely
	 * overwritten. In terms of security, this method should be used with the utmost amount
	 * of caution as declaration overwrites will completely ignore existing variable
	 * modifiers, such as private or final. This means that potentially sensitive
	 * data could be overwritten regardless of scope and visibility. As such, this
	 * method is potentially a very destructive means of variable declaration, which
	 * could have dramatic effects on further program execution if used improperly.
	 * <p>
	 * In nearly every circumstance, the standard variable declaration approaches should be
	 * preferred over this method.
	 * 
	 * @param name The name of the object which will either be defined or overwritten
	 * @param object The object being storred at the given 'name' location.
	 * @return The defined object
	 */
	public EnvisionObject forceDefine(String name, Object object) {
		var type = EnvisionDatatype.dynamicallyDetermineType(object);
		EnvisionObject toDefine = ObjectCreator.createObject(name, type, object, false);
		EnvisionObject existing = null;
		
		try {
			existing = scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (existing == null) scope.define(name, toDefine);
		else scope.set(name, toDefine);
		
		return toDefine;
	}
	
	/**
	 * This method will either define or overwrite an already defined value
	 * within this interpreter's current scope with the given 'object' value.
	 * <p>
	 * In the event that the given variable name is not already defined, the
	 * standard variable declaration process will be used. However, if the value
	 * is currently defined, the existing value will be completely overwritten,
	 * regardless of scope or visibility.
	 * <p>
	 * In terms of validity, it must be understood that this method will not
	 * attempt to check for logical variable overwrites, I.E. forcefully changing
	 * defined variable datatypes. Furthermore, this method will not attempt to preserve
	 * already existing values under the same name as existing values are completely
	 * overwritten. In terms of security, this method should be used with the utmost amount
	 * of caution as declaration overwrites will completely ignore existing variable
	 * modifiers, such as private or final. This means that potentially sensitive
	 * data could be overwritten regardless of scope and visibility. As such, this
	 * method is potentially a very destructive means of variable declaration, which
	 * could have dramatic effects on further program execution if used improperly.
	 * <p>
	 * In nearly every circumstance, the standard variable declaration approaches should be
	 * preferred over this method.
	 * 
	 * @param name The name of the object which will either be defined or overwritten
	 * @param object The object being storred at the given 'name' location.
	 * @return The defined object
	 */
	public EnvisionObject forceDefine(String name, EnvisionDatatype type, Object object) {
		EnvisionObject toDefine = ObjectCreator.createObject(name, type, object, false);
		EnvisionObject existing = null;
		
		try {
			existing = scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (existing == null) scope.define(name, type, toDefine);
		else scope.set(name, type, toDefine);
		
		return toDefine;
	}
	
	/**
	 * This method will attempt to define a given object if it is
	 * found to not already exist within the current interpreter
	 * scope. In the event that a value under the same name is
	 * already defined, no further action will take place and this
	 * method will execute quietly.
	 * <p>
	 * This method will not attempt to check for inconsistent
	 * datatypes between existing and incomming values. Simply put,
	 * this method only chcks if a value of the same 'name' is
	 * currently defined within the current interpreter scope or not.
	 * Because of this, it must be understood that just because a
	 * value under the same name exists within the current interpreter
	 * scope, it may not be the variable that was expected.
	 * 
	 * @param name The name for which to define the given object on
	 * @param object The object to be defined if not already present
	 * @return The defined objet
	 */
	public EnvisionObject defineIfNot(String name, EnvisionObject object) {
		EnvisionObject o = null;
		
		try {
			o = scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return scope.define(name, object);
		
		return o;
	}
	
	/**
	 * This method will attempt to define a given object if it is
	 * found to not already exist within the current interpreter
	 * scope. In the event that a value under the same name is
	 * already defined, no further action will take place and this
	 * method will execute quietly.
	 * <p>
	 * This method will not attempt to check for inconsistent
	 * datatypes between existing and incomming values. Simply put,
	 * this method only chcks if a value of the same 'name' is
	 * currently defined within the current interpreter scope or not.
	 * Because of this, it must be understood that just because a
	 * value under the same name exists within the current interpreter
	 * scope, it may not be the variable that was expected.
	 * 
	 * @param name The name for which to define the given object on
	 * @param object The object to be defined if not already present
	 * @return The defined object
	 */
	public EnvisionObject defineIfNot(String name, EnvisionDatatype type, EnvisionObject object) {
		EnvisionObject o = null;
		
		try {
			o = scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return scope.define(name, type, object);
		
		return o;
	}
	
	/**
	 * This method will attempt to update the value of an already existing
	 * variable under the same name with the given object. In the event
	 * that the object is not defined under the current interpreter scope,
	 * the given object will be defined in its place.
	 * <p>
	 * Note: This method will ignore inconsistencies between existing value
	 * datatypes and incomming datatypes. This means that the use of this
	 * method could potentially lead to destructive variable declarations
	 * such that existing variables under the same name, but with a different
	 * datatype, will be overwritten by the new incomming object.
	 * 
	 * @param name The name for which to define the given object on
	 * @param object The object to be updated or defined if not already present
	 * @return The defined object
	 */
	public EnvisionObject updateOrDefine(String name, EnvisionObject object) {
		EnvisionObject o = null;
		
		try {
			o = scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return scope.define(name, object);
		
		scope.set(name, object);
		return object;
	}
	
	/**
	 * This method will attempt to update the value of an already existing
	 * variable under the same name with the given object. In the event
	 * that the object is not defined under the current interpreter scope,
	 * the given object will be defined in its place.
	 * <p>
	 * Unlike the version of this method without the 'type' parameter, this
	 * version will specifically check to ensure that incomming object
	 * datatypes are consistent with already existing variable datatypes.
	 * In the event that the incomming datatype does not match, or is not
	 * an instance of (polymorphism), the existing variable datatype, an
	 * InvalidDatatypeError is thrown. However, in the case that the
	 * incomming object is 'NULL', then null (EnvisionNull) will be assigned
	 * to the existing variable.
	 * 
	 * @param name The name for which to define the given object on
	 * @param object The object to be updated or defined if not already present
	 * @return The defined object
	 */
	public EnvisionObject updateOrDefine(String name, EnvisionDatatype type, EnvisionObject object) {
		Box2<EnvisionDatatype, EnvisionObject> o = null;
		
		//return the typed variable (if it exists)
		try {
			o = scope.getTyped(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return scope.define(name, type, object);
		
		//check for datatype consistency
		if (!o.getA().equals(type))
			throw new InvalidDatatypeError("The incomming datatype: '" + type + "' does not match the existing" +
										   " datatype '" + o.getA() + "'!");
		
		scope.set(name, type, object);
		return object;
	}
	
	/**
	 * If a variable under the given name is defined within the current
	 * interpreter scope, then the variable is returned, otherwise
	 * Java:null is returned instead.
	 * 
	 * @param name The variable name for which to retrieve an object from
	 * @return The objet if it is defined or null if it is not
	 */
	public EnvisionObject getIfDefined(String name) {
		EnvisionObject o = null;
		
		try {
			o = scope.get(name);
		} catch (UndefinedValueError e) {}
		
		return o;
	}
	
	//------------
	// Statements
	//------------
	
	@Override public void handleBlockStatement(Stmt_Block s) { IS_Block.run(this, s); }
	@Override public void handleLoopControlStatement(Stmt_LoopControl s) { IS_LoopControl.run(this, s); }
	@Override public void handleCatchStatement(Stmt_Catch s) { IS_Catch.run(this, s); }
	@Override public void handleCaseStatement(Stmt_SwitchCase s) { IS_Case.run(this, s); }
	@Override public void handleClassStatement(Stmt_Class s) { IS_Class.run(this, s); }
	@Override public void handleEnumStatement(Stmt_EnumDef s) { IS_Enum.run(this, s); }
	@Override public void handleExceptionStatement(Stmt_Exception s) { IS_Exception.run(this, s); }
	@Override public void handleExpressionStatement(Stmt_Expression s) { IS_Expression.run(this, s); }
	@Override public void handleForStatement(Stmt_For s) { IS_For.run(this, s); }
	@Override public void handleGenericStatement(Stmt_Generic s) { IS_Generic.run(this, s); }
	@Override public void handleGetSetStatement(Stmt_GetSet s) { IS_GetSet.run(this, s); }
	@Override public void handleIfStatement(Stmt_If s) { IS_If.run(this, s); }
	@Override public void handleImportStatement(Stmt_Import s) { IS_Import.run(this, s); }
	@Override public void handleInterfaceStatement(Stmt_InterfaceDef s) {}
	@Override public void handleLambdaForStatement(Stmt_LambdaFor s) { IS_LambdaFor.run(this, s); }
	@Override public void handleMethodStatement(Stmt_FuncDef s) { IS_FuncDef.run(this, s); }
	@Override public void handleModularMethodStatement(Stmt_ModularFuncDef s) { IS_ModularFunc.run(this, s); }
	@Override public void handlePackageStatement(Stmt_Package s) { IS_Package.run(this, s); }
	@Override public void handleRangeForStatement(Stmt_RangeFor s) { IS_RangeFor.run(this, s); }
	@Override public void handleReturnStatement(Stmt_Return s) { IS_Return.run(this, s); }
	@Override public void handleSwitchStatement(Stmt_SwitchDef s) { IS_Switch.run(this, s); }
	@Override public void handleTryStatement(Stmt_Try s) { IS_Try.run(this, s); }
	@Override public void handleVariableStatement(Stmt_VarDef s) { IS_VarDec.run(this, s); }
	@Override public void handleWhileStatement(Stmt_While s) { IS_While.run(this, s); }
	
	//-------------
	// Expressions
	//-------------
	
	@Override public Object handleAssign_E(Expr_Assign e) { return IE_Assign.run(this, e); }
	@Override public Object handleBinary_E(Expr_Binary e) { return IE_Binary.run(this, e); }
	@Override public Object handleCompound_E(Expr_Compound e) { return IE_Compound.run(this, e); }
	@Override public Object handleDomain_E(Expr_Domain e) { return IE_Domain.run(this, e); }
	@Override public Object handleEnum_E(Expr_Enum e) { return IE_Enum.run(this, e); }
	@Override public Object handleGeneric_E(Expr_Generic e) { return IE_Generic.run(this, e); }
	@Override public Object handleGet_E(Expr_Get e) { return IE_Get.run(this, e); }
	@Override public Object handleGrouping_E(Expr_Grouping e) { return IE_Grouping.run(this, e); }
	@Override public Object handleImport_E(Expr_Import e) { return IE_Import.run(this, e); }
	@Override public Object handleLambda_E(Expr_Lambda e) { return IE_Lambda.run(this, e); }
	@Override public Object handleListIndex_E(Expr_ListIndex e) { return IE_ListIndex.run(this, e); }
	@Override public Object handleListInitializer_E(Expr_ListInitializer e) { return IE_ListInitializer.run(this, e); }
	@Override public Object handleListIndexSet_E(Expr_SetListIndex e) { return IE_ListIndexSet.run(this, e); }
	@Override public Object handleLiteral_E(Expr_Literal e) { return IE_Literal.run(this, e); }
	@Override public Object handleLogical_E(Expr_Logic e) { return IE_Logical.run(this, e); }
	@Override public Object handleMethodCall_E(Expr_FunctionCall e) { return IE_FunctionCall.run(this, e); }
	@Override public Object handleMethodDec_E(Expr_FuncDef e) { return IE_FuncDef.run(this, e); }
	//@Override public Object handleModular_E(ModularExpression e) { return IE_Modular.run(this, e); }
	@Override public Object handleRange_E(Expr_Range e) { return IE_Range.run(this, e); }
	@Override public Object handleSet_E(Expr_Set e) { return IE_Set.run(this, e); }
	@Override public Object handleSuper_E(Expr_Super e) { return IE_Super.run(this, e); }
	@Override public Object handleTernary_E(Expr_Ternary e) { return IE_Ternary.run(this, e); }
	@Override public Object handleThisGet_E(Expr_This e) { return IE_This.run(this, e); }
	@Override public Object handleTypeOf_E(Expr_TypeOf e) { return IE_TypeOf.run(this, e); }
	@Override public Object handleUnary_E(Expr_Unary e) { return IE_Unary.run(this, e); }
	@Override public Object handleVarDec_E(Expr_VarDef e) { return IE_VarDec.run(this, e); }
	@Override public Object handleVar_E(Expr_Var e) { return IE_Var.run(this, e); }
	
}
