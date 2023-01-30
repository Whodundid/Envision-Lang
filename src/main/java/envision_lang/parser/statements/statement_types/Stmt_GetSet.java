package envision_lang.parser.statements.statement_types;

import envision_lang.lang.natives.EnvisionVis;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;

public class Stmt_GetSet extends ParsedStatement {

	//========
	// Fields
	//========
	
	public final EnvisionVis getVis, setVis;
	public final boolean get, set;
	public EList<Token<?>> vars;
	
	//==============
	// Constructors
	//==============
	
	public Stmt_GetSet(Token<?> start, EnvisionVis getVisIn, EnvisionVis setVisIn) {
		this(start, getVisIn, setVisIn, null);
	}
	
	public Stmt_GetSet(Token<?> start, EnvisionVis getVisIn, EnvisionVis setVisIn, EList<Token<?>> varsIn) {
		super(start);
		getVis = getVisIn;
		setVis = setVisIn;
		get = (getVisIn != null);
		set = (setVisIn != null);
		vars = varsIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		var out = new EStringBuilder();
		if (get) out.a(getVis, "get ");
		if (set) out.a(setVis, "set ");
		if (vars != null) out.append(vars);
		return out.toString();
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleGetSetStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void setVars(EList<Token<?>> varsIn) {
		vars = varsIn;
	}
	
}
