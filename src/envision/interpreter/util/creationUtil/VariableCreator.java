package envision.interpreter.util.creationUtil;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidDataTypeError;
import envision.exceptions.errors.VariableNameError;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionBoolean;
import envision.lang.variables.EnvisionChar;
import envision.lang.variables.EnvisionDouble;
import envision.lang.variables.EnvisionInt;
import envision.lang.variables.EnvisionString;
import envision.tokenizer.Keyword;
import eutil.datatypes.util.EDataType;

import java.util.List;

/** Utility class designed to help with specifically variable creation. */
public class VariableCreator {
	
	//hide the constructor
	private VariableCreator() {}
	
	//----------------------------------------------------------------------
	
	public static EnvisionObject createVar(Object in) {
		EnvisionDataType type = EnvisionDataType.getDataType(in);
		return createVar(type, in);
	}
	
	public static EnvisionObject createVar(String name, Object in) {
		EnvisionDataType type = EnvisionDataType.getDataType(in);
		return createVar(name, type, in);
	}
	
	public static EnvisionObject createVar(EDataType typeIn) { return createVar(EnvisionDataType.getDataType(typeIn)); }
	public static EnvisionObject createVar(EnvisionDataType typeIn) {
		switch (typeIn) {
		case CHAR: return new EnvisionChar();
		case INT: return new EnvisionInt();
		case DOUBLE: return new EnvisionDouble();
		case STRING: return new EnvisionString();
		case BOOLEAN: return new EnvisionBoolean();
		default: throw new InvalidDataTypeError(typeIn);
		}
	}
	
	public static EnvisionObject createVar(EDataType typeIn, Object val) { return createVar(EnvisionDataType.getDataType(typeIn), val); }
	public static EnvisionObject createVar(EnvisionDataType typeIn, Object val) {
		switch (typeIn) {
		case CHAR: return new EnvisionChar((char) val);
		case INT: return new EnvisionInt((Number) val);
		case DOUBLE: return new EnvisionDouble((Number) val);
		case STRING: return new EnvisionString((String) val);
		case BOOLEAN: return new EnvisionBoolean((boolean) val);
		default: throw new InvalidDataTypeError(typeIn);
		}
	}
	
	public static EnvisionObject createVar(String name, EDataType typeIn) {
		return createVar(name, EnvisionDataType.getDataType(typeIn));
	}
	public static EnvisionObject createVar(String name, EnvisionDataType typeIn) {
		//check for null names or not allowed names
		if (name == null || !Keyword.isAllowedName(name)) { throw new VariableNameError(name); }
		
		switch (typeIn) {
		case NULL: return new EnvisionNullObject();
		case CHAR: return new EnvisionChar(name);
		case INT: return new EnvisionInt(name);
		case DOUBLE: return new EnvisionDouble(name);
		case STRING: return new EnvisionString(name);
		case BOOLEAN: return new EnvisionBoolean(name);
		default: throw new EnvisionError("Invalid variable type!");
		}
	}
	
	public static EnvisionObject createVar(String name, EDataType typeIn, Object val) {
		return createVar(name, EnvisionDataType.getDataType(typeIn), val);
	}
	public static EnvisionObject createVar(String name, EnvisionDataType typeIn, Object val) {
		if (val == null) { return createVar(name, typeIn); }
		
		//check for null names or not allowed names
		if (name == null || !Keyword.isAllowedName(name)) { throw new VariableNameError(name); }
		
		switch (typeIn) {
		case CHAR: return new EnvisionChar((char) val);
		case INT: return new EnvisionInt(name, (Number) val);
		case DOUBLE: return new EnvisionDouble(name, (Number) val);
		case STRING: return new EnvisionString(name, (String) val);
		case BOOLEAN: return new EnvisionBoolean(name, (boolean) val);
		default: throw new EnvisionError("Invalid variable type!");
		}
	}
	
	//----------------------------------------------------------------------
	
	/*
	 * This section for list creation needs to be moved out of here..
	 */
	
	public static EnvisionList createList() { return new EnvisionList(); }
	public static EnvisionList createList(String name) { return new EnvisionList(name); }
	public static EnvisionList createList(EnvisionDataType listType) { return new EnvisionList(listType); }
	public static EnvisionList createList(String listType, List data) { return new EnvisionList(listType).addAll(data); }
	public static EnvisionList createList(EnvisionDataType listType, List data) { return new EnvisionList(listType).addAll(data); }
	public static EnvisionList createList(String listType, String name) { return new EnvisionList(listType, name); }
	public static EnvisionList createList(EnvisionDataType listType, String name) { return new EnvisionList(listType, name); }
	public static EnvisionList createList(String listType, String name, List data) { return new EnvisionList(listType, name).addAll(data); }
	public static EnvisionList createList(EnvisionDataType listType, String name, List data) { return new EnvisionList(listType, name).addAll(data); }
	
	//----------------------------------------------------------------------
	
}
