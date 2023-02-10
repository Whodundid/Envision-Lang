package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Expr_ListInitializer extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final EList<ParsedExpression> values;
	
	//==============
	// Constructors
	//==============
	
	public Expr_ListInitializer(Token<?> start) { this(start, EList.newList()); }
	public Expr_ListInitializer(Token<?> start, EList<ParsedExpression> valuesIn) {
		super(start);
		values = EList.of(valuesIn);
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return values.toString();
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleListInitializer_E(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addValue(ParsedExpression in) {
		values.add(in);
	}
	
}
