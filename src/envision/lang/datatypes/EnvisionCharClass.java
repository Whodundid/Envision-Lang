package envision.lang.datatypes;

import static envision.lang.natives.Primitives.CHAR;
import static envision.lang.natives.Primitives.INT;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.Primitives;
import envision.lang.util.IPrototypeHandler;
import envision.lang.util.InstanceFunction;
import envision.lang.util.StaticTypes;

public class EnvisionCharClass extends EnvisionClass {

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
		CHAR_PROTOS.define("get", CHAR).assignDynamicClass(IFunc_get.class);
		CHAR_PROTOS.define("set", CHAR, CHAR).assignDynamicClass(IFunc_set.class);
		CHAR_PROTOS.define("toUpperCase", CHAR).assignDynamicClass(IFunc_toUpperCase.class);
		CHAR_PROTOS.define("toLowerCase", CHAR).assignDynamicClass(IFunc_toLowerCase.class);
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
		staticScope.define("NULL_CHAR", StaticTypes.CHAR_TYPE, EnvisionChar.NULL_CHAR);
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
		if (args.length == 0) char_val = new EnvisionChar();
		//ensure there is at most 1 argument being passed
		else if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		//otherwise, attempt to create from passed args
		else {
			EnvisionObject arg_val = args[0];
			
			//don't accept null arguments
			if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
			
			//check for invalid argument constructor datatypes
			if (arg_val instanceof EnvisionChar c)		char_val = new EnvisionChar(c.char_val);
			if (arg_val instanceof EnvisionInt i) 		char_val = new EnvisionChar((char) i.int_val);
			if (arg_val instanceof EnvisionBoolean b)	char_val = new EnvisionChar((b.bool_val) ? 'T' : 'F');
			
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
	
	private static class IFunc_get<E extends EnvisionChar> extends InstanceFunction<E> {
		public IFunc_get() { super(CHAR, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionCharClass.newChar(inst.char_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionChar> extends InstanceFunction<E> {
		public IFunc_set() { super(CHAR, "set", CHAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.char_val = ((EnvisionChar) args[0]).char_val;
			ret(inst);
		}
	}
	
	private static class IFunc_toUpperCase<E extends EnvisionChar> extends InstanceFunction<E> {
		public IFunc_toUpperCase() { super(CHAR, "toUpperCase"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionCharClass.newChar(Character.toUpperCase(inst.char_val)));
		}
	}
	
	private static class IFunc_toLowerCase<E extends EnvisionChar> extends InstanceFunction<E> {
		public IFunc_toLowerCase() { super(CHAR, "toLowerCase"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionCharClass.newChar(Character.toLowerCase(inst.char_val)));
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
			if (args[0] instanceof EnvisionChar env_char) ret(EnvisionCharClass.newChar(env_char));
			else if (args[0] instanceof EnvisionInt env_int) ret(EnvisionIntClass.newInt(env_int));
			else throw new EnvisionError("Invalid type -- should not have reached here!");
		}
	}
	
}