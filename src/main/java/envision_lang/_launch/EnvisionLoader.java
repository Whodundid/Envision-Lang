package envision_lang._launch;

/** Loads the language ahead of time to speed up program execution times. */
public class EnvisionLoader {
	
	private static ClassLoader loader = EnvisionLoader.class.getClassLoader();
	private static String curBasePath = "envision_lang.";
	
	private static void d(String dir) {
		curBasePath = "envision_lang." + dir;
	}
	
	private static void l(String path) throws ClassNotFoundException {
		loader.loadClass(curBasePath + path);
	}
	
	public static void loadLang() throws ClassNotFoundException {
		d("");
		l("EnvisionLang");
		
		d("_launch.");
		l("EnvisionCodeFile");
		l("EnvisionConsoleHandler");
		l("EnvisionConsoleOutputReceiver");
		l("EnvisionLangErrorCallBack");
		l("EnvisionLaunchSettings");
		l("EnvisionProgram");
		l("WorkingDirectory");
		
		d("debug.");
		l("DebugParserPrinter");
		l("DebugTokenPrinter");
		
		d("interpreter.");
		l("AbstractInterpreterExecutor");
		l("EnvisionInterpreter");
		
		d("interpreter.expressions.");
		l("IE_Assign");
		l("IE_Binary");
		l("IE_Compound");
		l("IE_FunctionCall");
		l("IE_Get");
		l("IE_Import");
		l("IE_Lambda");
		l("IE_ListIndex");
		l("IE_ListIndexSet");
		l("IE_ListInitializer");
		l("IE_Literal");
		l("IE_Logical");
		l("IE_Primitive");
		l("IE_Range");
		l("IE_Set");
		l("IE_Ternary");
		l("IE_This");
		l("IE_TypeOf");
		l("IE_Unary");
		l("IE_Var");
		l("IE_VarDec");
		
		d("interpreter.statements.");
		l("IS_Block");
		l("IS_Case");
		l("IS_Catch");
		l("IS_Class");
		l("IS_Exception");
		l("IS_Expression");
		l("IS_For");
		l("IS_FuncDef");
		l("IS_If");
		l("IS_Import");
		l("IS_LambdaFor");
		l("IS_LoopControl");
		l("IS_RangeFor");
		l("IS_Return");
		l("IS_Switch");
		l("IS_Try");
		l("IS_VarDec");
		l("IS_While");
		
		d("interpreter.util.");
		l("CastingUtil");
		l("EnvisionStringFormatter");
		l("OperatorOverloadHandler");
		l("UserDefinedTypeManager");
		
		d("interpreter.util.creationUtil.");
		l("FunctionCreator");
		l("ObjectCreator");
		
		d("interpreter.util.scope.");
		l("IScope");
		l("Scope");
		l("ScopeEntry");
		
		d("interpreter.util.throwables.");
		l("Break");
		l("Continue");
		l("InternalException");
		l("LangShutdownCall");
		l("ReturnValue");
		
		d("lang.");
		l("EnvisionObject");
		
		d("lang.classes.");
		l("ClassConstruct");
		l("ClassInstance");
		l("EnvisionClass");
		
		d("lang.datatypes.");
		l("EnvisionBoolean");
		l("EnvisionBooleanClass");
		l("EnvisionChar");
		l("EnvisionCharClass");
		l("EnvisionDouble");
		l("EnvisionDoubleClass");
		l("EnvisionInt");
		l("EnvisionIntClass");
		l("EnvisionList");
		l("EnvisionListClass");
		l("EnvisionNull");
		l("EnvisionNumber");
		l("EnvisionNumberClass");
		l("EnvisionString");
		l("EnvisionStringClass");
		l("EnvisionTuple");
		l("EnvisionTupleClass");
		l("EnvisionVariable");
		l("EnvisionVoid");
		
		d("lang.functions.");
		l("EnvisionFunction");
		l("EnvisionFunctionClass");
		l("FunctionPrototype");
		l("InstanceFunction");
		l("IPrototype");
		l("IPrototypeHandler");
		
		d("lang.java.");
		l("BridgeVariable");
		l("EnvisionBridge");
		l("EnvisionCodeBlock");
		l("EnvisionJavaClass");
		l("EnvisionJavaObject");
		l("JavaScope");
		l("NativeClassInstance");
		l("NativeFunctionWrapper");
		
		d("lang.natives.");
		l("DataModifier");
		l("DataModifierHandler");
		l("EnvisionDatatype");
		l("EnvisionParameter");
		l("EnvisionStaticTypes");
		l("EnvisionVisibilityModifier");
		l("IDatatype");
		l("InternalJavaObjectWrapper");
		l("NativeTypeManager");
		l("ParameterData");
		l("Primitives");
		
		d("lang.packages.");
		l("Buildable");
		l("EnvisionLangPackage");
		
		d("lang.packages.native_packages.");
		l("DebugPackage");
		l("EnvPackage");
		l("FilePackage");
		l("ImagePackage");
		l("IOPackage");
		l("MathPackage");
		l("NativePackage");
		
		d("lang.packages.native_packages.base.");
		l("InternalEnvision");
		l("InternalEnvisionClass");
		l("Millis");
		l("Nanos");
		l("Sleep");
		l("SupportsOP");
		
		d("lang.packages.native_packages.debug.");
		l("DebugInfo");
		l("DebugParsed");
		l("DebugScope");
		l("DebugScopeFull");
		
		d("lang.packages.native_packages.file.");
		l("EnvisionFile");
		l("EnvisionFileClass");
		
		d("lang.packages.native_packages.image.");
		l("EnvisionImage");
		l("EnvisionImageClass");
		
		d("lang.packages.native_packages.io.");
		l("Print");
		l("Printf");
		l("Println");
		l("Read");
		
		d("lang.packages.native_packages.math.");
		l("Ceil");
		l("Floor");
		l("Log");
		l("Pow");
		l("RandDouble");
		l("RandInt");
		l("RandName");
		l("RandStr");
		l("Sqrt");
		
		d("parser.");
		l("EnvisionLangParser");
		l("ParserHead");
		l("ParsingError");
		
		d("parser.expressions.");
		l("ExpressionHandler");
		l("ExpressionParser");
		l("ParsedExpression");
		
		d("parser.expressions.expression_types.");
		l("Expr_Assign");
		l("Expr_Binary");
		l("Expr_Compound");
		l("Expr_FunctionCall");
		l("Expr_Get");
		l("Expr_Import");
		l("Expr_Lambda");
		l("Expr_ListIndex");
		l("Expr_ListInitializer");
		l("Expr_Literal");
		l("Expr_Logic");
		l("Expr_Primitive");
		l("Expr_Range");
		l("Expr_Set");
		l("Expr_SetListIndex");
		l("Expr_Ternary");
		l("Expr_This");
		l("Expr_TypeOf");
		l("Expr_Unary");
		l("Expr_Var");
		l("Expr_VarDef");
		
		d("parser.statements.");
		l("ParsedStatement");
		l("StatementHandler");
		
		d("parser.statements.statement_types.");
		l("Stmt_Block");
		l("Stmt_Catch");
		l("Stmt_Class");
		l("Stmt_Exception");
		l("Stmt_For");
		l("Stmt_FuncDef");
		l("Stmt_Generic");
		l("Stmt_If");
		l("Stmt_Import");
		l("Stmt_LambdaFor");
		l("Stmt_LoopControl");
		l("Stmt_RangeFor");
		l("Stmt_Return");
		l("Stmt_SwitchCase");
		l("Stmt_SwitchDef");
		l("Stmt_Try");
		l("Stmt_VarDef");
		l("Stmt_While");
		
		d("parser.statements.statementParsers.");
		l("PS_Class");
		l("PS_For");
		l("PS_Function");
		l("PS_If");
		l("PS_Import");
		l("PS_LoopControl");
		l("PS_ParseDeclaration");
		l("PS_Return");
		l("PS_Switch");
		l("PS_Try");
		l("PS_VarDef");
		l("PS_While");
		
		d("parser.util.");
		l("DeclarationStage");
		l("DeclarationType");
		l("ParsedObject");
		l("ParserDeclaration");
		l("StatementParameter");
		l("VariableDeclaration");
		
		d("tokenizer.");
		l("EscapeCode");
		l("IKeyword");
		l("KeywordType");
		l("Operator");
		l("ReservedWord");
		l("Token");
		l("Tokenizer");
	}
	
}
