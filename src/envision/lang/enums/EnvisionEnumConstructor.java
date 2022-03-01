package envision.lang.enums;

import envision.lang.EnvisionObject;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;

public class EnvisionEnumConstructor extends EnvisionObject {

	EnvisionEnum theEnum;
	EArrayList<Primitives> params;
	
	public EnvisionEnumConstructor(EnvisionEnum theEnumIn) { this(theEnumIn, null); }
	public EnvisionEnumConstructor(EnvisionEnum theEnumIn, EArrayList<Primitives> paramsIn) {
		super("method");
		
		theEnum = theEnumIn;
		params = (paramsIn != null) ? new EArrayList(paramsIn) : new EArrayList();
	}
	
	public boolean hasParams() { return params.isNotEmpty(); }
	
	public EnvisionEnum getEnum() { return theEnum; }
	public EArrayList<Primitives> getParams() { return params; }
	
}