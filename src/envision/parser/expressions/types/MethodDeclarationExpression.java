package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statementUtil.StatementParameter;
import envision.parser.statements.types.MethodDeclarationStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class MethodDeclarationExpression implements Expression {
	
	public final MethodDeclarationStatement declaration;
	
	public MethodDeclarationExpression(Token nameIn, EArrayList<StatementParameter> paramsIn, EArrayList<Statement> bodyIn) {
		ParserDeclaration d = new ParserDeclaration();
		d.setReturnType(Token.create(Keyword.OBJECT, "object", nameIn.line));
		declaration = new MethodDeclarationStatement(nameIn, null, paramsIn, bodyIn, d, false, false);
	}
	
	@Override
	public String toString() {
		return declaration.toString();
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleMethodDec_E(this);
	}
	
}
