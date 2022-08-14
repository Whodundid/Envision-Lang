package envision_lang.lang.util;

import java.util.Iterator;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.InvalidParameterError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class ParameterData implements Iterable<Parameter> {
	
	EList<Parameter> params = new EArrayList<>();
	
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
	
	public ParameterData(IDatatype... typesIn) {
		for (var t : typesIn) add(t.toDatatype(), "");
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
	public ParameterData(EList<EnvisionObject> objectsIn) {
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
	
	public void add(IDatatype type, String name) {
		params.add(new Parameter(type.toDatatype(), name));
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
				if (a.datatype.getPrimitive() == Primitives.NUMBER && b.isNumber()) return true;
				if (a.datatype.isVar()) continue;
				if (!a.compare(b)) return false;
			}
			return true;
		}
		//handle array types
		else if (params.isNotEmpty() && get(0).datatype.isArrayType()) {
			IDatatype type = get(0).datatype;
			Primitives base = type.getPrimitive().getNonArrayType();
			
			if (base == null) throw new EnvisionLangError("Parameter type parsing failed! '" + get(0) + "'");
			if (base == Primitives.VAR) return true;
			
			for (int i = 0; i < size(); i++) {
				Parameter b = dataIn.get(i);
				if (base == Primitives.NUMBER && b.isNumber()) return true;
				if (!type.compare(b.datatype)) return false;
			}
			
			return true;
		}
		return false;
	}
	
	//---------
	// Getters
	//---------
	
	public Parameter get(int i) { return params.get(i); }
	
	public EList<IDatatype> getDataTypes() { return params.map(p -> p.datatype); }
	public EList<String> getNames() { return params.map(p -> p.name); }
	
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
	
	public static ParameterData fromTypes(IDatatype... types) {
		ParameterData d = new ParameterData();
		for (IDatatype t : types) {
			d.add(t.toDatatype(), "");
		}
		return d;
	}
	
}
