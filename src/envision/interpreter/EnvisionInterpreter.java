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
import envision.interpreter.expressions.IE_FunctionCall;
import envision.interpreter.expressions.IE_FuncDef;
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
import envision.interpreter.statements.IS_FuncDef;
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
import envision.parser.expressions.expression_types.AssignExpression;
import envision.parser.expressions.expression_types.BinaryExpression;
import envision.parser.expressions.expression_types.CompoundExpression;
import envision.parser.expressions.expression_types.DomainExpression;
import envision.parser.expressions.expression_types.EnumExpression;
import envision.parser.expressions.expression_types.GenericExpression;
import envision.parser.expressions.expression_types.GetExpression;
import envision.parser.expressions.expression_types.GroupingExpression;
import envision.parser.expressions.expression_types.ImportExpression;
import envision.parser.expressions.expression_types.LambdaExpression;
import envision.parser.expressions.expression_types.ListIndexExpression;
import envision.parser.expressions.expression_types.ListIndexSetExpression;
import envision.parser.expressions.expression_types.ListInitializerExpression;
import envision.parser.expressions.expression_types.LiteralExpression;
import envision.parser.expressions.expression_types.LogicalExpression;
import envision.parser.expressions.expression_types.FunctionCallExpression;
import envision.parser.expressions.expression_types.FuncDefExpression;
import envision.parser.expressions.expression_types.RangeExpression;
import envision.parser.expressions.expression_types.SetExpression;
import envision.parser.expressions.expression_types.SuperExpression;
import envision.parser.expressions.expression_types.TernaryExpression;
import envision.parser.expressions.expression_types.ThisConExpression;
import envision.parser.expressions.expression_types.ThisGetExpression;
import envision.parser.expressions.expression_types.TypeOfExpression;
import envision.parser.expressions.expression_types.UnaryExpression;
import envision.parser.expressions.expression_types.VarDecExpression;
import envision.parser.expressions.expression_types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statement_types.BlockStatement;
import envision.parser.statements.statement_types.CaseStatement;
import envision.parser.statements.statement_types.CatchStatement;
import envision.parser.statements.statement_types.ClassStatement;
import envision.parser.statements.statement_types.EnumStatement;
import envision.parser.statements.statement_types.ExceptionStatement;
import envision.parser.statements.statement_types.ExpressionStatement;
import envision.parser.statements.statement_types.ForStatement;
import envision.parser.statements.statement_types.GenericStatement;
import envision.parser.statements.statement_types.GetSetStatement;
import envision.parser.statements.statement_types.IfStatement;
import envision.parser.statements.statement_types.ImportStatement;
import envision.parser.statements.statement_types.InterfaceStatement;
import envision.parser.statements.statement_types.LambdaForStatement;
import envision.parser.statements.statement_types.LoopControlStatement;
import envision.parser.statements.statement_types.FuncDefStatement;
import envision.parser.statements.statement_types.ModularFunctionStatement;
import envision.parser.statements.statement_types.PackageStatement;
import envision.parser.statements.statement_types.RangeForStatement;
import envision.parser.statements.statement_types.ReturnStatement;
import envision.parser.statements.statement_types.SwitchStatement;
import envision.parser.statements.statement_types.TryStatement;
import envision.parser.statements.statement_types.VariableStatement;
import envision.parser.statements.statement_types.WhileStatement;
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
	@Override public void handleMethodStatement(FuncDefStatement s) { IS_FuncDef.run(this, s); }
	@Override public void handleModularMethodStatement(ModularFunctionStatement s) { IS_ModularFunc.run(this, s); }
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
	@Override public Object handleMethodCall_E(FunctionCallExpression e) { return IE_FunctionCall.run(this, e); }
	@Override public Object handleMethodDec_E(FuncDefExpression e) { return IE_FuncDef.run(this, e); }
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
