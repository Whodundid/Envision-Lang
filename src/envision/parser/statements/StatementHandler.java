package envision.parser.statements;

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
	public void handleMethodStatement(FuncDefStatement s);
	public void handleModularMethodStatement(ModularFunctionStatement s);
	public void handlePackageStatement(PackageStatement s);
	public void handleRangeForStatement(RangeForStatement s);
	public void handleReturnStatement(ReturnStatement s);
	public void handleSwitchStatement(SwitchStatement s);
	public void handleTryStatement(TryStatement s);
	public void handleVariableStatement(VariableStatement s);
	public void handleWhileStatement(WhileStatement s);
	
}
