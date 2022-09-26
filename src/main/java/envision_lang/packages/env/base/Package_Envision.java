package envision_lang.packages.env.base;

import envision_lang.EnvisionLang;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.util.StaticTypes;
import eutil.datatypes.EArrayList;

public class Package_Envision extends ClassInstance {
	
	private static EnvisionObject userArgs = EnvisionNull.NULL;
	private static Package_Envision instance;
	
	public static void init(EnvisionLang lang, Scope internalScope, EArrayList<String> programArgs) {
		instance = new Package_Envision(lang, programArgs);
		internalScope.define("Envision", instance);
	}
	
	private Package_Envision(EnvisionLang lang, EArrayList<String> programArgs) {
		super(Package_EnvisionClass.ENVISION_CLASS);
		setFinal();
		
		Package_EnvisionClass.ENVISION_CLASS.defineFunctionScopeMembers(this);
		
		EnvisionList list = EnvisionListClass.newList(StaticTypes.STRING_TYPE);
		for (String arg : programArgs) {
			list.add(EnvisionStringClass.newString(arg));
		}
		userArgs = list;
		
		internalClass.getClassScope().define("args", userArgs);
	}
	
}
