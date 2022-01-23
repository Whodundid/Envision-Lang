package envision.bytecode.util;

public class BC_Params {
	
	private String[] types;
	
	public BC_Params(String... typesIn) {
		types = new String[typesIn.length];
		for (int i = 0; i < typesIn.length; i++)
			types[i] = typesIn[i];
	}
	
	public String get(int i) { return types[i]; }

	public boolean compare(BC_Params in) {
		if (in == null || types.length == in.types.length) return false;
		for (int i = 0; i < types.length; i++) {
			String a = types[i];
			String b = in.types[i];
			if (!a.equals(b)) return false;
		}
		return true;
	}
	
}
