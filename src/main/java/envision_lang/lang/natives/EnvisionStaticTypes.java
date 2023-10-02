package envision_lang.lang.natives;

public class EnvisionStaticTypes {

	private EnvisionStaticTypes() {}
	
	//------------------------------------
	// Statically Wrapped Primitive Types
	//------------------------------------
	
	public static final EnvisionDatatype ENVISION_TYPE      =   NativeTypeManager.datatypeOf(Primitives.ENVISION);
	public static final EnvisionDatatype VAR_TYPE 			= 	NativeTypeManager.datatypeOf(Primitives.VAR);
	public static final EnvisionDatatype NULL_TYPE			= 	NativeTypeManager.datatypeOf(Primitives.NULL);
	public static final EnvisionDatatype VOID_TYPE			= 	NativeTypeManager.datatypeOf(Primitives.VOID);
	public static final EnvisionDatatype BOOL_TYPE			= 	NativeTypeManager.datatypeOf(Primitives.BOOLEAN);
	public static final EnvisionDatatype CHAR_TYPE 			= 	NativeTypeManager.datatypeOf(Primitives.CHAR);
	public static final EnvisionDatatype INT_TYPE 			= 	NativeTypeManager.datatypeOf(Primitives.INT);
	public static final EnvisionDatatype DOUBLE_TYPE 		= 	NativeTypeManager.datatypeOf(Primitives.DOUBLE);
	public static final EnvisionDatatype NUMBER_TYPE 		= 	NativeTypeManager.datatypeOf(Primitives.NUMBER);
	public static final EnvisionDatatype STRING_TYPE 		= 	NativeTypeManager.datatypeOf(Primitives.STRING);
	public static final EnvisionDatatype LIST_TYPE 			= 	NativeTypeManager.datatypeOf(Primitives.LIST);
	public static final EnvisionDatatype TUPLE_TYPE			=   NativeTypeManager.datatypeOf(Primitives.TUPLE);
	public static final EnvisionDatatype CLASS_TYPE 		= 	NativeTypeManager.datatypeOf(Primitives.CLASS);
//	public static final EnvisionDatatype INTERFACE_TYPE 	= 	NativeTypeManager.datatypeOf(Primitives.INTERFACE);
	public static final EnvisionDatatype FUNC_TYPE 			= 	NativeTypeManager.datatypeOf(Primitives.FUNCTION);
	public static final EnvisionDatatype EXCEPTION_TYPE		=	NativeTypeManager.datatypeOf(Primitives.EXCEPTION);
	public static final EnvisionDatatype PACKAGE_TYPE		= 	NativeTypeManager.datatypeOf(Primitives.PACKAGE);
	public static final EnvisionDatatype FILE_TYPE          =   NativeTypeManager.datatypeOf(Primitives.FILE);
	public static final EnvisionDatatype CODE_FILE			=	NativeTypeManager.datatypeOf(Primitives.CODE_FILE);
	
	public static final EnvisionDatatype BOOL_A_TYPE        =   NativeTypeManager.datatypeOf(Primitives.BOOLEAN_A);
	public static final EnvisionDatatype CHAR_A_TYPE        =   NativeTypeManager.datatypeOf(Primitives.CHAR_A);
	public static final EnvisionDatatype INT_A_TYPE         =   NativeTypeManager.datatypeOf(Primitives.INT_A);
	public static final EnvisionDatatype DOUBLE_A_TYPE      =   NativeTypeManager.datatypeOf(Primitives.DOUBLE_A);
	public static final EnvisionDatatype STRING_A_TYPE      =   NativeTypeManager.datatypeOf(Primitives.STRING_A);
	public static final EnvisionDatatype NUMBER_A_TYPE      =   NativeTypeManager.datatypeOf(Primitives.NUMBER_A);
	public static final EnvisionDatatype VAR_A_TYPE         =   NativeTypeManager.datatypeOf(Primitives.VAR_A);
	
}
