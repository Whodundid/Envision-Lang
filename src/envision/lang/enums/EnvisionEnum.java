package envision.lang.enums;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionFunction;
import envision.lang.objects.EnvisionList;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;
import envision.lang.util.VisibilityType;
import envision.lang.util.structureTypes.InheritableObject;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class EnvisionEnum extends InheritableObject {
	
	EArrayList<EnumValue> values = new EArrayList();
	EnvisionEnumConstructor constructor = new EnvisionEnumConstructor(this);
	
	EArrayList<EnvisionObject> members = new EArrayList();
	EArrayList<EnvisionFunction> functions = new EArrayList();
	
	public EnvisionEnum(VisibilityType visIn, String nameIn) {
		super(new EnvisionDatatype(Primitives.ENUM), nameIn);
		
		setVisibility(visIn);
	}
	
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalMethod(LIST, "values") { protected void body(Object[] a) { ret(new EnvisionList(ENUM_TYPE, values)); }});
		im(new InternalMethod(INT, "length") { protected void body(Object[] a) { ret(values.size()); }});
	}
	
	public void addValue(String name, EArrayList<EnvisionObject> argsIn) {
		EnumValue v = getValue(name);
		if (v != null) { throw new EnvisionError("An enum value of the same name '" + name + "' already exists on (" + this.getName() + ")!"); }
		values.add(new EnumValue(this, name, argsIn));
	}
	
	public EnumValue getValue(String name) {
		return EUtil.getFirst(values, v -> v.name.equals(name));
	}
	
	public boolean matchesConstructorParams(EArrayList<Primitives> argsIn) {
		if (constructor.hasParams()) {
			if (argsIn == null || argsIn.isEmpty() || constructor.params.size() != argsIn.size()) { return false; }
			
			for (int i = 0; i < constructor.params.size(); i++) {
				Primitives t = constructor.params.get(i);
				if (t != argsIn.get(i)) { return false; }
			}
		}
		return false;
	}
	
	public EArrayList<EnumValue> getValues() { return values; }
	public EnvisionEnumConstructor getConstructor() { return constructor; }

	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
	}
	
	/*
	public void setConstructorParams(ArgumentData dataIn) {
		
	}
	*/
	
}
