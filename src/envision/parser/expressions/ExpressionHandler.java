package envision.parser.expressions;

import envision.parser.expressions.expressions.AssignExpression;
import envision.parser.expressions.expressions.BinaryExpression;
import envision.parser.expressions.expressions.CompoundExpression;
import envision.parser.expressions.expressions.DomainExpression;
import envision.parser.expressions.expressions.EnumExpression;
import envision.parser.expressions.expressions.GenericExpression;
import envision.parser.expressions.expressions.GetExpression;
import envision.parser.expressions.expressions.GroupingExpression;
import envision.parser.expressions.expressions.ImportExpression;
import envision.parser.expressions.expressions.LambdaExpression;
import envision.parser.expressions.expressions.ListIndexExpression;
import envision.parser.expressions.expressions.ListIndexSetExpression;
import envision.parser.expressions.expressions.ListInitializerExpression;
import envision.parser.expressions.expressions.LiteralExpression;
import envision.parser.expressions.expressions.LogicalExpression;
import envision.parser.expressions.expressions.MethodCallExpression;
import envision.parser.expressions.expressions.MethodDeclarationExpression;
import envision.parser.expressions.expressions.RangeExpression;
import envision.parser.expressions.expressions.SetExpression;
import envision.parser.expressions.expressions.SuperExpression;
import envision.parser.expressions.expressions.TernaryExpression;
import envision.parser.expressions.expressions.ThisConExpression;
import envision.parser.expressions.expressions.ThisGetExpression;
import envision.parser.expressions.expressions.TypeOfExpression;
import envision.parser.expressions.expressions.UnaryExpression;
import envision.parser.expressions.expressions.VarDecExpression;
import envision.parser.expressions.expressions.VarExpression;

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
