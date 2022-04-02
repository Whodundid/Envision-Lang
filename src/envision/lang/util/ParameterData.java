package envision.lang.util;

import java.util.Iterator;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidParameterError;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionNull;
import eutil.datatypes.EArrayList;

public class ParameterData implements Iterable<Parameter> {
	
	EArrayList<Parameter> params = new EArrayList();
	
	//--------------
	// Constructors
	//--------------
	
	public ParameterData() {}
	
	public ParameterData(ParameterData dataIn) {
		params = new EArrayList(dataIn.params);
	}
	
	public ParameterData(Parameter... paramsIn) {
		add(paramsIn);
	}
	
	public ParameterData(Primitives... typesIn) {
		for (Primitives s : typesIn) add(s.toDatatype(), "");
	}
	
	public ParameterData(EnvisionDatatype... typesIn) {
		for (var t : typesIn) add(t, "");
	}
	
	public ParameterData(EnvisionObject... objsIn) {
		for (var o : objsIn) {
			if (o != null && !(o instanceof EnvisionNull)) {
				add(o.getDatatype(), "");
			}
			else throw new InvalidParameterError(o + " is not a valid parameter type!");
		}
	}
	
	// Evil type erasure overload bypass..
	public ParameterData(EArrayList<EnvisionObject> objectsIn) {
		for (EnvisionObject o : objectsIn) {
			if (o != null && !(o instanceof EnvisionNull)) {
				add(o.getDatatype(), "");
			}
			else throw new InvalidParameterError(o + " is not a valid parameter type!");
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public Iterator<Parameter> iterator() {
		return params.iterator();
	}
	
	@Override
	public String toString() {
		return getDataTypes() + "";
	}
	
	//---------
	// Methods
	//---------
	
	public void add(Primitives type, String name) {
		params.add(new Parameter(type.toDatatype(), name));
	}
	
	public void add(EnvisionDatatype type, String name) {
		params.add(new Parameter(type, name));
	}
	
	public void add(Parameter... in) {
		for (Parameter p : in) {
			params.add(p);
		}
	}
	
	public int size() {
		return params.size();
	}
	
	public void clear() {
		params.clear();
	}
	
	public boolean compare(ParameterData dataIn) {
		if (dataIn.size() == size()) {
			for (int i = 0; i < size(); i++) {
				Parameter a = get(i);
				Parameter b = dataIn.get(i);
				//System.out.println("the param compare: " + a.datatype + " : " + b.datatype + " : " + a.isNumber() + " : " + b.isNumber());
				if (a.datatype.getPrimitiveType() == Primitives.NUMBER && b.isNumber()) return true;
				if (a.datatype.isVar()) continue;
				if (!a.compare(b)) return false;
			}
			return true;
		}
		//handle array types
		else if (params.isNotEmpty() && get(0).datatype.isArrayType()) {
			EnvisionDatatype type = get(0).datatype;
			Primitives base = type.getPrimitiveType().getNonArrayType();
			
			if (base == null) throw new EnvisionError("Parameter type parsing failed! '" + get(0) + "'");
			if (base == Primitives.VAR) return true;
			
			for (int i = 0; i < size(); i++) {
				Parameter b = dataIn.get(i);
				if (base == Primitives.NUMBER && b.isNumber()) return true;
				if (!type.compareType(b.datatype)) return false;
			}
			
			return true;
		}
		return false;
	}
	
	//---------
	// Getters
	//---------
	
	public Parameter get(int i) { return params.get(i); }
	
	public EArrayList<EnvisionDatatype> getDataTypes() { return params.map(p -> p.datatype); }
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
	
	public static ParameterData fromTypes(Primitives... types) {
		ParameterData d = new ParameterData();
		for (Primitives t : types) {
			d.add(t.toDatatype(), "");
		}
		return d;
	}
	
}
