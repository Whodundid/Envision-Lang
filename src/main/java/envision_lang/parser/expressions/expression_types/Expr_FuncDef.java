package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.StatementParameter;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
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
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleMethodDec_E(this);
	}
	
}
