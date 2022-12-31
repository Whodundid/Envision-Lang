package envision_lang.parser.statements;

import envision_lang.tokenizer.Token;

public abstract class BasicStatement implements Statement {
	
	public final Token<?> definingToken;
	
	protected BasicStatement(Token<?> definingTokenIn) {
		definingToken = definingTokenIn;
	}
	
	public Token<?> getDefiningToken() { return definingToken; }

	@Override
	public Token<?> definingToken() {
		return null;
	}
	
}
