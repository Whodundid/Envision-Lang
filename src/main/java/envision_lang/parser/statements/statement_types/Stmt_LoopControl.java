package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;

/**
 * Can either be a break or continue statement.
 * For continue, pass 'false' for isBreakIn.
 * 
 * @author Hunter
 */
public class Stmt_LoopControl extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final boolean isBreak;
	public final boolean isContinue;
	public final ParsedExpression condition;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_LoopControl(Token start, boolean isBreakIn) { this(start, isBreakIn, null); }
	public Stmt_LoopControl(Token start, boolean isBreakIn, ParsedExpression conditionIn) {
		super(start);
		isBreak = isBreakIn;
		isContinue = !isBreakIn;
		condition = conditionIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String n = (isBreak) ? "break" : ("cont" + ((condition != null) ? "" : "inue"));
		String c = (condition != null) ? "if(" + condition + ")" : "";
		return n + c;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleLoopControlStatement(this);
	}
	
}
