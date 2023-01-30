package envision_lang.lang.packages.env.base;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.EnvisionLang;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.LangShutdownCall;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.internal.IPrototypeHandler;
import envision_lang.lang.internal.InstanceFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.packages.env.file.EnvisionFile;
import envision_lang.lang.packages.env.file.EnvisionFileClass;

public class Package_EnvisionClass extends EnvisionClass {
	
	/** The lang-wrapped program user arguments. */
	private static EnvisionList userArgs;
	
	/**
	 * Constant file datatype reference. Use throughout.
	 * Do not redefine.
	 */
	public static final IDatatype ENVISION_DATATYPE = NativeTypeManager.datatypeOf("Envision");
	
	public static final Package_EnvisionClass ENVISION_CLASS = new Package_EnvisionClass();
	
	/**
	 * Character member function prototypes.
	 */
	private static final IPrototypeHandler ENVISION_PROTOS = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		ENVISION_PROTOS.define("shutdown").assignDynamicClass(IFunc_shutdown.class);
		ENVISION_PROTOS.define("dir").assignDynamicClass(IFunc_dir.class);
	}
	
	//--------------
	// Constructors
	//--------------
	
	private Package_EnvisionClass() {
		super(ENVISION_DATATYPE.getStringValue());
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public ClassInstance newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new EnvisionLangError("Illegal Object Instantiation!");
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new EnvisionLangError("Illegal Object Instantiation!");
	}
	
	//---------
	// Methods
	//---------
	
	public void defineFunctionScopeMembers(Package_Envision env) {
		//define super object's members
		super.defineScopeMembers(env);
		//define scope members
		ENVISION_PROTOS.defineOn(env);
	}
	
	//---------------------------------
	// Static Envision Class Functions
	//---------------------------------
	
	private static class IFunc_shutdown<E extends Package_Envision> extends InstanceFunction<E> {
		public IFunc_shutdown() { super(VOID, "shutdown"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			throw new LangShutdownCall();
		}
	}
	
	private static class IFunc_dir<E extends Package_Envision> extends InstanceFunction<E> {
		public IFunc_dir() { super(EnvisionFileClass.FILE_DATATYPE, "dir"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionFile dirFile = EnvisionFileClass.newFile(EnvisionLang.programDir);
			ret(dirFile);
		}
	}
}
