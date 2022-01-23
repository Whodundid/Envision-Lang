package envision.lang.enums;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import eutil.datatypes.EArrayList;

public class EnumValue extends EnvisionObject {
	
	public final EnvisionEnum theEnum;
	public final String name;
	public final EArrayList<EnvisionObject> args;
	
	public EnumValue(EnvisionEnum enumIn, String nameIn) { this(enumIn, nameIn, null); }
	public EnumValue(EnvisionEnum enumIn, String nameIn, EArrayList<EnvisionObject> argsIn) {
		super(EnvisionDataType.ENUM_TYPE, nameIn);
		theEnum = enumIn;
		name = nameIn;
		args = (argsIn != null) ? new EArrayList(argsIn) : new EArrayList();
		setFinal();
		setStatic();
		setPublic();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
