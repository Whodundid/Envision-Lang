package envision_lang.parser.expressions;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.parser.expressions.expression_types.Expr_Cast;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_Domain;
import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_FuncDef;
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

public interface ExpressionHandler {
	
	public EnvisionObject handleAssign_E(Expr_Assign e);
	public EnvisionObject handleBinary_E(Expr_Binary e);
	public EnvisionObject handleCast_E(Expr_Cast e);
	public EnvisionObject handleCompound_E(Expr_Compound e);
	public EnvisionObject handleDomain_E(Expr_Domain e);
	public EnvisionObject handleEnum_E(Expr_Enum e);
	public EnvisionObject handleGeneric_E(Expr_Generic e);
	public EnvisionObject handleGet_E(Expr_Get e);
	public EnvisionObject handleGrouping_E(Expr_Grouping e);
	public EnvisionObject handleImport_E(Expr_Import e);
	public EnvisionObject handleLambda_E(Expr_Lambda e);
	public EnvisionObject handleListIndex_E(Expr_ListIndex e);
	public EnvisionObject handleListInitializer_E(Expr_ListInitializer e);
	public EnvisionObject handleListIndexSet_E(Expr_SetListIndex e);
	public EnvisionObject handleLiteral_E(Expr_Literal e);
	public EnvisionObject handleLogical_E(Expr_Logic e);
	public EnvisionObject handleMethodCall_E(Expr_FunctionCall e);
	public EnvisionObject handleMethodDec_E(Expr_FuncDef e);
	//public EnvisionObject handleModular_E(ModularExpression e);
	public EnvisionObject handleRange_E(Expr_Range e);
	public EnvisionObject handleSet_E(Expr_Set e);
	public EnvisionObject handleSuper_E(Expr_Super e);
	public EnvisionObject handleTernary_E(Expr_Ternary e);
	public EnvisionObject handleThisGet_E(Expr_This e);
	public EnvisionObject handleTypeOf_E(Expr_TypeOf e);
	public EnvisionObject handleUnary_E(Expr_Unary e);
	public EnvisionObject handleVarDec_E(Expr_VarDef e);
	public EnvisionObject handleVar_E(Expr_Var e);
	
}
