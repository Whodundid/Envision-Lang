package envision.bytecode.util;

import envision.exceptions.errors.InvalidDatatypeError;
import envision.lang.EnvisionObject;
import envision.lang.util.VisibilityType;
import eutil.datatypes.Box4;
import eutil.datatypes.EArrayList;

public class BC_Scope {
	
	private BC_Scope parent = null;
	private EArrayList<EnvisionObject> stack = null;
	private StringBuilder concater = new StringBuilder();
	private Box4<String, String, VisibilityType, EnvisionObject>[] lVals, cVals, iVals;
	
	public BC_Scope() { this(null); }
	public BC_Scope(BC_Scope parentIn) {
		parent = parentIn;
		stack = new EArrayList();
		lVals = new Box4[0];
		cVals = new Box4[0];
		iVals = new Box4[0];
	}
	
	public void lnum(int i) { lVals = new Box4[i]; }
	public void cnum(int i) { cVals = new Box4[i]; }
	public void inum(int i) { iVals = new Box4[i]; }
	
	public void ldef(int i, String name, String type, VisibilityType vis) { lVals[i] = new Box4(name, type, vis, null); }
	public void cdef(int i, String name, String type, VisibilityType vis) { cVals[i] = new Box4(name, type, vis, null); }
	public void idef(int i, String name, String type, VisibilityType vis) { iVals[i] = new Box4(name, type, vis, null); }
	
	public EnvisionObject lload(int i) { return lVals[i].getD(); }
	public EnvisionObject cload(int i) { return cVals[i].getD(); }
	public EnvisionObject iload(int i) { return iVals[i].getD(); }
	
	public void lstore(int i, EnvisionObject o) { checkType(lVals, i, o); lVals[i].setD(o); }
	public void cstore(int i, EnvisionObject o) { checkType(cVals, i, o); cVals[i].setD(o); }
	public void istore(int i, EnvisionObject o) { checkType(iVals, i, o); iVals[i].setD(o); }
	
	public Box4<String, String, VisibilityType, EnvisionObject> lget(int i) { return lVals[i]; }
	public Box4<String, String, VisibilityType, EnvisionObject> cget(int i) { return cVals[i]; }
	public Box4<String, String, VisibilityType, EnvisionObject> iget(int i) { return iVals[i]; }
	
	public Box4<String, String, VisibilityType, EnvisionObject>[] lgetAll() { return lVals; }
	public Box4<String, String, VisibilityType, EnvisionObject>[] cgetAll() { return cVals; }
	public Box4<String, String, VisibilityType, EnvisionObject>[] igetAll() { return iVals; }
	
	public BC_Scope getParent() { return parent; }
	public EArrayList<EnvisionObject> getStack() { return stack; }
	public StringBuilder getConcater() { return concater; }
	public void setParent(BC_Scope parentIn) { parent = parentIn; }
	
	private void checkType(Box4[] arr, int i, EnvisionObject o) {
		String expected = (String) arr[i].getB();
		String got = o.getDatatype().getType();
		if (!got.equals(expected))
			throw new InvalidDatatypeError("Expected type '" + expected + "' but got '" + got + "'!");
	}
	
}
