package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.functions.InstanceFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.ArgLengthError;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.EnvisionStaticTypes;

public final class EnvisionCharClass extends EnvisionClass {

	/**
	 * The singular, static Char class for which all Envision:Char
	 * objects are derived from.
	 */
	public static final EnvisionCharClass CHAR_CLASS = new EnvisionCharClass();
	
	/**
	 * Character member function prototypes.
	 */
	private static final IPrototypeHandler CHAR_PROTOS = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		CHAR_PROTOS.define("toUpperCase", CHAR).assignDynamicClass(IFunc_toUpperCase.class);
		CHAR_PROTOS.define("toLowerCase", CHAR).assignDynamicClass(IFunc_toLowerCase.class);
	}
	
	/**
	 * Internally caches standard ascii char values in order to reduce overall char
	 * instantiation.
	 * <p>
	 * Java Integer.IntegerCache heavily referenced.
	 * 
	 * @author Hunter Bragg
	 */
	private static final class EnvisionCharCache {
	    static final int LENGTH = 127;
		static final EnvisionChar[] cache;
		
		static {
			EnvisionChar[] c = new EnvisionChar[LENGTH];
			int j = 0;
			for (int i = 0; i < LENGTH; i++) {
				c[i] = newChar((char) j++);
			}
			cache = c;
		}
		
		private EnvisionCharCache() {}
	}
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionCharClass() {
		super(Primitives.CHAR);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	@Override
	protected void registerStaticNatives() {
		staticScope.define("NULL_CHAR", EnvisionStaticTypes.CHAR_TYPE, EnvisionChar.NULL_CHAR);
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionChar newChar() { return newChar('\0'); }
	public static EnvisionChar newChar(boolean value) { return newChar(value ? 'T' : 'F'); }
	public static EnvisionChar newChar(int value) { return newChar((char) value); }
	public static EnvisionChar newChar(long value) { return newChar((char) value); }
	public static EnvisionChar newChat(EnvisionInt value) { return newChar((char) value.int_val); }
	public static EnvisionChar newChar(EnvisionChar value) { return newChar(value.char_val); }
	public static EnvisionChar newChar(char value) {
		EnvisionChar c = new EnvisionChar(value);
		CHAR_CLASS.defineScopeMembers(c);
		return c;
	}

	public static EnvisionChar defaultValue() {
		return EnvisionChar.NULL_CHAR;
	}
	
	/** Return direct instances. */
	public static EnvisionChar valueOf(EnvisionChar value) { return value; }
	public static EnvisionChar valueOf(boolean value) { return valueOf((value) ? 'T' : 'F'); }
	public static EnvisionChar valueOf(int value) { return valueOf((char) value); }
	public static EnvisionChar valueOf(long value) { return valueOf((char) value); }
	public static EnvisionChar valueOf(EnvisionInt value) { return valueOf((char) value.int_val); }
	
	public static EnvisionChar valueOf(char value) {
		int i = value;
		
		if (i >= 0 && i <= EnvisionCharCache.LENGTH) {
			return EnvisionCharCache.cache[i];
		}
		
		return newChar(value);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public ClassInstance newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionChar char_val = null;
		
		//if no args, return default char instance
		if (args.length == 0) char_val = EnvisionChar.NULL_CHAR;
		//ensure there is at most 1 argument being passed
		else if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		//otherwise, attempt to create from passed args
		else {
			EnvisionObject arg_val = args[0];
			
			//don't accept null arguments
			if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
			
			//check for invalid argument constructor datatypes
			else if (arg_val instanceof EnvisionChar c)			char_val = new EnvisionChar(c.char_val);
			else if (arg_val instanceof EnvisionInt i) 			char_val = new EnvisionChar((char) i.int_val);
			else if (arg_val instanceof EnvisionBoolean b)		char_val = new EnvisionChar((b.bool_val) ? 'T' : 'F');
			
			if (char_val == null)
				throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		}
		
		//define scope members
		defineScopeMembers(char_val);
		
		return char_val;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		//define char members
		CHAR_PROTOS.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_toUpperCase extends InstanceFunction<EnvisionChar> {
		public IFunc_toUpperCase() { super(CHAR, "toUpperCase"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionCharClass.valueOf(Character.toUpperCase(inst.char_val)));
		}
	}
	
	private static class IFunc_toLowerCase extends InstanceFunction<EnvisionChar> {
		public IFunc_toLowerCase() { super(CHAR, "toLowerCase"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionCharClass.valueOf(Character.toLowerCase(inst.char_val)));
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(CHAR, "valueOf", CHAR);
			//allow valueOf(int)
			addOverload(CHAR, INT);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionChar env_char) ret(EnvisionCharClass.valueOf(env_char));
			else if (args[0] instanceof EnvisionInt env_int) ret(EnvisionCharClass.valueOf(env_int));
			else throw new EnvisionLangError("Invalid type -- should not have reached here!");
		}
	}
	
}