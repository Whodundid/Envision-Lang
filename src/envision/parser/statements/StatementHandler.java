package envision.parser.statements;

import envision.parser.statements.statements.BlockStatement;
import envision.parser.statements.statements.CaseStatement;
import envision.parser.statements.statements.CatchStatement;
import envision.parser.statements.statements.ClassStatement;
import envision.parser.statements.statements.EnumStatement;
import envision.parser.statements.statements.ExceptionStatement;
import envision.parser.statements.statements.ExpressionStatement;
import envision.parser.statements.statements.ForStatement;
import envision.parser.statements.statements.GenericStatement;
import envision.parser.statements.statements.GetSetStatement;
import envision.parser.statements.statements.IfStatement;
import envision.parser.statements.statements.ImportStatement;
import envision.parser.statements.statements.InterfaceStatement;
import envision.parser.statements.statements.LambdaForStatement;
import envision.parser.statements.statements.LoopControlStatement;
import envision.parser.statements.statements.MethodDeclarationStatement;
import envision.parser.statements.statements.ModularMethodStatement;
import envision.parser.statements.statements.PackageStatement;
import envision.parser.statements.statements.RangeForStatement;
import envision.parser.statements.statements.ReturnStatement;
import envision.parser.statements.statements.SwitchStatement;
import envision.parser.statements.statements.TryStatement;
import envision.parser.statements.statements.VariableStatement;
import envision.parser.statements.statements.WhileStatement;

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
