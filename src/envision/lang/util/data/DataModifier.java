package envision.lang.util.data;

import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.tokenizer.IKeyword;
import envision.tokenizer.ReservedWord;
import java.util.List;

public enum DataModifier {
	
	STATIC		(0b00000001),	//0000-0001
	FINAL		(0b00000010),	//0000-0010
	STRONG		(0b00000100),	//0000-0100
	ABSTRACT	(0b00001000),	//0000-1000
	OVERRIDE	(0b00010000);	//0001-0000
	
	public final int byteVal;
	
	private DataModifier(int byteValIn) {
		byteVal = byteValIn;
	}
	
	public static DataModifier of(IKeyword k) {
		if (k.isReservedWord()) return of(k.asReservedWord());
		return null;
	}
	
	public static DataModifier of(ReservedWord k) {
		switch (k) {
		case STATIC: return STATIC;
		case FINAL: return FINAL;
		case STRONG: return STRONG;
		case ABSTRACT: return ABSTRACT;
		case OVERRIDE: return OVERRIDE;
		default: return null;
		}
	}
	
	//--------------------------------------
	
	/** Returns true if all of the modifiers are valid for a variable. */
	public static boolean isValid_varDec(ParserDeclaration dec) {
		return isValid_varDec(dec.getMods());
	}
	
	/** Returns true if all of the modifiers are valid for a variable. */
	public static boolean isValid_varDec(List<DataModifier> mods) {
		for (DataModifier m : mods) {
			if (!checkVariable(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a variable. */
	public static boolean checkVariable(DataModifier... mods) {
		for (DataModifier m : mods) {
			if (!checkVariable(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a generic (var) variable. */
	public static boolean checkGeneric(List<DataModifier> mods) {
		for (DataModifier m : mods) {
			if (!checkGeneric(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a generic (var) variable. */
	public static boolean checkGeneric(DataModifier... mods) {
		for (DataModifier m : mods) {
			if (!checkGeneric(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a method. */
	public static boolean checkMethod(List<DataModifier> mods) {
		for (DataModifier m : mods) {
			if (!checkMethod(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a method. */
	public static boolean checkMethod(DataModifier... mods) {
		for (DataModifier m : mods) {
			if (!checkMethod(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a class. */
	public static boolean checkClass(List<DataModifier> mods) {
		for (DataModifier m : mods) {
			if (!checkClass(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for a class. */
	public static boolean checkClass(DataModifier... mods) {
		for (DataModifier m : mods) {
			if (!checkClass(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for an enum. */
	public static boolean checkEnum(List<DataModifier> mods) {
		for (DataModifier m : mods) {
			if (!checkEnum(m)) { return false; }
		}
		return true;
	}
	
	/** Returns true if all of the modifiers are valid for an enum. */
	public static boolean checkEnum(DataModifier... mods) {
		for (DataModifier m : mods) {
			if (!checkEnum(m)) { return false; }
		}
		return true;
	}
	
	//--------------------------------------
	
	/** Returns true if the data modifier is valid for a variable. */
	public static boolean checkVariable(DataModifier m) {
		switch (m) {
		case STATIC:
		case FINAL:
		case STRONG: return true;
		default: return false;
		}
	}
	
	/** Returns true if the data modifier is valid for a generic (var) variable. */
	public static boolean checkGeneric(DataModifier m) {
		switch (m) {
		case STATIC:
		case FINAL:
		case STRONG: return true;
		default: return false;
		}
	}
	
	/** Returns true if the data modifier is valid for a method. */
	public static boolean checkMethod(DataModifier m) {
		switch (m) {
		case STATIC:
		case FINAL:
		case ABSTRACT:
		case OVERRIDE: return true;
		default: return false;
		}
	}
	
	/** Returns true if the data modifier is valid for a class. */
	public static boolean checkClass(DataModifier m) {
		switch (m) {
		case STATIC:
		case FINAL:
		case ABSTRACT: return true;
		default: return false;
		}
	}
	
	/** Returns true if the data modifier is valid for an enum. */
	public static boolean checkEnum(DataModifier m) {
		switch (m) {
		case STATIC:
		case FINAL: return true;
		default: return false;
		}
	}
	
}
