package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

public class Stmt_EnumDef extends BasicStatement {
	
	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<Expr_Var> superEnums = new EArrayList<>();
	public final EArrayList<Expr_Enum> values = new EArrayList<>();
	public final EArrayList<Statement> body = new EArrayList<>();
	
	public Stmt_EnumDef(Token nameIn, ParserDeclaration declarationIn) {
		super(nameIn);
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
		String b = (body != null && body.isNotEmpty()) ? " : {" + EStringUtil.toString(body, ", ") + " }" : "";
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
