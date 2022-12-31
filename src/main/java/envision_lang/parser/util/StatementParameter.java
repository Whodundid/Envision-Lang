package envision_lang.parser.util;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.expression_types.Expr_Assign;
import envision_lang.tokenizer.Token;

public class StatementParameter {
	
	public final Token<?> type;
	public final Token<?> name;
	public final Expression assignment;
	public final boolean varags;
	
	public StatementParameter(Expr_Assign assignmentIn, boolean varagsIn) { this(null, null, assignmentIn, varagsIn); }
	public StatementParameter(Token<?> typeIn, Token<?> nameIn, boolean varagsIn) { this(typeIn, nameIn, null, varagsIn); }
	public StatementParameter(Token<?> typeIn, Token<?> nameIn, Expression assignmentIn, boolean varagsIn) {
		type = typeIn;
		name = nameIn;
		assignment = assignmentIn;
		varags = varagsIn;
	}
	
	@Override
	public String toString() {
		String t = (type != null) ? type.getLexeme() : "";
		String var = (varags) ? "..." : "";
		String n = (name != null) ? name.getLexeme() : "";
		String v = (type == null || name == null) ? "" : " ";
		String a = (assignment != null) ? " = " + assignment : "";
		return t + var + v + n + a;
	}
	
}
