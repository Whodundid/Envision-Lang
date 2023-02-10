package envision_lang.lang.packages.native_packages.base;

import envision_lang.EnvisionLang;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.natives.EnvisionStaticTypes;
import eutil.datatypes.util.EList;

public class InternalEnvision extends ClassInstance {
	
	private static EnvisionObject userArgs = EnvisionNull.NULL;
	private static InternalEnvision instance;
	
	public static void init(EnvisionLang lang, IScope internalScope, EList<String> programArgs) {
		instance = new InternalEnvision(lang, programArgs);
		internalScope.defineImportVal("Envision", instance);
	}
	
	private InternalEnvision(EnvisionLang lang, EList<String> programArgs) {
		super(InternalEnvisionClass.ENVISION_CLASS);
		setFinal();
		
		InternalEnvisionClass.ENVISION_CLASS.defineFunctionScopeMembers(this);
		
		EnvisionList list = EnvisionListClass.newList(EnvisionStaticTypes.STRING_TYPE);
		if (programArgs != null) {
			for (String arg : programArgs) {
				list.add(EnvisionStringClass.valueOf(arg));
			}
		}
		userArgs = list;
		
		internalClass.getClassScope().define("args", userArgs);
	}
	
}
