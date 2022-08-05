package envision_lang._launch;

/** Loads the language ahead of time to speed up program execution times. */
public class EnvisionLoader {
	
	public static void loadLang() throws ClassNotFoundException {
		ClassLoader loader = EnvisionLoader.class.getClassLoader();
		
		//for (int i = 0; i < 1; i++) {
			//Object util
			loader.loadClass("envision.lang.util.EnvisionDataType");
			loader.loadClass("envision.lang.util.VisibilityType");
			loader.loadClass("envision.lang.util.data.DataModifier");
			loader.loadClass("envision.lang.util.data.DataValue");
			loader.loadClass("envision.lang.util.data.Parameter");
			loader.loadClass("envision.lang.util.data.ParameterData");
			loader.loadClass("envision.lang.util.InternalMethod");
			loader.loadClass("envision.lang.util.InternalConstructor"); //deprecated
			loader.loadClass("envision.lang.util.structureTypes.CallableObject");
			loader.loadClass("envision.lang.util.structureTypes.InheritableObject");
			loader.loadClass("envision.lang.util.structureTypes.InstantiableObject");
			//Object base
			loader.loadClass("envision.lang.EnvisionObject");
			//Variables
			loader.loadClass("envision.lang.datatypes.EnvisionVariable");
			loader.loadClass("envision.lang.datatypes.EnvisionNumber");
			loader.loadClass("envision.lang.datatypes.EnvisionDouble");
			loader.loadClass("envision.lang.datatypes.EnvisionInt");
			loader.loadClass("envision.lang.datatypes.EnvisionBoolean");
			loader.loadClass("envision.lang.datatypes.EnvisionChar");
			loader.loadClass("envision.lang.datatypes.EnvisionString");
			//Classes
			loader.loadClass("envision.lang.classes.EnvisionClass");
			loader.loadClass("envision.lang.classes.ClassInstance");
			//Enums
			loader.loadClass("envision.lang.enums.EnvisionEnum");
			loader.loadClass("envision.lang.enums.EnumValue");
			loader.loadClass("envision.lang.enums.EnvisionEnumConstructor");
			//objects
			loader.loadClass("envision.lang.objects.EnvisionFunction");
			loader.loadClass("envision.lang.objects.EnvisionNullObject");
			loader.loadClass("envision.lang.objects.EnvisionVoidObject");
			loader.loadClass("envision.lang.objects.EnvisionOperator");
			loader.loadClass("envision.lang.objects.EnvisionList");
			loader.loadClass("envision.lang.objects.EnvisionInterface");
			//packages
			loader.loadClass("envision.lang.packages.EnvisionPackage");
			loader.loadClass("envision.lang.packages.EnvisionLangPackage");
			loader.loadClass("envision.lang.packages.EnvisionDefaultPackages");
			loader.loadClass("envision.lang.packages.env.EnvPackage");
			loader.loadClass("envision.lang.packages.env.base.Millis");
			loader.loadClass("envision.lang.packages.env.base.Nanos");
			loader.loadClass("envision.lang.packages.env.base.Sleep");
			loader.loadClass("envision.lang.packages.env.base.SupportsOP");
			loader.loadClass("envision.lang.packages.env.debug.DebugInfo");
			loader.loadClass("envision.lang.packages.env.debug.DebugParsed");
			loader.loadClass("envision.lang.packages.env.debug.DebugScope");
			loader.loadClass("envision.lang.packages.env.debug.DebugPackage");
			loader.loadClass("envision.lang.packages.env.file.EnvFile");
			loader.loadClass("envision.lang.packages.env.file.FilePackage");
			loader.loadClass("envision.lang.packages.env.io.Print");
			loader.loadClass("envision.lang.packages.env.io.Println");
			loader.loadClass("envision.lang.packages.env.io.Read");
			loader.loadClass("envision.lang.packages.env.io.IOPackage");
			loader.loadClass("envision.lang.packages.env.math.Ceil");
			loader.loadClass("envision.lang.packages.env.math.Floor");
			loader.loadClass("envision.lang.packages.env.math.Log");
			loader.loadClass("envision.lang.packages.env.math.Pow");
			loader.loadClass("envision.lang.packages.env.math.RandDouble");
			loader.loadClass("envision.lang.packages.env.math.RandInt");
			loader.loadClass("envision.lang.packages.env.math.RandName");
			loader.loadClass("envision.lang.packages.env.math.RandStr");
			loader.loadClass("envision.lang.packages.env.math.Sqrt");
			loader.loadClass("envision.lang.packages.env.math.MathPackage");
			//Interpreter - creation
			loader.loadClass("envision.interpreter.util.scope.Scope");
			loader.loadClass("envision.interpreter.util.TypeManager");
			loader.loadClass("envision.interpreter.util.EnvisionStringFormatter");
			loader.loadClass("envision.interpreter.util.creationUtil.ObjectCreator");
			loader.loadClass("envision.interpreter.util.creationUtil.VariableCreator");
			loader.loadClass("envision.interpreter.util.CastingUtil");
			loader.loadClass("envision.interpreter.util.creationUtil.MethodCreator");
			loader.loadClass("envision.interpreter.util.creationUtil.VariableUtil");
			loader.loadClass("envision.interpreter.util.creationUtil.OperatorOverloadHandler");
			//Interpreter - throwables
			loader.loadClass("envision.interpreter.util.throwables.ReturnValue");
			loader.loadClass("envision.interpreter.util.throwables.EnvisionException");
			loader.loadClass("envision.interpreter.util.throwables.Exception_Class");
			loader.loadClass("envision.interpreter.util.throwables.Break");
			loader.loadClass("envision.interpreter.util.throwables.Continue");
			//Interpreter - Base
			loader.loadClass("envision.interpreter.util.interpreterBase.ExpressionExecutor");
			loader.loadClass("envision.interpreter.util.interpreterBase.StatementExecutor");
			loader.loadClass("envision.interpreter.util.interpreterBase.InterpreterExecutor");
			//Interpreter
			loader.loadClass("envision.interpreter.EnvisionInterpreter");
			//Interpreter - Statements
			loader.loadClass("envision.interpreter.statements.IS_Block");
			loader.loadClass("envision.interpreter.statements.IS_Case");
			loader.loadClass("envision.interpreter.statements.IS_Catch");
			loader.loadClass("envision.interpreter.statements.IS_Enum");
			loader.loadClass("envision.interpreter.statements.IS_Exception");
			loader.loadClass("envision.interpreter.statements.IS_Expression");
			loader.loadClass("envision.interpreter.statements.IS_For");
			loader.loadClass("envision.interpreter.statements.IS_Generic");
			loader.loadClass("envision.interpreter.statements.IS_GetSet");
			loader.loadClass("envision.interpreter.statements.IS_If");
			loader.loadClass("envision.interpreter.statements.IS_Import");
			loader.loadClass("envision.interpreter.statements.IS_LambdaFor");
			loader.loadClass("envision.interpreter.statements.IS_LoopControl");
			loader.loadClass("envision.interpreter.statements.IS_MethodDeclaration");
			loader.loadClass("envision.interpreter.statements.IS_ModularMethod");
			loader.loadClass("envision.interpreter.statements.IS_Package");
			loader.loadClass("envision.interpreter.statements.IS_RangeFor");
			loader.loadClass("envision.interpreter.statements.IS_Return");
			loader.loadClass("envision.interpreter.statements.IS_Switch");
			loader.loadClass("envision.interpreter.statements.IS_Try");
			loader.loadClass("envision.interpreter.statements.IS_VarDec");
			loader.loadClass("envision.interpreter.statements.IS_While");
			//Interpreter - Expressions
			loader.loadClass("envision.interpreter.expressions.IE_Assign");
			loader.loadClass("envision.interpreter.expressions.IE_Binary");
			loader.loadClass("envision.interpreter.expressions.IE_Compound");
			loader.loadClass("envision.interpreter.expressions.IE_Domain");
			loader.loadClass("envision.interpreter.expressions.IE_Enum");
			loader.loadClass("envision.interpreter.expressions.IE_Generic");
			loader.loadClass("envision.interpreter.expressions.IE_Get");
			loader.loadClass("envision.interpreter.expressions.IE_Grouping");
			loader.loadClass("envision.interpreter.expressions.IE_Import");
			loader.loadClass("envision.interpreter.expressions.IE_Lambda");
			loader.loadClass("envision.interpreter.expressions.IE_ListIndex");
			loader.loadClass("envision.interpreter.expressions.IE_ListIndexSet");
			loader.loadClass("envision.interpreter.expressions.IE_ListInitializer");
			loader.loadClass("envision.interpreter.expressions.IE_Literal");
			loader.loadClass("envision.interpreter.expressions.IE_Logical");
			loader.loadClass("envision.interpreter.expressions.IE_MethodCall");
			loader.loadClass("envision.interpreter.expressions.IE_MethodDec");
			loader.loadClass("envision.interpreter.expressions.IE_Modular");
			loader.loadClass("envision.interpreter.expressions.IE_Range");
			loader.loadClass("envision.interpreter.expressions.IE_Set");
			loader.loadClass("envision.interpreter.expressions.IE_Super");
			loader.loadClass("envision.interpreter.expressions.IE_Ternary");
			loader.loadClass("envision.interpreter.expressions.IE_This");
			loader.loadClass("envision.interpreter.expressions.IE_TypeOf");
			loader.loadClass("envision.interpreter.expressions.IE_Unary");
			loader.loadClass("envision.interpreter.expressions.IE_Var");
			loader.loadClass("envision.interpreter.expressions.IE_VarDec");
			//Parser
			loader.loadClass("envision.parser.ParserStage");
			loader.loadClass("envision.parser.EnvisionParser");
			//Parser - Expressions
			loader.loadClass("envision.parser.expressions.Expression");
			loader.loadClass("envision.parser.expressions.ExpressionHandler");
			//Parser - Expressions - Stages
			loader.loadClass("envision.parser.expressions.stages.PE_0_Assignment");
			loader.loadClass("envision.parser.expressions.stages.PE_1_Logic");
			loader.loadClass("envision.parser.expressions.stages.PE_2_Arithmetic");
			loader.loadClass("envision.parser.expressions.stages.PE_3_Lambda");
			loader.loadClass("envision.parser.expressions.stages.PE_4_Unary");
			loader.loadClass("envision.parser.expressions.stages.PE_5_Range");
			loader.loadClass("envision.parser.expressions.stages.PE_6_MethodCall");
			loader.loadClass("envision.parser.expressions.stages.PE_7_Primary");
			//Parser - Expressions - Types
			loader.loadClass("envision.parser.expressions.types.AssignExpression");
			loader.loadClass("envision.parser.expressions.types.BinaryExpression");
			loader.loadClass("envision.parser.expressions.types.CompoundExpression");
			loader.loadClass("envision.parser.expressions.types.DomainExpression");
			loader.loadClass("envision.parser.expressions.types.EnumExpression");
			loader.loadClass("envision.parser.expressions.types.EnumTypeExpression");
			loader.loadClass("envision.parser.expressions.types.GenericExpression");
			loader.loadClass("envision.parser.expressions.types.GetExpression");
			loader.loadClass("envision.parser.expressions.types.GroupingExpression");
			loader.loadClass("envision.parser.expressions.types.ImportExpression");
			loader.loadClass("envision.parser.expressions.types.LambdaExpression");
			loader.loadClass("envision.parser.expressions.types.ListIndexExpression");
			loader.loadClass("envision.parser.expressions.types.ListIndexSetExpression");
			loader.loadClass("envision.parser.expressions.types.ListInitializerExpression");
			loader.loadClass("envision.parser.expressions.types.LiteralExpression");
			loader.loadClass("envision.parser.expressions.types.LogicalExpression");
			loader.loadClass("envision.parser.expressions.types.MethodCallExpression");
			loader.loadClass("envision.parser.expressions.types.MethodDeclarationExpression");
			loader.loadClass("envision.parser.expressions.types.ModularExpression");
			loader.loadClass("envision.parser.expressions.types.NullExpression");
			loader.loadClass("envision.parser.expressions.types.RangeExpression");
			loader.loadClass("envision.parser.expressions.types.SetExpression");
			loader.loadClass("envision.parser.expressions.types.SuperExpression");
			loader.loadClass("envision.parser.expressions.types.TernaryExpression");
			loader.loadClass("envision.parser.expressions.types.ThisConExpression");
			loader.loadClass("envision.parser.expressions.types.ThisGetExpression");
			loader.loadClass("envision.parser.expressions.types.TypeOfExpression");
			loader.loadClass("envision.parser.expressions.types.UnaryExpression");
			loader.loadClass("envision.parser.expressions.types.VarDecExpression");
			loader.loadClass("envision.parser.expressions.types.VarExpression");
			//Parser - Statements
			loader.loadClass("envision.parser.statements.Statement");
			loader.loadClass("envision.parser.statements.StatementHandler");
			//Parser - Statements - StatementUtil
			loader.loadClass("envision.parser.statements.statementUtil.DeclarationStage");
			loader.loadClass("envision.parser.statements.statementUtil.ParserDeclaration");
			loader.loadClass("envision.parser.statements.statementUtil.StatementParameter");
			loader.loadClass("envision.parser.statements.statementUtil.StatementUtil");
			loader.loadClass("envision.parser.statements.statementUtil.VariableDeclaration");
			//Parser - Statements - Stages
			loader.loadClass("envision.parser.statements.stages.PS_Block");
			loader.loadClass("envision.parser.statements.stages.PS_Class");
			loader.loadClass("envision.parser.statements.stages.PS_Enum");
			loader.loadClass("envision.parser.statements.stages.PS_For");
			loader.loadClass("envision.parser.statements.stages.PS_GetSet");
			loader.loadClass("envision.parser.statements.stages.PS_If");
			loader.loadClass("envision.parser.statements.stages.PS_Import");
			loader.loadClass("envision.parser.statements.stages.PS_Interface");
			loader.loadClass("envision.parser.statements.stages.PS_LoopControl");
			loader.loadClass("envision.parser.statements.stages.PS_Method");
			loader.loadClass("envision.parser.statements.stages.PS_Package");
			loader.loadClass("envision.parser.statements.stages.PS_ParseDeclaration");
			loader.loadClass("envision.parser.statements.stages.PS_Return");
			loader.loadClass("envision.parser.statements.stages.PS_Switch");
			loader.loadClass("envision.parser.statements.stages.PS_Try");
			loader.loadClass("envision.parser.statements.stages.PS_VarDec");
			loader.loadClass("envision.parser.statements.stages.PS_While");
			//Parser - Statements - Types
			loader.loadClass("envision.parser.statements.types.BlockStatement");
			loader.loadClass("envision.parser.statements.types.CaseStatement");
			loader.loadClass("envision.parser.statements.types.CatchStatement");
			loader.loadClass("envision.parser.statements.types.ClassStatement");
			loader.loadClass("envision.parser.statements.types.LoopControlStatement");
			loader.loadClass("envision.parser.statements.types.EnumStatement");
			loader.loadClass("envision.parser.statements.types.ExceptionStatement");
			loader.loadClass("envision.parser.statements.types.ExpressionStatement");
			loader.loadClass("envision.parser.statements.types.ForStatement");
			loader.loadClass("envision.parser.statements.types.GenericStatement");
			loader.loadClass("envision.parser.statements.types.GetSetStatement");
			loader.loadClass("envision.parser.statements.types.IfStatement");
			loader.loadClass("envision.parser.statements.types.ImportStatement");
			loader.loadClass("envision.parser.statements.types.InterfaceStatement");
			loader.loadClass("envision.parser.statements.types.LambdaForStatement");
			loader.loadClass("envision.parser.statements.types.MethodDeclarationStatement");
			loader.loadClass("envision.parser.statements.types.ModularMethodStatement");
			loader.loadClass("envision.parser.statements.types.PackageStatement");
			loader.loadClass("envision.parser.statements.types.RangeForStatement");
			loader.loadClass("envision.parser.statements.types.ReturnStatement");
			loader.loadClass("envision.parser.statements.types.SwitchStatement");
			loader.loadClass("envision.parser.statements.types.TryStatement");
			loader.loadClass("envision.parser.statements.types.VariableStatement");
			loader.loadClass("envision.parser.statements.types.WhileStatement");
			//Tokenizer
			loader.loadClass("envision.tokenizer.EscapeCode");
			loader.loadClass("envision.tokenizer.Keyword");
			loader.loadClass("envision.tokenizer.Token");
			loader.loadClass("envision.tokenizer.Tokenizer");
		//}
	}
	
}
