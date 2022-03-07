package envision.parser.util;

public enum DeclarationStage {
	VISIBILITY(0),
	DATAMODS(1),
	GENERICS(2),
	TYPE(3);
	
	private int num;
	
	private DeclarationStage(int numIn) {
		num = numIn;
	}
	
	//------------------------------
	
	public DeclarationStage next() { return next(this); }
	
	public boolean greaterThan(DeclarationStage in) { return num > in.num; }
	public boolean greaterEqual(DeclarationStage in) { return num >= in.num; }
	public boolean lessThan(DeclarationStage in) { return num < in.num; }
	public boolean lessEqual(DeclarationStage in) { return num <= in.num; }
	
	public static DeclarationStage next(DeclarationStage in) {
		for (DeclarationStage s : values())
			if (s.num == in.num + 1)
				return s;
		return null;
	}
	
	public static DeclarationStage of(DeclarationStage in) {
		for (DeclarationStage s : values())
			if (s == in)
				return s;
		return null;
	}
	
}
