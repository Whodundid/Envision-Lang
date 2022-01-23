package envision.parser.expressions;

public interface Expression {
	
	public <R> R execute(ExpressionHandler handler);
	public default Expression copy() { return null; }
	
	public static Expression copy(Expression e) { return (e != null) ? e.copy() : null; }
	
}
