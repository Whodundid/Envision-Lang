package envision.parser.expressions;

import envision.parser.expressions.expression_types.AssignExpression;
import envision.parser.expressions.expression_types.BinaryExpression;
import envision.parser.expressions.expression_types.CompoundExpression;
import envision.parser.expressions.expression_types.DomainExpression;
import envision.parser.expressions.expression_types.EnumExpression;
import envision.parser.expressions.expression_types.GenericExpression;
import envision.parser.expressions.expression_types.GetExpression;
import envision.parser.expressions.expression_types.GroupingExpression;
import envision.parser.expressions.expression_types.ImportExpression;
import envision.parser.expressions.expression_types.LambdaExpression;
import envision.parser.expressions.expression_types.ListIndexExpression;
import envision.parser.expressions.expression_types.ListIndexSetExpression;
import envision.parser.expressions.expression_types.ListInitializerExpression;
import envision.parser.expressions.expression_types.LiteralExpression;
import envision.parser.expressions.expression_types.LogicalExpression;
import envision.parser.expressions.expression_types.MethodCallExpression;
import envision.parser.expressions.expression_types.MethodDeclarationExpression;
import envision.parser.expressions.expression_types.RangeExpression;
import envision.parser.expressions.expression_types.SetExpression;
import envision.parser.expressions.expression_types.SuperExpression;
import envision.parser.expressions.expression_types.TernaryExpression;
import envision.parser.expressions.expression_types.ThisConExpression;
import envision.parser.expressions.expression_types.ThisGetExpression;
import envision.parser.expressions.expression_types.TypeOfExpression;
import envision.parser.expressions.expression_types.UnaryExpression;
import envision.parser.expressions.expression_types.VarDecExpression;
import envision.parser.expressions.expression_types.VarExpression;

public interface ExpressionHandler {
	
	public Object handleAssign_E(AssignExpression e);
	public Object handleBinary_E(BinaryExpression e);
	public Object handleCompound_E(CompoundExpression e);
	public Object handleDomain_E(DomainExpression e);
	public Object handleEnum_E(EnumExpression e);
	public Object handleGeneric_E(GenericExpression e);
	public Object handleGet_E(GetExpression e);
	public Object handleGrouping_E(GroupingExpression e);
	public Object handleImport_E(ImportExpression e);
	public Object handleLambda_E(LambdaExpression e);
	public Object handleListIndex_E(ListIndexExpression e);
	public Object handleListInitializer_E(ListInitializerExpression e);
	public Object handleListIndexSet_E(ListIndexSetExpression e);
	public Object handleLiteral_E(LiteralExpression e);
	public Object handleLogical_E(LogicalExpression e);
	public Object handleMethodCall_E(MethodCallExpression e);
	public Object handleMethodDec_E(MethodDeclarationExpression e);
	//public Object handleModular_E(ModularExpression e);
	public Object handleRange_E(RangeExpression e);
	public Object handleSet_E(SetExpression e);
	public Object handleSuper_E(SuperExpression e);
	public Object handleTernary_E(TernaryExpression e);
	public Object handleThisCon_E(ThisConExpression e);
	public Object handleThisGet_E(ThisGetExpression e);
	public Object handleTypeOf_E(TypeOfExpression e);
	public Object handleUnary_E(UnaryExpression e);
	public Object handleVarDec_E(VarDecExpression e);
	public Object handleVar_E(VarExpression e);
	
}
