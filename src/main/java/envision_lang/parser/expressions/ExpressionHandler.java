package envision_lang.parser.expressions;

import envision_lang.lang.EnvisionObject;
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

public interface ExpressionHandler {
	
	EnvisionObject handleAssign_E(Expr_Assign e);
	EnvisionObject handleBinary_E(Expr_Binary e);
//	EnvisionObject handleCast_E(Expr_Cast e);
	EnvisionObject handleCompound_E(Expr_Compound e);
//	EnvisionObject handleDomain_E(Expr_Domain e);
//	EnvisionObject handleEnum_E(Expr_Enum e);
//	EnvisionObject handleGeneric_E(Expr_Generic e);
	EnvisionObject handleGet_E(Expr_Get e);
//	EnvisionObject handleGrouping_E(Expr_Grouping e);
	EnvisionObject handleImport_E(Expr_Import e);
	EnvisionObject handleLambda_E(Expr_Lambda e);	
	EnvisionObject handleListIndex_E(Expr_ListIndex e);
	EnvisionObject handleListInitializer_E(Expr_ListInitializer e);
	EnvisionObject handleListIndexSet_E(Expr_SetListIndex e);
	EnvisionObject handleLiteral_E(Expr_Literal e);
	EnvisionObject handleLogical_E(Expr_Logic e);
	EnvisionObject handleMethodCall_E(Expr_FunctionCall e);
	EnvisionObject handlePrimitive_E(Expr_Primitive e);
	EnvisionObject handleRange_E(Expr_Range e);
	EnvisionObject handleSet_E(Expr_Set e);
//	EnvisionObject handleSuper_E(Expr_Super e);
	EnvisionObject handleTernary_E(Expr_Ternary e);
	EnvisionObject handleThisGet_E(Expr_This e);
	EnvisionObject handleTypeOf_E(Expr_TypeOf e);
	EnvisionObject handleUnary_E(Expr_Unary e);
	EnvisionObject handleVarDec_E(Expr_VarDef e);
	EnvisionObject handleVar_E(Expr_Var e);
	
}
