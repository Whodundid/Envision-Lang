package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class Stmt_EnumDef implements Statement {
	
	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<Expr_Var> superEnums = new EArrayList<Expr_Var>();
	public final EArrayList<Expr_Enum> values = new EArrayList<Expr_Enum>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	
	public Stmt_EnumDef(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public Stmt_EnumDef setSupers(EArrayList<Expr_Var> supersIn) {
		superEnums.clear();
		superEnums.addAll(supersIn);
		return this;
	}
	
	public Stmt_EnumDef setValues(EArrayList<Expr_Enum> valuesIn) {
		values.clear();
		values.addAll(valuesIn);
		return this;
	}
	
	public Stmt_EnumDef setBody(EArrayList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public Stmt_EnumDef addSuper(Expr_Var in) { superEnums.add(in); return this; }
	public Stmt_EnumDef addValue(Expr_Enum in) { values.add(in); return this; }
	public Stmt_EnumDef addStatement(Statement in) { body.add(in); return this; }
	
	@Override
	public String toString() {
		String s = (superEnums.isEmpty()) ? "" : " " + superEnums.toString();
		String b = (body != null && body.isNotEmpty()) ? " : {" + StringUtil.toString(body, ", ") + " }" : "";
		return declaration + " enum " + name.lexeme + s + " { " + values + b + " }";
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleEnumStatement(this);
	}
	
}
