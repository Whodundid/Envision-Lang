package envision_lang.interpreter.util.scope;

import java.util.HashMap;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.EnvisionVis;
import eutil.datatypes.boxes.Box2;

public class VisibilityScope extends Scope {
	
	private final EnvisionVis visibility;
	private final HashMap<String, Box2<IDatatype, EnvisionObject>> values = new HashMap<>();
	
	public VisibilityScope(EnvisionVis visIn) {
		visibility = visIn;
	}
	
	
	
}
