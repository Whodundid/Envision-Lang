package envision.interpreter.util.creationUtil;

import java.util.List;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.lang.datatypes.EnvisionCharClass;
import envision.lang.datatypes.EnvisionDoubleClass;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.internal.EnvisionNull;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;

/** Utility class designed to help with general object creation. */
public class ObjectCreator {

	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	//hide the constructor
	private ObjectCreator() {}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	/** Wraps the given set of arguments in coresponding EnvisionObjects for internal language data communication. */
	public static EArrayList<EnvisionObject> createArgs(Object... args) {
		EArrayList<EnvisionObject> callArgs = new EArrayList();
		
		//return if incomming args is null
		if (args == null) return callArgs;
		
		//wrap each argument into an object
		for (Object o : args) {
			callArgs.add(wrap(o));
		}
		
		return callArgs;
	}
	
	/** Wraps the unknown object into an EnvisionObject. If the object was already an EnvisionObject, a casting is performed on the input instead. */
	public static EnvisionObject wrap(Object in) {
		if (in instanceof EnvisionObject env_obj) return env_obj;
		
		EnvisionDatatype type = EnvisionDatatype.dynamicallyDetermineType(in);
		//create object with determined type -- not strong -- not default
		return createObject(type, in, false, false);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionObject createDefault(EnvisionDatatype typeIn, boolean strongIn) {
		return createObject(typeIn, null, strongIn, true);
	}
	
	public static EnvisionObject createObject(EnvisionDatatype typeIn, Object valueIn) {
		return createObject(typeIn, valueIn, false, false);
	}
	
	public static EnvisionObject createObject(EnvisionDatatype typeIn, Object valueIn, boolean strongIn) {
		return createObject(typeIn, valueIn, strongIn, false);
	}
	
	public static EnvisionObject createObject(EnvisionDatatype typeIn, Object valueIn, boolean strongIn, boolean defaultIn) {
		if (typeIn == null) return EnvisionNull.NULL;
		
		//format incomming arguments
		//convert var type to definitive type
		if (typeIn.isVar()) typeIn = EnvisionDatatype.dynamicallyDetermineType(valueIn);
		Primitives p_type = typeIn.getPrimitiveType();
		
		//the object to be created
		EnvisionObject obj = null;
		
		//check if creating a variable type
		if (p_type.isVariableType()) {
			if (defaultIn) {
				switch (p_type) {
				case BOOLEAN: return EnvisionBooleanClass.newBoolean();
				case CHAR: return EnvisionCharClass.newChar();
				case INT: return EnvisionIntClass.newInt();
				case NUMBER:
				case DOUBLE: return EnvisionDoubleClass.newDouble();
				case STRING: return EnvisionStringClass.newString();
				default: throw new EnvisionError("Invalid Datatype! This should not be possible!");
				}
			}
			else {
				switch (p_type) {
				case BOOLEAN: obj = EnvisionBooleanClass.newBoolean((boolean) valueIn); break;
				case CHAR: obj = EnvisionCharClass.newChar(charify(valueIn)); break; //charify the input
				case INT: obj = EnvisionIntClass.newInt(((Number) valueIn).longValue()); break;
				case DOUBLE: obj = EnvisionDoubleClass.newDouble(((Number) valueIn).doubleValue()); break;
				case STRING: obj = EnvisionStringClass.newString(stringify(valueIn)); break;
				case NUMBER: 
					if (NumberUtil.isInteger(valueIn)) obj = EnvisionIntClass.newInt((long) valueIn);
					else obj = EnvisionDoubleClass.newDouble((double) valueIn);
					break;
				default:
					throw new EnvisionError("Invalid Datatype! This should not be possible!");
				}
			}
		}
		
		//check for other valid primitive types
		switch (p_type) {
		case LIST:
			EnvisionList new_list = EnvisionListClass.newList(typeIn);
			if (valueIn instanceof EnvisionList env_list) new_list.addAll(env_list);
			obj = new_list;
			break;
			
		case NULL:
			obj = EnvisionNull.NULL;
			break;
			
		default: break;
		}
		
		//assign name and strong attribute
		if (obj != null && !(obj instanceof EnvisionNull)) {
			if (strongIn) obj.setStrong();
		}
		
		//return the created object
		return obj;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionList createList() { return EnvisionListClass.newList(); }
	public static EnvisionList createList(Primitives typeIn) { return EnvisionListClass.newList(typeIn); }
	public static EnvisionList createList(EnvisionDatatype listType) { return EnvisionListClass.newList(listType); }
	public static EnvisionList createList(EnvisionDatatype listType, List data) { return EnvisionListClass.newList(listType).addAll(data); }
	
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
		if (input == null) return null;
		var s_input = String.valueOf(input);
		return s_input;
	}
	
}
