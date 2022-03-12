package envision.parser.statements;

import envision.parser.statements.statement_types.Stmt_Block;
import envision.parser.statements.statement_types.Stmt_SwitchCase;
import envision.parser.statements.statement_types.Stmt_Catch;
import envision.parser.statements.statement_types.Stmt_Class;
import envision.parser.statements.statement_types.Stmt_EnumDef;
import envision.parser.statements.statement_types.Stmt_Exception;
import envision.parser.statements.statement_types.Stmt_Expression;
import envision.parser.statements.statement_types.Stmt_For;
import envision.parser.statements.statement_types.Stmt_Generic;
import envision.parser.statements.statement_types.Stmt_GetSet;
import envision.parser.statements.statement_types.Stmt_If;
import envision.parser.statements.statement_types.Stmt_Import;
import envision.parser.statements.statement_types.Stmt_InterfaceDef;
import envision.parser.statements.statement_types.Stmt_LambdaFor;
import envision.parser.statements.statement_types.Stmt_LoopControl;
import envision.parser.statements.statement_types.Stmt_FuncDef;
import envision.parser.statements.statement_types.Stmt_ModularFuncDef;
import envision.parser.statements.statement_types.Stmt_Package;
import envision.parser.statements.statement_types.Stmt_RangeFor;
import envision.parser.statements.statement_types.Stmt_Return;
import envision.parser.statements.statement_types.Stmt_SwitchDef;
import envision.parser.statements.statement_types.Stmt_Try;
import envision.parser.statements.statement_types.Stmt_VarDef;
import envision.parser.statements.statement_types.Stmt_While;

public interface StatementHandler {
	
	public void handleBlockStatement(Stmt_Block s);
	public void handleLoopControlStatement(Stmt_LoopControl s);
	public void handleCatchStatement(Stmt_Catch s);
	public void handleCaseStatement(Stmt_SwitchCase s);
	public void handleClassStatement(Stmt_Class s);
	public void handleEnumStatement(Stmt_EnumDef s);
	public void handleExceptionStatement(Stmt_Exception s);
	public void handleExpressionStatement(Stmt_Expression s);
	public void handleForStatement(Stmt_For s);
	public void handleGenericStatement(Stmt_Generic s);
	public void handleGetSetStatement(Stmt_GetSet s);
	public void handleIfStatement(Stmt_If s);
	public void handleImportStatement(Stmt_Import s);
	public void handleInterfaceStatement(Stmt_InterfaceDef s);
	public void handleLambdaForStatement(Stmt_LambdaFor s);
	public void handleMethodStatement(Stmt_FuncDef s);
	public void handleModularMethodStatement(Stmt_ModularFuncDef s);
	public void handlePackageStatement(Stmt_Package s);
	public void handleRangeForStatement(Stmt_RangeFor s);
	public void handleReturnStatement(Stmt_Return s);
	public void handleSwitchStatement(Stmt_SwitchDef s);
	public void handleTryStatement(Stmt_Try s);
	public void handleVariableStatement(Stmt_VarDef s);
	public void handleWhileStatement(Stmt_While s);
	
}
