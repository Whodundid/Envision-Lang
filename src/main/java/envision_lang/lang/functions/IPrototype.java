package envision_lang.lang.functions;

import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.ParameterData;
import eutil.datatypes.boxes.BoxList;

/**
 * Applied to any function that is intended to be initially created as a
 * function prototype.
 * 
 * @author Hunter Bragg
 */
public interface IPrototype {
	
	/** The name of the prototype function. */
	public String name();
	/** The return type of the prototype function. */
	public IDatatype returnType();
	/** The accepted parameter datatypes (in order) of the prototype function. */
	public ParameterData parameters();
	/** The list of override parameter types in the order of ('returnType' : [parameter]*) */
	public BoxList<IDatatype, ParameterData> overloads();
	/** The dynamic backing class for which this prototype builds to. */
	public Class<? extends InstanceFunction> dynamicClass();
	
}
