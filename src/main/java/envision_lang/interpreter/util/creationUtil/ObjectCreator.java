package envision_lang.interpreter.util.creationUtil;

import java.util.List;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;

/** Utility class designed to help with general object creation. */
public class ObjectCreator {

	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	//hide the constructor
	private ObjectCreator() {}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	/** Wraps the given set of arguments in corresponding EnvisionObjects for internal language data communication. */
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
		
		IDatatype type = IDatatype.dynamicallyDetermineType(in);
		//create object with determined type -- not strong -- not default
		return createObject(type, in, false, false);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static EnvisionObject createDefault(IDatatype typeIn, boolean strongIn) {
		return createObject(typeIn, null, strongIn, true);
	}
	
	public static EnvisionObject createObject(IDatatype typeIn, Object valueIn) {
		return createObject(typeIn, valueIn, false, false);
	}
	
	public static EnvisionObject createObject(IDatatype typeIn, Object valueIn, boolean strongIn) {
		return createObject(typeIn, valueIn, strongIn, false);
	}
	
	public static EnvisionObject createObject(IDatatype typeIn, Object valueIn, boolean strongIn, boolean defaultIn) {
		if (typeIn == null) return EnvisionNull.NULL;
		
		//format incomming arguments
		//convert var type to definitive type
		if (typeIn.isVar()) typeIn = IDatatype.dynamicallyDetermineType(valueIn);
		Primitives p_type = typeIn.getPrimitive();
		
		//the object to be created
		EnvisionObject obj = null;
		
		//if not primitive, check for default value and furthermore matching datatype
		if (p_type == null)
			if (valueIn == null) return EnvisionNull.NULL;
			else if (valueIn instanceof ClassInstance ci && typeIn.compare(ci.getDatatype())) {
				return ci;
			}
		
		//check if creating a variable type
		if (p_type.isVariableType()) {
			if (defaultIn) {
				switch (p_type) {
				case BOOLEAN: return EnvisionBooleanClass.newBoolean(defaultIn);
				case CHAR: return EnvisionCharClass.newChar();
				case INT: return EnvisionIntClass.newInt();
				case NUMBER:
				case DOUBLE: return EnvisionDoubleClass.newDouble();
				case STRING: return EnvisionStringClass.newString();
				default: throw new EnvisionLangError("Invalid Datatype! This should not be possible!");
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
					if (ENumUtil.isInteger(valueIn)) obj = EnvisionIntClass.newInt((long) valueIn);
					else obj = EnvisionDoubleClass.newDouble((double) valueIn);
					break;
				default:
					throw new EnvisionLangError("Invalid Datatype! This should not be possible!");
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
	public static EnvisionList createList(IDatatype listType) { return EnvisionListClass.newList(listType); }
	public static EnvisionList createList(IDatatype listType, List data) { return EnvisionListClass.newList(listType).addAll(data); }
	
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
