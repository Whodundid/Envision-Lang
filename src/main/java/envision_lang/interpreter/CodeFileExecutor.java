package envision_lang.interpreter;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.WorkingDirectory;
import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.UndefinedValueError;
import envision_lang.interpreter.expressions.IE_Assign;
import envision_lang.interpreter.expressions.IE_Binary;
import envision_lang.interpreter.expressions.IE_Cast;
import envision_lang.interpreter.expressions.IE_Compound;
import envision_lang.interpreter.expressions.IE_Domain;
import envision_lang.interpreter.expressions.IE_Enum;
import envision_lang.interpreter.expressions.IE_FunctionCall;
import envision_lang.interpreter.expressions.IE_Generic;
import envision_lang.interpreter.expressions.IE_Get;
import envision_lang.interpreter.expressions.IE_Grouping;
import envision_lang.interpreter.expressions.IE_Import;
import envision_lang.interpreter.expressions.IE_Lambda;
import envision_lang.interpreter.expressions.IE_ListIndex;
import envision_lang.interpreter.expressions.IE_ListIndexSet;
import envision_lang.interpreter.expressions.IE_ListInitializer;
import envision_lang.interpreter.expressions.IE_Literal;
import envision_lang.interpreter.expressions.IE_Logical;
import envision_lang.interpreter.expressions.IE_Primitive;
import envision_lang.interpreter.expressions.IE_Range;
import envision_lang.interpreter.expressions.IE_Set;
import envision_lang.interpreter.expressions.IE_Super;
import envision_lang.interpreter.expressions.IE_Ternary;
import envision_lang.interpreter.expressions.IE_This;
import envision_lang.interpreter.expressions.IE_TypeOf;
import envision_lang.interpreter.expressions.IE_Unary;
import envision_lang.interpreter.expressions.IE_Var;
import envision_lang.interpreter.expressions.IE_VarDec;
import envision_lang.interpreter.statements.IS_Block;
import envision_lang.interpreter.statements.IS_Case;
import envision_lang.interpreter.statements.IS_Catch;
import envision_lang.interpreter.statements.IS_Class;
import envision_lang.interpreter.statements.IS_Enum;
import envision_lang.interpreter.statements.IS_Exception;
import envision_lang.interpreter.statements.IS_Expression;
import envision_lang.interpreter.statements.IS_For;
import envision_lang.interpreter.statements.IS_FuncDef;
import envision_lang.interpreter.statements.IS_Generic;
import envision_lang.interpreter.statements.IS_GetSet;
import envision_lang.interpreter.statements.IS_If;
import envision_lang.interpreter.statements.IS_Import;
import envision_lang.interpreter.statements.IS_LambdaFor;
import envision_lang.interpreter.statements.IS_LoopControl;
import envision_lang.interpreter.statements.IS_ModularFunc;
import envision_lang.interpreter.statements.IS_Package;
import envision_lang.interpreter.statements.IS_RangeFor;
import envision_lang.interpreter.statements.IS_Return;
import envision_lang.interpreter.statements.IS_Switch;
import envision_lang.interpreter.statements.IS_Try;
import envision_lang.interpreter.statements.IS_VarDec;
import envision_lang.interpreter.statements.IS_While;
import envision_lang.interpreter.util.UserDefinedTypeManager;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.parser.expressions.expression_types.Expr_Cast;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_Domain;
import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_FunctionCall;
import envision_lang.parser.expressions.expression_types.Expr_Generic;
import envision_lang.parser.expressions.expression_types.Expr_Get;
import envision_lang.parser.expressions.expression_types.Expr_Grouping;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import envision_lang.parser.expressions.expression_types.Expr_ListIndex;
import envision_lang.parser.expressions.expression_types.Expr_ListInitializer;
import envision_lang.parser.expressions.expression_types.Expr_Literal;
import envision_lang.parser.expressions.expression_types.Expr_Logic;
import envision_lang.parser.expressions.expression_types.Expr_Primitive;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.expressions.expression_types.Expr_Set;
import envision_lang.parser.expressions.expression_types.Expr_SetListIndex;
import envision_lang.parser.expressions.expression_types.Expr_Super;
import envision_lang.parser.expressions.expression_types.Expr_Ternary;
import envision_lang.parser.expressions.expression_types.Expr_This;
import envision_lang.parser.expressions.expression_types.Expr_TypeOf;
import envision_lang.parser.expressions.expression_types.Expr_Unary;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.expressions.expression_types.Expr_VarDef;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Catch;
import envision_lang.parser.statements.statement_types.Stmt_Class;
import envision_lang.parser.statements.statement_types.Stmt_EnumDef;
import envision_lang.parser.statements.statement_types.Stmt_Exception;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.statements.statement_types.Stmt_For;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.statements.statement_types.Stmt_Generic;
import envision_lang.parser.statements.statement_types.Stmt_GetSet;
import envision_lang.parser.statements.statement_types.Stmt_If;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.parser.statements.statement_types.Stmt_InterfaceDef;
import envision_lang.parser.statements.statement_types.Stmt_LambdaFor;
import envision_lang.parser.statements.statement_types.Stmt_LoopControl;
import envision_lang.parser.statements.statement_types.Stmt_ModularFuncDef;
import envision_lang.parser.statements.statement_types.Stmt_Package;
import envision_lang.parser.statements.statement_types.Stmt_RangeFor;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import envision_lang.parser.statements.statement_types.Stmt_SwitchCase;
import envision_lang.parser.statements.statement_types.Stmt_SwitchDef;
import envision_lang.parser.statements.statement_types.Stmt_Try;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.statements.statement_types.Stmt_While;
import envision_lang.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.util.EList;

public final class CodeFileExecutor implements ExpressionHandler, StatementHandler {
	
	//========
	// Fields
	//========
	
	/** The code file that is being interpreted. */
	private EnvisionCodeFile codeFile;
	
	/**
	 * The active working file directory for which this interpreter has been created
	 * from. This working directory contains all of the related codeFiles enclosed
	 * within the given directory.
	 */
	private WorkingDirectory activeWorkingDir;
	
	/** Lowest scope level intended exclusively for language packages and internal functions. */
	private final IScope internalScope = new Scope();
	/** The scope intended for actual user program execution. */
	private final IScope programScope = new Scope(internalScope);
	/** The primary working interpreter scope. */
	IScope working_scope = programScope;
	
	/**
	 * Keeps track and manages all user-defined datatypes during instance creation
	 * for any object that is created within this interpreter.
	 * <p>
	 * A user-defined type is any datatype (class) that has been defined by Envision
	 * code executed at runtime. This includes any types defined by any internal or
	 * user-defined EnvisionPackages.
	 */
	private final UserDefinedTypeManager typeManager = new UserDefinedTypeManager();
	
	//==============
	// Constructors
	//==============
	
	public CodeFileExecutor(EnvisionCodeFile codeFileIn) {
		codeFile = codeFileIn;
		activeWorkingDir = codeFile.getWorkingDir();
	}
	
	//----------------------
	// Interpreter Wrappers
	//----------------------
	
	protected void execute(ParsedStatement s) {
		EnvisionInterpreter.execute(this, s);
	}
	
	protected EnvisionObject evaluate(ParsedExpression e) {
		return EnvisionInterpreter.evaluate(this, e);
	}
	
	protected void executeBlock(EList<ParsedStatement> statements, Scope env) {
		EnvisionInterpreter.executeBlock(this, statements, env);
	}
	
	/**
	 * Returns the lowest scope level of this interpreter. This internal
	 * scope contains all native Envision members that are defined upon
	 * this interpreter's creation. Execute caution when modifiying the
	 * values present within this scope as they cannot be restored to
	 * their original state.
	 * 
	 * @return The internal scope of this interpreter
	 */
	public IScope internalScope() { return internalScope; }
	
	/**
	 * Returns the current working scope that is actively bound.
	 * 
	 * @return The current working scope
	 */
	public IScope scope() { return working_scope; }
	
	/**
	 * Returns the UserDefinedTypeManager for this executor. The type manager
	 * actively keeps track of all user-defined class types that have been
	 * defined while interpreting this code file.
	 * <p>
	 * User-defined types that are defined outside of this interpreter will
	 * need to be imported in order to be recognized.
	 * 
	 * @return The UserDefinedTypeManager for this code file
	 */
	public UserDefinedTypeManager getUserDefinedTypeManager() { return typeManager; }
	
	protected WorkingDirectory workingDir() { return activeWorkingDir; }
	protected IScope global() { return internalScope; }
	
	protected IScope pushScope() { return working_scope = new Scope(working_scope); }
	protected IScope popScope() { return working_scope = working_scope.getParent(); }
	
	public EnvisionObject lookUpVariable(String name) {
		EnvisionObject object = EnvisionNull.NULL;
		
		object = working_scope.get(name);
		if (object == null) throw new UndefinedValueError(name);
		
		return object;
	}
	
	public boolean isDefined(Token name) { return isDefined(name.getLexeme()); }
	public boolean isDefined(String name) {
		try {
			working_scope.get(name);
			return true;
		}
		catch (UndefinedValueError e) {
			return false;
		}
	}
	
	/**
	 * This method will either define or overwrite an already defined
	 * value within this interpreter's current scope with the given
	 * 'object' value.
	 * <p>
	 * In the event that the given variable name is not already defined,
	 * the standard variable declaration process will be used. However, if
	 * the value is currently defined, the existing value will be
	 * completely overwritten, regardless of scope or visibility.
	 * <p>
	 * In terms of validity, it must be understood that this method will
	 * not attempt to check for logical variable overwrites, I.E.
	 * forcefully changing defined variable datatypes. Furthermore, this
	 * method will not attempt to preserve already existing values under
	 * the same name as existing values are completely overwritten. In
	 * terms of security, this method should be used with the utmost
	 * amount of caution as declaration overwrites will completely ignore
	 * existing variable modifiers, such as private or final. This means
	 * that potentially sensitive data could be overwritten regardless of
	 * scope and visibility. As such, this method should be treated as a
	 * <u><strong>VERY DESTRUCTIVE</strong></u> method of variable
	 * declaration which could have dramatic effects on further program
	 * execution if used improperly.
	 * <p>
	 * In nearly every circumstance, the standard variable declaration
	 * approaches should be preferred over this method.
	 * 
	 * @param name   The name of the object which will either be defined
	 *               or overwritten
	 * @param object The object being stored at the given 'name'
	 *               location.
	 * @return The defined object
	 */
	public EnvisionObject forceDefine(String name, EnvisionObject object) {
		EnvisionObject existing = null;
		
		try {
			existing = working_scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (existing == null) working_scope.define(name, object);
		else working_scope.set(name, object);
		
		return object;
	}
	
	/**
	 * This method will either define or overwrite an already defined
	 * value within this interpreter's current scope with the given
	 * 'object' value.
	 * <p>
	 * In the event that the given variable name is not already defined,
	 * the standard variable declaration process will be used. However, if
	 * the value is currently defined, the existing value will be
	 * completely overwritten, regardless of scope or visibility.
	 * <p>
	 * In terms of validity, it must be understood that this method will
	 * not attempt to check for logical variable overwrites, I.E.
	 * forcefully changing defined variable datatypes. Furthermore, this
	 * method will not attempt to preserve already existing values under
	 * the same name as existing values are completely overwritten. In
	 * terms of security, this method should be used with the utmost
	 * amount of caution as declaration overwrites will completely ignore
	 * existing variable modifiers, such as private or final. This means
	 * that potentially sensitive data could be overwritten regardless of
	 * scope and visibility. As such, this method is potentially a very
	 * destructive means of variable declaration, which could have
	 * dramatic effects on further program execution if used improperly.
	 * <p>
	 * In nearly every circumstance, the standard variable declaration
	 * approaches should be preferred over this method.
	 * 
	 * @param name   The name of the object which will either be defined
	 *               or overwritten
	 * @param object The object being stored at the given 'name'
	 *               location.
	 * @return The defined object
	 */
	public EnvisionObject forceDefine(String name, IDatatype type, Object object) {
		EnvisionObject toDefine = ObjectCreator.createObject(type, object, false, false);
		EnvisionObject existing = null;
		
		try {
			existing = working_scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (existing == null) working_scope.define(name, type, toDefine);
		else working_scope.set(name, type, toDefine);
		
		return toDefine;
	}
	
	/**
	 * This method will attempt to define a given object if it is found to
	 * not already exist within the current interpreter scope. In the
	 * event that a value under the same name is already defined, no
	 * further action will take place and this method will execute
	 * quietly.
	 * <p>
	 * This method will not attempt to check for inconsistent datatypes
	 * between existing and incomming values. Simply put, this method only
	 * checks if a value of the same 'name' is currently defined within the
	 * current interpreter scope or not. Because of this, it must be
	 * understood that just because a value under the same name exists
	 * within the current interpreter scope, it may not be the variable
	 * that was expected.
	 * 
	 * @param name   The name for which to define the given object on
	 * @param object The object to be defined if not already present
	 * @return The defined object
	 */
	public EnvisionObject defineIfNot(String name, EnvisionObject object) {
		EnvisionObject o = EnvisionNull.NULL;
		
		try {
			o = working_scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return working_scope.define(name, object);
		
		return o;
	}
	
	/**
	 * This method will attempt to define a given object if it is found to
	 * not already exist within the current interpreter scope. In the
	 * event that a value under the same name is already defined, no
	 * further action will take place and this method will execute
	 * quietly.
	 * <p>
	 * This method will not attempt to check for inconsistent datatypes
	 * between existing and incomming values. Simply put, this method only
	 * checks if a value of the same 'name' is currently defined within the
	 * current interpreter scope or not. Because of this, it must be
	 * understood that just because a value under the same name exists
	 * within the current interpreter scope, it may not be the variable
	 * that was expected.
	 * 
	 * @param name   The name for which to define the given object on
	 * @param object The object to be defined if not already present
	 * @return The defined object
	 */
	public EnvisionObject defineIfNot(String name, IDatatype type, EnvisionObject object) {
		EnvisionObject o = EnvisionNull.NULL;
		
		try {
			o = working_scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return working_scope.define(name, type, object);
		
		return o;
	}
	
	/**
	 * This method will attempt to update the value of an already existing
	 * variable under the same name with the given object. In the event
	 * that the object is not defined under the current interpreter scope,
	 * the given object will be defined in its place.
	 * <p>
	 * Note: This method will ignore inconsistencies between existing
	 * value datatypes and incomming datatypes. This means that the use of
	 * this method could potentially lead to destructive variable
	 * declarations such that existing variables under the same name, but
	 * with a different datatype, will be overwritten by the new incomming
	 * object.
	 * 
	 * @param name   The name for which to define the given object on
	 * @param object The object to be updated or defined if not already
	 *               present
	 * @return The defined object
	 */
	public EnvisionObject updateOrDefine(String name, EnvisionObject object) {
		EnvisionObject o = EnvisionNull.NULL;
		
		try {
			o = working_scope.get(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return working_scope.define(name, object);
		
		working_scope.set(name, object);
		return object;
	}
	
	/**
	 * This method will attempt to update the value of an already existing
	 * variable under the same name with the given object. In the event
	 * that the object is not defined under the current interpreter scope,
	 * the given object will be defined in its place.
	 * <p>
	 * Unlike the version of this method without the 'type' parameter,
	 * this version will specifically check to ensure that incomming
	 * object datatypes are consistent with already existing variable
	 * datatypes. In the event that the incomming datatype does not match,
	 * or is not an instance of (polymorphism), the existing variable
	 * datatype, an InvalidDatatypeError is thrown. However, in the case
	 * that the incomming object is 'NULL', then null (EnvisionNull) will
	 * be assigned to the existing variable.
	 * 
	 * @param name   The name for which to define the given object on
	 * @param object The object to be updated or defined if not already
	 *               present
	 * @return The defined object
	 */
	public EnvisionObject updateOrDefine(String name, IDatatype type, EnvisionObject object) {
		Box2<IDatatype, EnvisionObject> o = null;
		
		//return the typed variable (if it exists)
		try {
			o = working_scope.getTyped(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return working_scope.define(name, type, object);
		
		//check for datatype consistency
		if (!o.getA().equals(type))
			throw new InvalidDatatypeError("The incomming datatype: '" + type + "' does not match the existing" +
										   " datatype '" + o.getA() + "'!");
		
		working_scope.set(name, type, object);
		return object;
	}
	
	/**
	 * If a variable under the given name is defined within the current
	 * interpreter scope, then the variable is returned, otherwise
	 * Java:null is returned instead.
	 * 
	 * @param name The variable name for which to retrieve an object from
	 * @return The object if it is defined or null if it is not
	 */
	public EnvisionObject getIfDefined(String name) {
		EnvisionObject o = EnvisionNull.NULL;
		
		try {
			o = working_scope.get(name);
		} catch (UndefinedValueError e) {}
		
		return o;
	}
	
	//================
	// Static Methods
	//================
	
	/**
	 * Returns true if the given object is an EnvisionBoolean and the
	 * boolean value is true.
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isTrue(EnvisionObject obj) {
		if (obj instanceof EnvisionBoolean bool) return bool.bool_val;
		if (obj instanceof EnvisionInt env_int) return env_int.int_val != 0;
		return false;
	}
	
	/**
	 * Returns true if the given objects are equivalent in some regard.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual_i(EnvisionObject a, EnvisionObject b) {
		//do not allow null objects!
		if (a == null) throw new NullVariableError(a);
		if (b == null) throw new NullVariableError(b);
		
		//check for null objects
		if (a == EnvisionNull.NULL) return b == EnvisionNull.NULL;
		//check for class instances
		if (a instanceof ClassInstance a_inst) {
			//if b is a class instance -- check 'equals' for overrides
			if (b instanceof ClassInstance b_inst) return a_inst.executeEquals_i(this, b);
			//if b is not a class instance, then these cannot be equal
			return false;
		}
		//if a was not a class instance, return false if b is an instance
		else if (b instanceof ClassInstance b_inst) return false;
		//otherwise check for direct object equivalence
		return a.equals(b);
	}
	
	public static EnvisionBoolean isEqual(EnvisionObject a, EnvisionObject b) {
		return EnvisionBooleanClass.newBoolean(isEqual_i(a, b));
	}
	
	/**
	 * Returns true if the given object is either null by Java terms,
	 * or is an instance of an EnvisionNullObject by Envision terms.
	 * 
	 * @param in The object being compared to null
	 * @return true if the given object is in fact null
	 */
	protected static boolean isNull(EnvisionObject in) {
		return (in == null || in == EnvisionNull.NULL);
	}
	
	/**
	 * Returns true if the given datatype is either null by Java terms,
	 * or is equivalent to the EnvisionDataType.NULL by Envision
	 * terms.
	 * 
	 * @param typeIn The datatype being compared to null
	 * @return true if the given datatype is in fact null
	 */
	protected static boolean isNull(IDatatype typeIn) {
		return (typeIn == null || typeIn == StaticTypes.NULL_TYPE);
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
	protected static void assertNotNull(EnvisionObject in) {
		if (isNull(in)) throw new NullVariableError(in);
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
	
	@Override public EnvisionObject handleAssign_E(Expr_Assign e) { return IE_Assign.run(this, e); }
	@Override public EnvisionObject handleBinary_E(Expr_Binary e) { return IE_Binary.run(this, e); }
	@Override public EnvisionObject handleCast_E(Expr_Cast e) { return IE_Cast.run(this, e); }
	@Override public EnvisionObject handleCompound_E(Expr_Compound e) { return IE_Compound.run(this, e); }
	@Override public EnvisionObject handleDomain_E(Expr_Domain e) { return IE_Domain.run(this, e); }
	@Override public EnvisionObject handleEnum_E(Expr_Enum e) { return IE_Enum.run(this, e); }
	@Override public EnvisionObject handleGeneric_E(Expr_Generic e) { return IE_Generic.run(this, e); }
	@Override public EnvisionObject handleGet_E(Expr_Get e) { return IE_Get.run(this, e); }
	@Override public EnvisionObject handleGrouping_E(Expr_Grouping e) { return IE_Grouping.run(this, e); }
	@Override public EnvisionObject handleImport_E(Expr_Import e) { return IE_Import.run(this, e); }
	@Override public EnvisionObject handleLambda_E(Expr_Lambda e) { return IE_Lambda.run(this, e); }
	@Override public EnvisionObject handleListIndex_E(Expr_ListIndex e) { return IE_ListIndex.run(this, e); }
	@Override public EnvisionObject handleListInitializer_E(Expr_ListInitializer e) { return IE_ListInitializer.run(this, e); }
	@Override public EnvisionObject handleListIndexSet_E(Expr_SetListIndex e) { return IE_ListIndexSet.run(this, e); }
	@Override public EnvisionObject handleLiteral_E(Expr_Literal e) { return IE_Literal.run(this, e); }
	@Override public EnvisionObject handleLogical_E(Expr_Logic e) { return IE_Logical.run(this, e); }
	@Override public EnvisionObject handleMethodCall_E(Expr_FunctionCall e) { return IE_FunctionCall.run(this, e); }
	//@Override public EnvisionObject handleModular_E(ModularExpression e) { return IE_Modular.run(this, e); }
	@Override public EnvisionObject handlePrimitive_E(Expr_Primitive e) { return IE_Primitive.run(this, e); }
	@Override public EnvisionObject handleRange_E(Expr_Range e) { return IE_Range.run(this, e); }
	@Override public EnvisionObject handleSet_E(Expr_Set e) { return IE_Set.run(this, e); }
	@Override public EnvisionObject handleSuper_E(Expr_Super e) { return IE_Super.run(this, e); }
	@Override public EnvisionObject handleTernary_E(Expr_Ternary e) { return IE_Ternary.run(this, e); }
	@Override public EnvisionObject handleThisGet_E(Expr_This e) { return IE_This.run(this, e); }
	@Override public EnvisionObject handleTypeOf_E(Expr_TypeOf e) { return IE_TypeOf.run(this, e); }
	@Override public EnvisionObject handleUnary_E(Expr_Unary e) { return IE_Unary.run(this, e); }
	@Override public EnvisionObject handleVarDec_E(Expr_VarDef e) { return IE_VarDec.run(this, e); }
	@Override public EnvisionObject handleVar_E(Expr_Var e) { return IE_Var.run(this, e); }
	
}
