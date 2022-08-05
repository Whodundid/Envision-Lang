package envision_lang.parser.expressions;

import envision_lang.lang.EnvisionObject;

public interface Expression {
	
	public EnvisionObject execute(ExpressionHandler handler);
	public default Expression copy() { return null; }
	
	public static Expression copy(Expression e) { return (e != null) ? e.copy() : null; }
	
}
