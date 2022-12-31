package envision_lang.parser.statements.statement_types;

import envision_lang.lang.util.EnvisionVis;
import envision_lang.parser.statements.BasicStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_GetSet extends BasicStatement {

	public final EnvisionVis getVis, setVis;
	public final boolean get, set;
	public EList<Token<?>> vars;
	
	public Stmt_GetSet(Token<?> start, EnvisionVis getVisIn, EnvisionVis setVisIn, boolean getIn, boolean setIn) {
		this(start, getVisIn, setVisIn, getIn, setIn, null);
	}
	public Stmt_GetSet(Token<?> start, EnvisionVis getVisIn, EnvisionVis setVisIn, boolean getIn, boolean setIn, EList<Token<?>> varsIn) {
		super(start);
		getVis = getVisIn;
		setVis = setVisIn;
		get = getIn;
		set = setIn;
		vars = varsIn;
	}
	
	public void setVars(EList<Token<?>> varsIn) {
		vars = varsIn;
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		if (get) {
			if (getVis != null) out.append(getVis);
			out.append("get ");
		}
		if (set) {
			if (setVis != null) out.append(setVis);
			out.append("set ");
		}
		if (vars != null) out.append(vars);
		return out.toString();
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleGetSetStatement(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
