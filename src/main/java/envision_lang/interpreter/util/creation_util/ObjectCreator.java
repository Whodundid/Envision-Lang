package envision_lang.interpreter.util.creation_util;

import java.util.List;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionTupleClass;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import eutil.datatypes.util.EList;
import eutil.debug.PotentiallyBroken;
import eutil.math.ENumUtil;

/** Utility class designed to help with general object creation. */
public class ObjectCreator {

	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	//hide the constructor
	private ObjectCreator() {}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	/** Wraps the given set of arguments in corresponding EnvisionObjects for internal language data communication. */
	public static EList<EnvisionObject> createArgs(Object... args) {
		EList<EnvisionObject> callArgs = EList.newList();
		
		//return if incomming args is null
		if (args == null) return callArgs;
		
		//wrap each argument into an object
		for (int i = 0; i < args.length; i++) {
			callArgs.add(wrap(args[i]));
		}
		
		return callArgs;
	}
	
	/** Wraps the unknown object into an EnvisionObject. If the object was already an EnvisionObject, a casting is performed on the input instead. */
	public static EnvisionObject wrap(Object in) {
		if (in == null) return EnvisionNull.NULL;
		if (in instanceof EnvisionObject env_obj) return env_obj;
		
		IDatatype type = IDatatype.dynamicallyDetermineType(in);
		//create object with determined type -- not strong -- not default
		return createObject(type, in, false, false);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionObject createDefault(IDatatype typeIn) {
		return createObject(typeIn, null, false, true);
	}
	
	public static EnvisionObject createDefault(IDatatype typeIn, boolean strongIn) {
		return createObject(typeIn, null, strongIn, true);
	}
	
	public static EnvisionObject createObject(IDatatype typeIn, Object valueIn) {
		return createObject(typeIn, valueIn, false, false);
	}
	
	public static EnvisionObject createObject(IDatatype typeIn, Object valueIn, boolean strongIn) {
		return createObject(typeIn, valueIn, strongIn, false);
	}
	
	@PotentiallyBroken("I have no idea what the 'passByValue' check is doing..")
	public static EnvisionObject createObject(IDatatype typeIn, Object valueIn, boolean strongIn, boolean defaultIn) {
		if (typeIn == null || EnvisionStaticTypes.NULL_TYPE.compare(typeIn) || valueIn == EnvisionNull.NULL) {
			return EnvisionNull.NULL;
		}
		
		//format incomming arguments
		//convert var type to definitive type
		if (typeIn.isVar()) typeIn = IDatatype.dynamicallyDetermineType(valueIn);
		Primitives p_type = typeIn.getPrimitive();
		
		//the object to be created
		EnvisionObject obj = null;
		
		//if not primitive, check for default value and furthermore matching datatype
		if (p_type == null) {
			if (valueIn == null) return EnvisionNull.NULL;
			else if (valueIn instanceof ClassInstance ci && typeIn.compare(ci.getDatatype())) {
				return ci;
			}
			else throw new EnvisionLangError("Invalid datatype '" + typeIn + "'! Envision ObjectCreator can only be used to create primitive types!");
		}
		
		if (defaultIn) {
            obj = switch (p_type) {
            case BOOLEAN            -> EnvisionBooleanClass.defaultValue();
            case CHAR               -> EnvisionCharClass.defaultValue();
            case STRING             -> EnvisionStringClass.defaultValue();
            case INT                -> EnvisionIntClass.defaultValue();
            case DOUBLE, NUMBER     -> EnvisionDoubleClass.defaultValue();
            case LIST               -> EnvisionListClass.newList();
            case TUPLE              -> EnvisionTupleClass.newTuple();
            
            default                 -> throw new EnvisionLangError("Invalid primitive type: '" + p_type + "'!");
            };
            
            //assign name and strong attribute
            if (obj != null && !(obj instanceof EnvisionNull) &&  (strongIn)) obj.setStrong();
        }
		else {
		    obj = switch (p_type) {
	        case BOOLEAN   -> EnvisionBooleanClass.valueOf((boolean) valueIn);
	        case CHAR      -> EnvisionCharClass.valueOf(charify(valueIn));
	        case STRING    -> EnvisionStringClass.valueOf(stringify(valueIn));
	        case INT       -> EnvisionIntClass.valueOf((Number) valueIn);
	        case DOUBLE    -> EnvisionDoubleClass.valueOf((Number) valueIn);
	        case NUMBER    -> (ENumUtil.isInteger(valueIn)) ? EnvisionIntClass.valueOf((Number) valueIn)
	                                                        : EnvisionDoubleClass.valueOf((Number) valueIn);
	        case LIST      -> createList(valueIn);
	        case TUPLE     -> createTuple(valueIn);
	        default        -> throw new EnvisionLangError("Invalid Datatype! This should not be possible!");
	        };
		}
		
		//assign name and strong attribute
		if (obj != null && !(obj instanceof EnvisionNull) && strongIn) obj.setStrong();
		
		//return the created object
		return obj;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionList createList() { return EnvisionListClass.newList(); }
	public static EnvisionList createList(IDatatype listType) { return EnvisionListClass.newList(listType); }
	public static EnvisionList createList(IDatatype listType, List<EnvisionObject> data) { return EnvisionListClass.newList(listType).addAll(data); }
	public static EnvisionList createList(Object value) { return EnvisionListClass.newList(value); }
	
	public static EnvisionTuple createTuple() { return EnvisionTupleClass.newTuple(); }
    public static EnvisionTuple createTuple(List<EnvisionObject> data) { return EnvisionTupleClass.newTuple(data); }
    public static EnvisionTuple createTuple(Object value) { return EnvisionTupleClass.newTuple(value); }
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Converts an object to a string and then takes the first char
	 * of that string for the output. If the input is null or results
	 * in an empty string, then the null char is returned instead.
	 * 
	 * @param input The object to convert to a char
	 * @return The converted char result of the given input object
	 */
	public static char charify(Object input) {
		if (input == null) return '\0';
		
		if (input instanceof Integer i) return Character.valueOf((char) (int) i);
		if (input instanceof Long l) return Character.valueOf((char) (long) l);
		if (input instanceof EnvisionInt i) return Character.valueOf((char) (long) i.get_i());
		
		String s = String.valueOf(input);
		
		if (s.length() > 0) return s.charAt(0);
		return '\0';
	}
	
	/**
	 * Converts an object to a string and then returns the string
	 * result. If the input is null, the output is null.
	 * 
	 * @param input THe object to convert to a string
	 * @return The converted string result of the given input
	 */
	public static String stringify(Object input) {
		return (input != null) ? String.valueOf(input) : null;
	}
	
}
