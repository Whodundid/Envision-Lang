package envision.bytecode.vm;

import envision.bytecode.BC_Statement;
import envision.bytecode.util.BC_Params;
import envision.bytecode.util.BC_Scope;
import eutil.datatypes.EArrayList;

public class BC_Method {
	
	private String name;
	private String rType;
	private BC_Params params;
	private boolean isConst = false;
	private EArrayList<BC_Statement> body = new EArrayList();
	private BC_Scope scope;
	
	public BC_Method(String nameIn, String rTypeIn, BC_Params paramsIn, boolean isConstIn) {
		name = nameIn;
		rType = rTypeIn;
		params = paramsIn;
		isConst = isConstIn;
	}
	
	public void addBody(BC_Statement s) { body.add(s); }
	
}
