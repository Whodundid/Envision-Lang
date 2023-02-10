package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;
import eutil.datatypes.boxes.BoxList;

public class Expr_Assign extends ParsedExpression {

	//========
	// Fields
	//========
	
	public Expr_Assign leftAssign;
	public Token<?> name;
	public Operator operator;
	public ParsedExpression value;
	public final BoxList<Token<?>, Token<?>> modulars;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Assign(Token<?> nameIn, Operator operatorIn, ParsedExpression valueIn) {
		this(nameIn, operatorIn, valueIn, null);
	}
	
	public Expr_Assign(Token<?> nameIn, Operator operatorIn, ParsedExpression valueIn, BoxList<Token<?>, Token<?>> modularsIn) {
		super(nameIn);
		name = nameIn;
		operator = operatorIn;
		value = valueIn;
		modulars = modularsIn;
	}
	
	public Expr_Assign(Expr_Assign left, Operator operatorIn, ParsedExpression valueIn) {
		super(left);
		leftAssign = left;
		name = null;
		operator = operatorIn;
		value = valueIn;
		modulars = null;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String n = "";
		if (name != null) n = name.getLexeme();
		else if (modulars != null) n = modulars + "";
		else n = leftAssign + "";
		return n + " " + operator.typeString() + " " + value;
	}
	
	@Override
	public Expr_Assign copy() {
		return new Expr_Assign(name.copy(), operator, value.copy(), modulars);
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleAssign_E(this);
	}
	
	//=========
	// Getters
	//=========
	
	public String getName() {
		return name.getLexeme();
	}
	
}
