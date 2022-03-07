package envision.interpreter.util.creationUtil;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBoolean;
import envision.lang.datatypes.EnvisionChar;
import envision.lang.datatypes.EnvisionDouble;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionString;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;

import java.util.List;

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
		//create object with default name -- determined type -- not strong -- not default
		return createObject((String) null, type, in, false, false);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionObject createDefault(String nameIn, EnvisionDatatype typeIn, boolean strongIn) {
		return createObject(nameIn, typeIn, null, strongIn, true);
	}
	
	public static EnvisionObject createObject(String nameIn, EnvisionDatatype typeIn, Object valueIn) {
		return createObject(nameIn,typeIn, valueIn, false, false);
	}
	
	public static EnvisionObject createObject(String nameIn, EnvisionDatatype typeIn, Object valueIn, boolean strongIn) {
		return createObject(nameIn, typeIn, valueIn, strongIn, false);
	}
	
	public static EnvisionObject createObject(String nameIn, EnvisionDatatype typeIn, Object valueIn, boolean strongIn, boolean defaultIn) {
		if (typeIn == null) return new EnvisionNullObject();
		
		//format incomming arguments
		String name = (nameIn != null) ? nameIn : EnvisionObject.DEFAULT_NAME;
		//convert var type to definitive type
		if (typeIn.isVar()) typeIn = EnvisionDatatype.dynamicallyDetermineType(valueIn);
		Primitives p_type = typeIn.getPrimitiveType();
		
		//the object to be created
		EnvisionObject obj = null;
		
		//check if creating a variable type
		if (p_type.isVariableType()) {
			if (defaultIn) {
				switch (p_type) {
				case BOOLEAN: return new EnvisionBoolean();
				case CHAR: return new EnvisionChar();
				case INT: return new EnvisionInt();
				case NUMBER:
				case DOUBLE: return new EnvisionDouble();
				case STRING: return new EnvisionString();
				default: throw new EnvisionError("Invalid Datatype! This should not be possible!");
				}
			}
			else {
				switch (p_type) {
				case BOOLEAN: obj = new EnvisionBoolean((boolean) valueIn); break;
				case CHAR: obj = new EnvisionChar(charify(valueIn)); break; //charify the input
				case INT: obj = new EnvisionInt(((Number) valueIn).longValue()); break;
				case DOUBLE: obj = new EnvisionDouble(((Number) valueIn).doubleValue()); break;
				case STRING: obj = new EnvisionString(stringify(valueIn)); break;
				case NUMBER: 
					if (NumberUtil.isInteger(valueIn)) obj = new EnvisionInt((long) valueIn);
					else obj = new EnvisionDouble((double) valueIn);
					break;
				default:
					throw new EnvisionError("Invalid Datatype! This should not be possible!");
				}
			}
		}
		
		//check for other valid primitive types
		switch (p_type) {
		case LIST:
			var new_list = new EnvisionList(typeIn, nameIn);
			if (valueIn instanceof EnvisionList env_list) new_list.addAll(env_list);
			obj = new_list;
			break;
			
		case NULL:
			obj = new EnvisionNullObject();
			break;
			
		default: break;
		}
		
		//assign name and strong attribute
		if (obj != null && !(obj instanceof EnvisionNullObject)) {
			obj.setName(name);
			if (strongIn) obj.setStrong();
		}
		
		//return the created object
		return obj;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionList createList() { return new EnvisionList(); }
	public static EnvisionList createList(String name) { return new EnvisionList(name); }
	public static EnvisionList createList(Primitives typeIn) { return new EnvisionList(typeIn); }
	public static EnvisionList createList(EnvisionDatatype listType) { return new EnvisionList(listType); }
	public static EnvisionList createList(EnvisionDatatype listType, List data) { return new EnvisionList(listType).addAll(data); }
	public static EnvisionList createList(EnvisionDatatype listType, String name) { return new EnvisionList(listType, name); }
	public static EnvisionList createList(EnvisionDatatype listType, String name, List data) { return new EnvisionList(listType, name).addAll(data); }
	
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
