package envision.parser.statements.statements;

import envision.lang.util.VisibilityType;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class GetSetStatement implements Statement {

	public final VisibilityType getVis, setVis;
	public final boolean get, set;
	public EArrayList<Token> vars;
	
	public GetSetStatement(VisibilityType getVisIn, VisibilityType setVisIn, boolean getIn, boolean setIn) { this(getVisIn, setVisIn, getIn, setIn, null); }
	public GetSetStatement(VisibilityType getVisIn, VisibilityType setVisIn, boolean getIn, boolean setIn, EArrayList<Token> varsIn) {
		getVis = getVisIn;
		setVis = setVisIn;
		get = getIn;
		set = setIn;
		vars = varsIn;
	}
	
	public void setVars(EArrayList<Token> varsIn) {
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
