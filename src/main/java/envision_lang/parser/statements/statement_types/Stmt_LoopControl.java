package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

/**
 * Can either be a break or continue statement.
 * For continue, pass 'false' for isBreakIn.
 * 
 * @author Hunter
 */
public class Stmt_LoopControl extends BasicStatement {
	
	public final boolean isBreak;
	public final boolean isContinue;
	public final Expression condition;
	
	public Stmt_LoopControl(Token start, boolean isBreakIn) { this(start, isBreakIn, null); }
	public Stmt_LoopControl(Token start, boolean isBreakIn, Expression conditionIn) {
		super(start);
		isBreak = isBreakIn;
		isContinue = !isBreakIn;
		condition = conditionIn;
	}
	
	@Override
	public String toString() {
		String n = (isBreak) ? "break" : ("cont" + ((condition != null) ? "" : "inue"));
		String c = (condition != null) ? "if(" + condition + ")" : "";
		return n + c;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleLoopControlStatement(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
