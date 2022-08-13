package envision_lang.lang.internal;

import java.util.List;

import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.ParameterData;
import envision_lang.lang.util.StaticTypes;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;

/**
 * Essentially a glorified list that is used to directly define a set of
 * internal function prototypes for a given native EnvisionObject.
 * <p>
 * Note: The intended use of this object is entirely internal and is not
 * designed to be interfaced with actual Envision code.
 * 
 * @author Hunter Bragg
 */
public class IPrototypeHandler {

	private final EArrayList<ProtoHolder> prototypes = new EArrayList<>();
	
	//---------------------
	// Static Helper Class
	//---------------------
	
	public static class ProtoHolder {
		private final String name;
		private final IDatatype r_type;
		private final ParameterData paramData;
		private Class<? extends InstanceFunction> dynamicClass;
		private final List<Box2<IDatatype, ParameterData>> overloads = new EArrayList<>();
		
		public ProtoHolder(String nameIn, IDatatype r_typeIn, ParameterData paramDataIn) {
			name = nameIn;
			r_type = r_typeIn;
			paramData = paramDataIn;
		}
		
		public ProtoHolder assignDynamicClass(Class<? extends InstanceFunction> dynamicClassIn) {
			dynamicClass = dynamicClassIn;
			return this;
		}
		
		public ProtoHolder addOverload(IDatatype rType, IDatatype... params) {
			return addOverload(rType, new ParameterData(params));
		}
		
		public ProtoHolder addOverload(IDatatype rType, ParameterData params) {
			overloads.add(new Box2<>(rType, params));
			return this;
		}
	}
	
	//--------------
	// Constructors
	//--------------
	
	public IPrototypeHandler() {}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Creates a new internal function prototype definition
	 * with the given function name.
	 * <p>
	 * Note: Duplicate function names will not throw any kind
	 * of error when being added. A duplicate internal prototype
	 * will simply overwrite the previous function prototype name.
	 * <p>
	 * Furthermore, it is the responsibility of the programmer
	 * to ensure that a defined prototype's name is not going to
	 * directly overwrite an already existing scope value.
	 * 
	 * @param func_name The name of the prototype to create
	 */
	public ProtoHolder define(String func_name) {
		return define(func_name, StaticTypes.VAR_TYPE, new ParameterData());
	}
	
	public ProtoHolder define(String func_name, IDatatype rType) {
		return define(func_name, rType, new ParameterData());
	}
	
	public ProtoHolder define(String func_name, IDatatype rType, IDatatype... params) {
		return define(func_name, rType, new ParameterData(params));
	}
	
	public ProtoHolder define(String func_name, IDatatype rType, ParameterData params) {
		var p = new ProtoHolder(func_name, rType, params);
		return prototypes.addR(p);
	}
	
	/**
	 * Defines this handler's set of prototypes onto the given class instance's
	 * scope.
	 * 
	 * @param inst The class instance for which this handler is defining on
	 */
	public void defineOn(ClassInstance inst) {
		defineOn(inst.getScope());
	}
	
	/**
	 * Defines this handler's set of prototypes onto the given scope.
	 * 
	 * @param scope The scope for which this handler is defining on
	 */
	public void defineOn(Scope scope) {
		for (var p : prototypes) {
			var proto = new FunctionPrototype(p.name, p.r_type, p.paramData);
			proto.assignDynamicClass(p.dynamicClass);
			for (var o : p.overloads) proto.addOverload(o.getA(), o.getB());
			scope.define(p.name, proto);
		}
	}
	
}
