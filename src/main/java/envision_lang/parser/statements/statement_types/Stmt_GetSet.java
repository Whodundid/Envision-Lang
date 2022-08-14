package envision_lang.parser.statements.statement_types;

import envision_lang.lang.util.VisibilityType;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class Stmt_GetSet implements Statement {

	public final VisibilityType getVis, setVis;
	public final boolean get, set;
	public EList<Token> vars;
	
	public Stmt_GetSet(VisibilityType getVisIn, VisibilityType setVisIn, boolean getIn, boolean setIn) { this(getVisIn, setVisIn, getIn, setIn, null); }
	public Stmt_GetSet(VisibilityType getVisIn, VisibilityType setVisIn, boolean getIn, boolean setIn, EArrayList<Token> varsIn) {
		getVis = getVisIn;
		setVis = setVisIn;
		get = getIn;
		set = setIn;
		vars = varsIn;
	}
	
	public void setVars(EList<Token> varsIn) {
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
	
}
