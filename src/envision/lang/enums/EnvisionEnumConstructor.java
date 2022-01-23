package envision.lang.enums;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import eutil.datatypes.EArrayList;

public class EnvisionEnumConstructor extends EnvisionObject {

	EnvisionEnum theEnum;
	EArrayList<EnvisionDataType> params;
	
	public EnvisionEnumConstructor(EnvisionEnum theEnumIn) { this(theEnumIn, null); }
	public EnvisionEnumConstructor(EnvisionEnum theEnumIn, EArrayList<EnvisionDataType> paramsIn) {
		super("method");
		
		theEnum = theEnumIn;
		params = (paramsIn != null) ? new EArrayList(paramsIn) : new EArrayList();
	}
	
	public boolean hasParams() { return params.isNotEmpty(); }
	
	public EnvisionEnum getEnum() { return theEnum; }
	public EArrayList<EnvisionDataType> getParams() { return params; }
	
}