package envision.parser.expressions;

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
import envision.parser.expressions.expression_types.ThisGetExpression;
import envision.parser.expressions.expression_types.Expr_TypeOf;
import envision.parser.expressions.expression_types.Expr_Unary;
import envision.parser.expressions.expression_types.Expr_VarDef;
import envision.parser.expressions.expression_types.Expr_Var;

public interface ExpressionHandler {
	
	public Object handleAssign_E(Expr_Assign e);
	public Object handleBinary_E(Expr_Binary e);
	public Object handleCompound_E(Expr_Compound e);
	public Object handleDomain_E(Expr_Domain e);
	public Object handleEnum_E(Expr_Enum e);
	public Object handleGeneric_E(Expr_Generic e);
	public Object handleGet_E(Expr_Get e);
	public Object handleGrouping_E(Expr_Grouping e);
	public Object handleImport_E(Expr_Import e);
	public Object handleLambda_E(Expr_Lambda e);
	public Object handleListIndex_E(Expr_ListIndex e);
	public Object handleListInitializer_E(Expr_ListInitializer e);
	public Object handleListIndexSet_E(Expr_SetListIndex e);
	public Object handleLiteral_E(Expr_Literal e);
	public Object handleLogical_E(Expr_Logic e);
	public Object handleMethodCall_E(Expr_FunctionCall e);
	public Object handleMethodDec_E(Expr_FuncDef e);
	//public Object handleModular_E(ModularExpression e);
	public Object handleRange_E(Expr_Range e);
	public Object handleSet_E(Expr_Set e);
	public Object handleSuper_E(Expr_Super e);
	public Object handleTernary_E(Expr_Ternary e);
	public Object handleThisGet_E(ThisGetExpression e);
	public Object handleTypeOf_E(Expr_TypeOf e);
	public Object handleUnary_E(Expr_Unary e);
	public Object handleVarDec_E(Expr_VarDef e);
	public Object handleVar_E(Expr_Var e);
	
}
