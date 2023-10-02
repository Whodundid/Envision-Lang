package envision_lang.interpreter;

import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.WorkingDirectory;
import envision_lang.interpreter.expressions.IE_Assign;
import envision_lang.interpreter.expressions.IE_Binary;
import envision_lang.interpreter.expressions.IE_Compound;
import envision_lang.interpreter.expressions.IE_FunctionCall;
import envision_lang.interpreter.expressions.IE_Get;
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
import envision_lang.interpreter.statements.IS_Exception;
import envision_lang.interpreter.statements.IS_Expression;
import envision_lang.interpreter.statements.IS_For;
import envision_lang.interpreter.statements.IS_FuncDef;
import envision_lang.interpreter.statements.IS_If;
import envision_lang.interpreter.statements.IS_Import;
import envision_lang.interpreter.statements.IS_LambdaFor;
import envision_lang.interpreter.statements.IS_LoopControl;
import envision_lang.interpreter.statements.IS_RangeFor;
import envision_lang.interpreter.statements.IS_Return;
import envision_lang.interpreter.statements.IS_Switch;
import envision_lang.interpreter.statements.IS_Try;
import envision_lang.interpreter.statements.IS_VarDec;
import envision_lang.interpreter.statements.IS_While;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.interpreter.util.throwables.LangShutdownCall;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.java.EnvisionJavaObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.ExpressionError;
import envision_lang.lang.language_errors.error_types.InvalidDatatypeError;
import envision_lang.lang.language_errors.error_types.NullVariableError;
import envision_lang.lang.language_errors.error_types.StatementError;
import envision_lang.lang.language_errors.error_types.UndefinedValueError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.UserDefinedTypeManager;
import envision_lang.lang.packages.native_packages.EnvPackage;
import envision_lang.lang.packages.native_packages.base.InternalEnvision;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_FunctionCall;
import envision_lang.parser.expressions.expression_types.Expr_Get;
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
import envision_lang.parser.statements.statement_types.Stmt_Exception;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.statements.statement_types.Stmt_For;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.statements.statement_types.Stmt_If;
import envision_lang.parser.statements.statement_types.Stmt_Import;
import envision_lang.parser.statements.statement_types.Stmt_LambdaFor;
import envision_lang.parser.statements.statement_types.Stmt_LoopControl;
import envision_lang.parser.statements.statement_types.Stmt_RangeFor;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import envision_lang.parser.statements.statement_types.Stmt_SwitchCase;
import envision_lang.parser.statements.statement_types.Stmt_SwitchDef;
import envision_lang.parser.statements.statement_types.Stmt_Try;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.statements.statement_types.Stmt_While;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/**
 * The primary class responsible for executing parsed Envision script
 * files.
 * 
 * @author Hunter Bragg
 */
public class EnvisionInterpreter implements StatementHandler, ExpressionHandler {
	
	//--------
	// Fields
	//--------
	
	/**
	 * The initial (and primary) working directory for which all Envision code executions
	 * will have originated from.
	 */
	private static WorkingDirectory topDir;
	
	/**
	 * Lowest scope level intended exclusively for language packages and internal functions.
	 */
	private final IScope internalScope = new Scope();
	
	/**
	 * The scope intended for actual user program execution.
	 */
	private final IScope programScope = new Scope(internalScope);
	
	/**
	 * The primary working interpreter scope.
	 */
	IScope working_scope = programScope;
	
	/**
	 * The active working file directory for which this interpreter has been created
	 * from. This working directory contains all of the related codeFiles enclosed
	 * within the given directory.
	 */
	private WorkingDirectory active_dir;
	
	/**
	 * The specific codeFile that this interpreter was created from.
	 */
	private EnvisionCodeFile startingFile;
	
	/**
	 * The name of the file that this interpreter was created from.
	 */
	public final String fileName;
	
	/**
	 * Keeps track and manages all user-defined datatypes during instance creation
	 * for any object that is created within this interpreter.
	 * <p>
	 * A user-defined type is any datatype (class) that has been defined by Envision
	 * code executed at runtime. This includes any types defined by any internal or
	 * user-defined EnvisionPackages.
	 */
	private final UserDefinedTypeManager typeManager = new UserDefinedTypeManager();
	
	private boolean hasError = false;
	private Exception errorObject;
	private int statementIndex = -1;
	private int lastLineNum = -1;
	private Token<?> lastLineToken = null;
	
	private volatile boolean runNext;
	private boolean advancePassedBlock;
	
	private EList<ParsedStatement> statements;
	private EList<StackFrame> frames = EList.newList();
	//private EList<ParsedStatement> statementStack = EList.newList();
	
	public static class StackFrame {
		public EList<ParsedStatement> statementStack = EList.newList();
		
		public StackFrame(EList<ParsedStatement> in) {
			statementStack = EList.of(in);
		}
		
		@Override public String toString() { return "" + statementStack; }
	}
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionInterpreter(EnvisionCodeFile codeFileIn) {
		startingFile = codeFileIn;
		fileName = startingFile.getFileName();
		
		WorkingDirectory dirIn = startingFile.getWorkingDir();
		
		// it may be a bad idea to do this... too bad!
		internalScope.values().putAll(codeFileIn.scope().values());
		
		if (topDir == null) topDir = dirIn;
		active_dir = dirIn;
		
		EnvPackage.ENV_PACKAGE.defineOn(this);
	}
	
	public void setup() {
	    setupWithUserArguments(EList.newList());
	}
	
	public void setupWithUserArguments(EList<String> userArgs) {
		InternalEnvision.init(EnvisionLang.getInstance(), internalScope, userArgs);
		statements = startingFile.getStatements();
		if (statements.isNotEmpty()) {
			frames.push(new StackFrame(statements));			
		}
		hasError = false;
	}
	
	public void injectJavaObject(String asName, Object objectToInject) {
	    EnvisionJavaObject wrapped;
	    
	    if (objectToInject instanceof EnvisionJavaObject ejo) wrapped = ejo;
	    else wrapped = EnvisionJavaObject.wrapJavaObject(this, objectToInject);
	    
	    scope().define(asName, wrapped);
	}
	
	public void executeNext() {
		if (hasError) {
			//this could error so this should probably be re-thought
			System.out.println("(" + startingFile.getSystemFile() + ") error at: " + statements.get(statementIndex));
			System.out.println(errorObject);
			return;
		}
		
		try {
			executeNext_i();
		}
		//exit silently on shutdown call
		catch (LangShutdownCall shutdownCall) {}
		//report error on statement execution error
		catch (Exception error) {
			hasError = true;
			errorObject = error;
			//such professional error handler
			System.out.println("(" + startingFile.getSystemFile() + ") error at: " + frames.getFirst());
			printStackFrames();
			//error.printStackTrace();
			throw error;
		}
	}
	
	public void executeNext(ParsedStatement s) {
		if (s == null) return;
		else if (s instanceof Stmt_Block b) this.executeBlock(b, working_scope);
		else {
			frames.push(new StackFrame(EList.of(s)));
		}
	}
	
	private void executeNext_i() {
		if (frames.isEmpty()) return;
		
		// set this flag to true whenever we enter this method so that the next
		// instruction will be run even if it is a blocking instruction
		advancePassedBlock = true;
		runNext = true;
		
		final var curFrame = frames.peek();
		final var frameStatements = curFrame.statementStack;
		final boolean isLastStatement = hasOnlyOneInstructionLeft();
		
		while (frameStatements.isNotEmpty()) {
			ParsedStatement s = frameStatements.peek();
			
			if (EnvisionLang.debugMode) {
				printStackFrames();
				//System.out.println(IScope.printFullStack(working_scope));
			}
			
			// error on NULL statements (this really shouldn't be possible!)
			if (s == null) throw new EnvisionLangError("NULL statement detected! Interpreting aborted!");
			
			execute(s);
			
			// pop off the statement from the stack frame
			frameStatements.pop();
			
			// break out on blocking statements when not explicitly told to advance
			
			if (EnvisionLang.enableBlockingStatements && (!runNext || s.isBlockingStatement())) {
				if (!isLastStatement) throw new LangShutdownCall();
			}
			
			// reset state
			advancePassedBlock = false;
		}
		
		// pop off the stack frame
		frames.pop();
	}
	
	public boolean hasError() { return hasError; }
	private boolean hasOnlyOneInstructionLeft() {
		return (frames.size() == 1) && (frames.get(0).statementStack.size() == 1);
	}
	
	//---------------------------------------------------------------------------------
	
	public static EnvisionInterpreter build(EnvisionCodeFile codeFileIn, EList<String> userArgs) throws Exception {
		var interpreter = new EnvisionInterpreter(codeFileIn);
		
		EnvPackage.ENV_PACKAGE.defineOn(interpreter);
		InternalEnvision.init(EnvisionLang.getInstance(), interpreter.internalScope, userArgs);
		interpreter.setupWithUserArguments(userArgs);
		
		return interpreter;
	}
	
	public static EnvisionInterpreter interpret(EnvisionCodeFile codeFileIn, EList<String> userArgs) throws Exception {
		var interpreter = new EnvisionInterpreter(codeFileIn);
		interpreter.setupWithUserArguments(userArgs);
		interpreter.executeNext();
		return interpreter;
	}
	
	//---------------------
	// Interpreter Methods
	//---------------------
	
	public void terminate() {
		frames.clear();
	}
	
	public boolean hasNext() {
		return frames.isNotEmpty();
	}
	
	public void pauseExecution() {
		runNext = false;
	}
	
	public StackFrame getCurrentStackFrame() { return frames.getFirst(); }
	public EList<StackFrame> getStackFrames() { return frames; }
	public int getFrameSize() { return frames.size(); }
	public StackFrame popStackFrame() { return frames.pop(); }
	
	/**
	 * Attempts to execute the given statement.
	 * Note: if the statement is null, a ParsedStatementError will be thrown instead.
	 * 
	 * @param s The statement to be executed
	 */
	public void execute(ParsedStatement s) {
		if (s == null) throw new StatementError("The given statement is null!");
		s.execute(this);
	}
	
	public void printStackFrames() {
		System.out.println("\nSTACK FRAMES:");
		final int fsize = frames.size();
		for (int i = 0; i < fsize; i++) {
			System.out.println("\t" + (fsize - i - 1) + " FRAME:");
			
			var s = frames.get(i).statementStack;
			final int size = s.size();
			
			for (int j = 0; j < size; j++) {
				System.out.println("\t\t" + (size - j - 1) + ": '" + s.get(j) + "'");
			}
			
		}
		System.out.println();
	}
	
	/**
	 * Attempts to evaluate the given expression.
	 * Note: if the expression is null, an ExpressionError will be thrown instead.
	 * 
	 * @param e The expression to be evaluted
	 * @return The result of the given expression in the form of an EnvisionObject
	 */
	public EnvisionObject evaluate(ParsedExpression e) {
		if (e == null) throw new ExpressionError("The given expression is null!");
		return e.evaluate(this);
	}
	
	/**
	 * Executes a series of given statements under the specific scope. The
	 * original scope is momentarily swapped out for the incoming scope.
	 * Regardless of execution sucess, the original interpreter scope will be
	 * restored once this method returns.
	 * 
	 * @param statements The list of statements to be executed
	 * @param scopeIn    The scope for which to execute the statements on
	 */
	public void executeBlock(Stmt_Block block, IScope scopeIn) {
		IScope prev = working_scope;
		try {
			working_scope = scopeIn;
			
			final var statements = block.statements;
			frames.push(new StackFrame(statements));
			
			executeNext_i();
		}
		finally {
			working_scope = prev;
		}
	}
	
	/**
	 * Executes a series of given statements under the specific scope. The
	 * original scope is momentarily swapped out for the incoming scope.
	 * Regardless of execution sucess, the original interpreter scope will be
	 * restored once this method returns.
	 * 
	 * @param statements The list of statements to be executed
	 * @param scopeIn    The scope for which to execute the statements on
	 */
	public void executeStatements(EList<ParsedStatement> statements, IScope scopeIn) {
		IScope prev = working_scope;
		try {
			working_scope = scopeIn;
			
			final int size = statements.size();
			for (int i = 0; i < size; i++) {
				final var s = statements.get(i);
				execute(s);
			}
		}
		finally {
			working_scope = prev;
		}
	}
	
	/**
	 * This method simply serves as a wrapper around a block execution but
	 * specifically indicates that there may potentially be a return value
	 * thrown from the resulting execution.
	 * 
	 * @param statements The list of statements to be executed
	 * @param scopeIn The scope for which to execute the statements on
	 */
	public void executeBlockForReturns(Stmt_Block block, IScope scopeIn) {
		try {
			executeBlock(block, scopeIn);
		}
		catch (ReturnValue r) {
			throw r;
		}
	}
	
	/**
	 * This method simply serves as a wrapper around a block execution but
	 * specifically indicates that there may potentially be a return value
	 * thrown from the resulting execution.
	 * 
	 * @param statements The list of statements to be executed
	 * @param scopeIn The scope for which to execute the statements on
	 */
	public void executeStatementsForReturns(EList<ParsedStatement> statements, IScope scopeIn) {
		try {
			executeStatements(statements, scopeIn);
		}
		catch (ReturnValue r) {
			throw r;
		}
	}
	
	/**
	 * Returns true if the given object is an EnvisionBoolean and the
	 * boolean value is true.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isTrue(EnvisionObject obj) {
		if (obj instanceof EnvisionBoolean bool) return bool.get_i();
		if (obj instanceof EnvisionInt env_int) return env_int.get_i() != 0L;
		return false;
	}
	
	/**
	 * Returns true if the given objects are equivalent in some regard.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isEqual_i(EnvisionObject a, EnvisionObject b) {
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
	
	public EnvisionBoolean isEqual(EnvisionObject a, EnvisionObject b) {
		return EnvisionBooleanClass.valueOf(isEqual_i(a, b));
	}
	
	/**
	 * Returns the active working file directory for which this
	 * interpreter has been created from. This working directory contains
	 * all of the related codeFiles enclosed within the given directory.
	 * 
	 * @return The working directory of this interpreter
	 */
	public WorkingDirectory workingDir() { return active_dir; }
	
	/**
	 * Returns the initial (and primary) working directory for which all Envision
	 * code executions will have originated from.
	 * 
	 * @return The initial, top-level working directory
	 */
	public static WorkingDirectory topDir() { return topDir; }
	
	/**
	 * Returns the specific codeFile that this interpreter was created from.
	 * @return The codeFile for this interpreter
	 */
	public EnvisionCodeFile codeFile() { return startingFile; }
	
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
	 * Returns the TypeManager for this interpreter. The type manager
	 * actively keeps track of all user-defined class types that have been
	 * defined within this interpreter's scope of execution. User-defined
	 * types that are defined outside of this interpreter will need to be
	 * imported in order to be recognized.
	 * 
	 * @return The TypeManager for this interpreter
	 */
	public UserDefinedTypeManager getTypeManager() { return typeManager; }
	
	//----------------------------
	// Interpreter Helper Methods
	//----------------------------
	
	public IScope pushScope() { return working_scope = new Scope(working_scope); }
	public IScope popScope() { return working_scope = working_scope.getParent(); }
	
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
		else working_scope.set(name, toDefine);
		
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
		ScopeEntry o = null;
		
		//return the typed variable (if it exists)
		try {
			o = working_scope.getTyped(name);
		} catch (UndefinedValueError e) {}
		
		if (o == null) return working_scope.define(name, type, object);
		
		//check for datatype consistency
		if (!o.getDatatype().equals(type))
			throw new InvalidDatatypeError("The incomming datatype: '" + type + "' does not match the existing" +
										   " datatype '" + o.getDatatype() + "'!");
		
		working_scope.set(name, object);
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
	
	//------------
	// Statements
	//------------
	
	@Override public void handleBlockStatement(Stmt_Block s) { IS_Block.run(this, s); }
	@Override public void handleLoopControlStatement(Stmt_LoopControl s) { IS_LoopControl.run(this, s); }
	@Override public void handleCatchStatement(Stmt_Catch s) { IS_Catch.run(this, s); }
	@Override public void handleCaseStatement(Stmt_SwitchCase s) { IS_Case.run(this, s); }
	@Override public void handleClassStatement(Stmt_Class s) { IS_Class.run(this, s); }
//	@Override public void handleEnumStatement(Stmt_EnumDef s) { IS_Enum.run(this, s); }
	@Override public void handleExceptionStatement(Stmt_Exception s) { IS_Exception.run(this, s); }
	@Override public void handleExpressionStatement(Stmt_Expression s) { IS_Expression.run(this, s); }
	@Override public void handleForStatement(Stmt_For s) { IS_For.run(this, s); }
//	@Override public void handleGenericStatement(Stmt_Generic s) { IS_Generic.run(this, s); }
//	@Override public void handleGetSetStatement(Stmt_GetSet s) { IS_GetSet.run(this, s); }
	@Override public void handleIfStatement(Stmt_If s) { IS_If.run(this, s); }
	@Override public void handleImportStatement(Stmt_Import s) { IS_Import.run(this, s); }
//	@Override public void handleInterfaceStatement(Stmt_InterfaceDef s) {}
	@Override public void handleLambdaForStatement(Stmt_LambdaFor s) { IS_LambdaFor.run(this, s); }
	@Override public void handleMethodStatement(Stmt_FuncDef s) { IS_FuncDef.run(this, s); }
//	@Override public void handlePackageStatement(Stmt_Package s) { IS_Package.run(this, s); }
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
//	@Override public EnvisionObject handleCast_E(Expr_Cast e) { return IE_Cast.run(this, e); }
	@Override public EnvisionObject handleCompound_E(Expr_Compound e) { return IE_Compound.run(this, e); }
//	@Override public EnvisionObject handleDomain_E(Expr_Domain e) { return IE_Domain.run(this, e); }
//	@Override public EnvisionObject handleEnum_E(Expr_Enum e) { return IE_Enum.run(this, e); }
//	@Override public EnvisionObject handleGeneric_E(Expr_Generic e) { return IE_Generic.run(this, e); }
	@Override public EnvisionObject handleGet_E(Expr_Get e) { return IE_Get.run(this, e); }
//	@Override public EnvisionObject handleGrouping_E(Expr_Grouping e) { return IE_Grouping.run(this, e); }
	@Override public EnvisionObject handleImport_E(Expr_Import e) { return IE_Import.run(this, e); }
	@Override public EnvisionObject handleLambda_E(Expr_Lambda e) { return IE_Lambda.run(this, e); }
	@Override public EnvisionObject handleListIndex_E(Expr_ListIndex e) { return IE_ListIndex.run(this, e); }
	@Override public EnvisionObject handleListInitializer_E(Expr_ListInitializer e) { return IE_ListInitializer.run(this, e); }
	@Override public EnvisionObject handleListIndexSet_E(Expr_SetListIndex e) { return IE_ListIndexSet.run(this, e); }
	@Override public EnvisionObject handleLiteral_E(Expr_Literal e) { return IE_Literal.run(this, e); }
	@Override public EnvisionObject handleLogical_E(Expr_Logic e) { return IE_Logical.run(this, e); }
	@Override public EnvisionObject handleMethodCall_E(Expr_FunctionCall e) { return IE_FunctionCall.run(this, e); }
	@Override public EnvisionObject handlePrimitive_E(Expr_Primitive e) { return IE_Primitive.run(this, e); }
	@Override public EnvisionObject handleRange_E(Expr_Range e) { return IE_Range.run(this, e); }
	@Override public EnvisionObject handleSet_E(Expr_Set e) { return IE_Set.run(this, e); }
//	@Override public EnvisionObject handleSuper_E(Expr_Super e) { return IE_Super.run(this, e); }
	@Override public EnvisionObject handleTernary_E(Expr_Ternary e) { return IE_Ternary.run(this, e); }
	@Override public EnvisionObject handleThisGet_E(Expr_This e) { return IE_This.run(this, e); }
	@Override public EnvisionObject handleTypeOf_E(Expr_TypeOf e) { return IE_TypeOf.run(this, e); }
	@Override public EnvisionObject handleUnary_E(Expr_Unary e) { return IE_Unary.run(this, e); }
	@Override public EnvisionObject handleVarDec_E(Expr_VarDef e) { return IE_VarDec.run(this, e); }
	@Override public EnvisionObject handleVar_E(Expr_Var e) { return IE_Var.run(this, e); }
	
}
