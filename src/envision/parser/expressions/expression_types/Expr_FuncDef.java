package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_FuncDef;
import envision.parser.util.ParserDeclaration;
import envision.parser.util.StatementParameter;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class Expr_FuncDef implements Expression {
	
	public final Stmt_FuncDef declaration;
	
	public Expr_FuncDef(Token nameIn, EArrayList<StatementParameter> paramsIn, EArrayList<Statement> bodyIn) {
		ParserDeclaration d = new ParserDeclaration();
		d.applyReturnType(Token.create(ReservedWord.VAR, "var", nameIn.line));
		declaration = new Stmt_FuncDef(nameIn, null, paramsIn, bodyIn, d, false, false);
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
