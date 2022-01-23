package envision.parser.statements;

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

public interface StatementHandler {
	
	public void handleBlockStatement(BlockStatement s);
	public void handleLoopControlStatement(LoopControlStatement s);
	public void handleCatchStatement(CatchStatement s);
	public void handleCaseStatement(CaseStatement s);
	public void handleClassStatement(ClassStatement s);
	public void handleEnumStatement(EnumStatement s);
	public void handleExceptionStatement(ExceptionStatement s);
	public void handleExpressionStatement(ExpressionStatement s);
	public void handleForStatement(ForStatement s);
	public void handleGenericStatement(GenericStatement s);
	public void handleGetSetStatement(GetSetStatement s);
	public void handleIfStatement(IfStatement s);
	public void handleImportStatement(ImportStatement s);
	public void handleInterfaceStatement(InterfaceStatement s);
	public void handleLambdaForStatement(LambdaForStatement s);
	public void handleMethodStatement(MethodDeclarationStatement s);
	public void handleModularMethodStatement(ModularMethodStatement s);
	public void handlePackageStatement(PackageStatement s);
	public void handleRangeForStatement(RangeForStatement s);
	public void handleReturnStatement(ReturnStatement s);
	public void handleSwitchStatement(SwitchStatement s);
	public void handleTryStatement(TryStatement s);
	public void handleVariableStatement(VariableStatement s);
	public void handleWhileStatement(WhileStatement s);
	
}
