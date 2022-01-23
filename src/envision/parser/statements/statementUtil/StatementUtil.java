package envision.parser.statements.statementUtil;

import static envision.tokenizer.Keyword.*;

import envision.lang.util.EnvisionDataType;
import envision.parser.ParserStage;
import envision.parser.statements.Statement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class StatementUtil extends ParserStage {
	
	/**
	 * Collects the statements inside of a { } block.
	 * @return EArrayList<Statement>
	 */
	public static EArrayList<Statement> getBlock() { return getBlock(false); }
	public static EArrayList<Statement> getBlock(boolean inMethod) {
		EArrayList<Statement> statements = new EArrayList();
		
		while (!check(SCOPE_RIGHT) && !atEnd()) {
			Statement s = declaration(inMethod);
			//remove any new lines
			while (match(NEWLINE));
			statements.addIf(s != null, s);
		}
		
		consume(SCOPE_RIGHT, "Expected '}' after block!");
		return statements;
	}
	
	/**
	 * Parses through the next several tokens attempting to find tokens for parameters.
	 * @param types
	 * @return Token[]
	 */
	public static Token[] getParameters() { return getParameters(null); }
	public static Token[] getParameters(Token typeIn) {
		EArrayList<Token> params = new EArrayList();
		
		//check type
		if (typeIn != null) {
			EnvisionDataType datatype = EnvisionDataType.getDataType(typeIn);
			
			if (datatype != null) {
				//check if base type can even have parameters
				if (!EnvisionDataType.canBeParameterized(datatype)) {
					error("This datatype cannot be natively parameterized!");
				}
			}
		}
		
		consume(LESS_THAN, "Expected '<' to start parameter types!");
		
		if (!check(GREATER_THAN)) {
			//grab all parameters
			do {
				Token parameter = current();
				errorIf(params.contains(parameter), "Duplicate parameter in type!");
				params.add(parameter);
				advance();
			}
			while (match(COMMA));
		}
		
		consume(GREATER_THAN, "Expected '>' to end parameter types!");
		
		Token[] parr = new Token[params.size()];
		for (int i = 0; i < params.size(); i++) {
			parr[i] = params.get(i);
		}
		
		return parr;
	}
	
}
