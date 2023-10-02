package envision_lang.lang.packages.native_packages.base;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.EnvisionLang;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.LangShutdownCall;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.file.EnvisionFile;
import envision_lang.lang.file.EnvisionFileClass;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.functions.InstanceFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class InternalEnvisionClass extends EnvisionClass {
	
	/** The lang-wrapped program user arguments. */
	private static EnvisionList userArgs;
	
	public static final InternalEnvisionClass ENVISION_CLASS = new InternalEnvisionClass();
	
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
	
	private InternalEnvisionClass() {
		super(EnvisionStaticTypes.ENVISION_TYPE);
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
	
	public void defineFunctionScopeMembers(InternalEnvision env) {
		//define super object's members
		super.defineScopeMembers(env);
		//define scope members
		ENVISION_PROTOS.defineOn(env);
	}
	
	//---------------------------------
	// Static Envision Class Functions
	//---------------------------------
	
	private static class IFunc_shutdown extends InstanceFunction<InternalEnvision> {
		public IFunc_shutdown() { super(VOID, "shutdown"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			throw new LangShutdownCall();
		}
	}
	
	private static class IFunc_dir extends InstanceFunction<InternalEnvision> {
		public IFunc_dir() { super(FILE, "dir"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionFile dirFile = EnvisionFileClass.newFile(EnvisionLang.programDir);
			ret(dirFile);
		}
	}
}
