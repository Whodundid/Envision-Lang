package envision.lang.util.data;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidParameterError;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.util.EnvisionDataType;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxHolder;

import java.util.Iterator;

public class ParameterData implements Iterable<Parameter> {
	
	EArrayList<Parameter> params = new EArrayList();
	
	public ParameterData() {}
	
	public ParameterData(EArrayList<String> dataTypes, EArrayList<String> names) {
		if (dataTypes.size() == names.size()) {
			for (int i = 0; i < dataTypes.size(); i++) {
				params.add(new Parameter(dataTypes.get(i), names.get(i)));
			}
		}
	}
	
	public ParameterData(BoxHolder<String, String> data) {
		for (Box2<String, String> b : data) {
			params.add(new Parameter(b.getA(), b.getB()));
		}
	}
	
	public ParameterData(ParameterData dataIn) {
		params = new EArrayList(dataIn.params);
	}
	
	public ParameterData(Parameter... paramsIn) {
		add(paramsIn);
	}
	
	public ParameterData(EnvisionDataType... typesIn) {
		for (EnvisionDataType s : typesIn) { add(s.type, ""); }
	}
	
	public ParameterData(String... typesIn) {
		for (String s : typesIn) { add(s, ""); }
	}
	
	public ParameterData(EArrayList<Parameter> paramsIn) {
		params.addAll(paramsIn);
	}
	
	// Evil type erasure overload bypass..
	public ParameterData(EArrayList<EnvisionObject> objectsIn, Void... a) {
		for (EnvisionObject o : objectsIn) {
			if (o != null && !(o instanceof EnvisionNullObject)) {
				String type = o.getInternalType().type;
				type = (o instanceof EnvisionClass || o instanceof ClassInstance) ? o.getTypeString() : type;
				add(type, "");
			}
			else throw new InvalidParameterError(o + " is not a valid parameter type!");
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public Iterator<Parameter> iterator() { return params.iterator(); }
	
	@Override
	public String toString() {
		return getDataTypes() + "";
	}
	
	//---------
	// Methods
	//---------
	
	public int size() { return params.size(); }
	public void clear() { params.clear(); }
	
	public void add(String type, String name) {
		params.add(new Parameter(type, name));
	}
	
	public void add(Parameter... in) {
		for (Parameter p : in) {
			params.add(p);
		}
	}
	
	public void add(Box2<String, String> data) {
		params.add(new Parameter(data.getA(), data.getB()));
	}
	
	public void addAll(BoxHolder<String, String> dataIn) {
		dataIn.forEach(b -> add(b));
	}
	
	public boolean compare(ParameterData dataIn) {
		if (dataIn.size() == size()) {
			for (int i = 0; i < size(); i++) {
				Parameter a = get(i);
				Parameter b = dataIn.get(i);
				//System.out.println("the param compare: " + a.datatype + " : " + b.datatype + " : " + a.isNumber() + " : " + b.isNumber());
				if (a.datatype.equals("number") && b.isNumber()) { return true; }
				if (a.datatype.equals("var") || a.datatype.equals("object")) { continue; }
				if (!a.compare(b)) { return false; }
			}
			return true;
		}
		//handle array types
		else if (params.isNotEmpty() && EnvisionDataType.isArrayType(get(0).datatype)) {
			EnvisionDataType base = EnvisionDataType.getDataType(get(0).datatype).getNonArrayType();
			
			if (base == null) throw new EnvisionError("Parameter type parsing failed! '" + get(0) + "'");
			if (base == EnvisionDataType.OBJECT) return true;
			String bs = base.type;
			
			for (int i = 0; i < size(); i++) {
				Parameter b = dataIn.get(i);
				//System.out.println("the param compare: " + a.datatype + " : " + b.datatype + " : " + a.isNumber() + " : " + b.isNumber());
				if (bs.equals("number") && b.isNumber()) return true;
				if (bs.equals("var") || bs.equals("object")) continue;
				if (!bs.equals(b.datatype)) return false;
			}
			
			return true;
		}
		return false;
	}
	
	public boolean compare(EArrayList<String> types) {
		return compare(ParameterData.of(types));
	}
	
	//---------
	// Getters
	//---------
	
	public Parameter get(int i) { return params.get(i); }
	
	public EArrayList<String> getDataTypes() { return params.map(p -> p.datatype); }
	public EArrayList<String> getNames() { return params.map(p -> p.name); }
	
	//---------
	// Setters
	//---------
	
	public ParameterData set(ParameterData dataIn) {
		params.clear();
		dataIn.forEach(p -> add(p));
		return this;
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static ParameterData empty() { return new ParameterData(); }
	
	public static ParameterData fromTypes(EnvisionDataType... types) {
		ParameterData d = new ParameterData();
		for (EnvisionDataType t : types) {
			d.add(t.type, "");
		}
		return d;
	}
	
	public static ParameterData of(EArrayList<String> dataTypes) { return new ParameterData(dataTypes.toArray(new String[0])); }
	public static ParameterData of(EArrayList<String> dataTypes, EArrayList<String> names) { return new ParameterData(dataTypes, names); }
	public static ParameterData of(BoxHolder<String, String> data) { return new ParameterData(data); }
	public static ParameterData of(ParameterData dataIn) { return new ParameterData(dataIn); }
	public static ParameterData of(Parameter... dataIn) { return new ParameterData(dataIn); }
}
