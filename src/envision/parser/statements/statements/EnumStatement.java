package envision.parser.statements.statements;

import envision.parser.expressions.expressions.EnumExpression;
import envision.parser.expressions.expressions.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class EnumStatement implements Statement {
	
	public final Token name;
	public final ParserDeclaration declaration;
	public final EArrayList<VarExpression> superEnums = new EArrayList<VarExpression>();
	public final EArrayList<EnumExpression> values = new EArrayList<EnumExpression>();
	public final EArrayList<Statement> body = new EArrayList<Statement>();
	
	public EnumStatement(Token nameIn, ParserDeclaration declarationIn) {
		name = nameIn;
		declaration = declarationIn;
	}
	
	public EnumStatement setSupers(EArrayList<VarExpression> supersIn) {
		superEnums.clear();
		superEnums.addAll(supersIn);
		return this;
	}
	
	public EnumStatement setValues(EArrayList<EnumExpression> valuesIn) {
		values.clear();
		values.addAll(valuesIn);
		return this;
	}
	
	public EnumStatement setBody(EArrayList<Statement> bodyIn) {
		body.clear();
		body.addAll(bodyIn);
		return this;
	}
	
	public EnumStatement addSuper(VarExpression in) { superEnums.add(in); return this; }
	public EnumStatement addValue(EnumExpression in) { values.add(in); return this; }
	public EnumStatement addStatement(Statement in) { body.add(in); return this; }
	
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
