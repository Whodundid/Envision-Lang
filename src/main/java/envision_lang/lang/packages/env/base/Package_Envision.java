package envision_lang.lang.packages.env.base;

import envision_lang.EnvisionLang;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.natives.StaticTypes;
import eutil.datatypes.util.EList;

public class Package_Envision extends ClassInstance {
	
	private static EnvisionObject userArgs = EnvisionNull.NULL;
	private static Package_Envision instance;
	
	public static void init(EnvisionLang lang, IScope internalScope, EList<String> programArgs) {
		instance = new Package_Envision(lang, programArgs);
		internalScope.defineImportVal("Envision", instance);
	}
	
	private Package_Envision(EnvisionLang lang, EList<String> programArgs) {
		super(Package_EnvisionClass.ENVISION_CLASS);
		setFinal();
		
		Package_EnvisionClass.ENVISION_CLASS.defineFunctionScopeMembers(this);
		
		EnvisionList list = EnvisionListClass.newList(StaticTypes.STRING_TYPE);
		if (programArgs != null) {
			for (String arg : programArgs) {
				list.add(EnvisionStringClass.valueOf(arg));
			}
		}
		userArgs = list;
		
		internalClass.getClassScope().define("args", userArgs);
	}
	
}
