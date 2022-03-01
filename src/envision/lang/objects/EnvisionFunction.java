package envision.lang.objects;

import static envision.lang.util.Primitives.*;

import envision.exceptions.errors.InvalidArgumentError;
import envision.exceptions.errors.NoOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.CastingUtil;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.scope.Scope;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;
import envision.lang.util.data.ParameterData;
import envision.lang.util.structureTypes.InvocableObject;
import envision.parser.statements.Statement;
import envision.tokenizer.Operator;
import eutil.datatypes.EArrayList;

public class EnvisionFunction extends InvocableObject {

	protected EArrayList<Statement> statements = new EArrayList();
	protected ParameterData params = new ParameterData();
	protected EnvisionDatatype returnType;
	protected Scope methodScope;
	protected EArrayList<EnvisionFunction> overloads = new EArrayList();
	protected EnvisionClass parentClass = null;
	protected Operator operatorKeyword;
	protected boolean isConstructor = false;
	protected boolean isOperator = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionFunction(String nameIn) {
		this(EnvisionDatatype.prim_var(), nameIn, new ParameterData());
	}
	
	public EnvisionFunction(Primitives rTypeIn, String nameIn) {
		this(new EnvisionDatatype(rTypeIn), nameIn, new ParameterData());
	}
	
	public EnvisionFunction(Primitives rTypeIn, String nameIn, ParameterData paramsIn) {
		this(new EnvisionDatatype(rTypeIn), nameIn, paramsIn);
	}
	
	public EnvisionFunction(EnvisionDatatype rTypeIn, String nameIn) {
		this(rTypeIn, nameIn, new ParameterData());
	}
	
	public EnvisionFunction(EnvisionDatatype rTypeIn, String nameIn, ParameterData paramsIn) {
		super(new EnvisionDatatype(Primitives.FUNCTION), nameIn);
		returnType = rTypeIn;
		setParams(paramsIn);
	}
	
	/** Used to help create method overrides. */
	public EnvisionFunction(EnvisionFunction mIn, ParameterData paramsIn) {
		super(new EnvisionDatatype(Primitives.FUNCTION), mIn.name);
		returnType = mIn.returnType;
		setParams(paramsIn);
		isConstructor = mIn.isConstructor;
		isOperator = mIn.isOperator;
		operatorKeyword = mIn.operatorKeyword;
		methodScope = mIn.methodScope;
	}
	
	/** Exclusive to class constructors */
	public EnvisionFunction(ParameterData paramsIn) {
		super(new EnvisionDatatype(Primitives.FUNCTION), "init");
		setParams(paramsIn);
		isConstructor = true;
	}
	
	/** Exclusive to operator overloads. */
	public EnvisionFunction(Operator operatorIn, ParameterData paramsIn) {
		super(new EnvisionDatatype(Primitives.FUNCTION), "operator " + operatorIn.chars);
		setParams(paramsIn);
		operatorKeyword = operatorIn;
		isOperator = true;
	}
	
	private EnvisionFunction(EnvisionFunction toCopy) {
		super(new EnvisionDatatype(Primitives.FUNCTION), toCopy.getName());
		statements = toCopy.statements;
		returnType = toCopy.returnType;
		setParams(toCopy.params);
		isConstructor = toCopy.isConstructor;
		isOperator = toCopy.isOperator;
		operatorKeyword = toCopy.operatorKeyword;
		methodScope = toCopy.methodScope;
		
		for (EnvisionFunction overload : toCopy.overloads) {
			overloads.add(copy(overload));
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return name + params + getHexHash();
	}
	
	@Override
	public EnvisionFunction copy() {
		return new EnvisionFunction(this);
	}
	
	@Override
	public boolean equals(Object in) {
		if (!(in instanceof EnvisionFunction)) return false;
		
		EnvisionFunction m = (EnvisionFunction) in;
		return m.name.equals(name) &&
			   m.params.compare(params) &&
			   m.isConstructor == isConstructor &&
			   m.isOperator == isOperator;
	}
	
	//-------------------
	// Protected Methods
	//-------------------
	
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		//im(new InternalMethod(returnType, "invoke", true) { protected void body(EArrayList a) { call(in)}});
		im(new InternalMethod(BOOLEAN, "hasOverloads") { protected void body(Object[] a) { ret(overloads.isNotEmpty()); }});
		im(new InternalMethod(LIST, "getOverloads") { protected void body(Object[] a) { ret(new EnvisionList(FUNCTION).addAll(overloads)); }});
		im(new InternalMethod(INT, "getOverloads") { protected void body(Object[] a) { ret(argLength()); }});
		im(new InternalMethod(STRING, "getReturnType") { protected void body(Object[] a) { ret(getReturnType()); }});
		im(new InternalMethod(LIST, "getParamTypes") { protected void body(Object[] a) { ret(new EnvisionList(STRING).addAll(getParamTypes())); }});
		im(new InternalMethod(BOOLEAN, "getParamNames") { protected void body(Object[] a) { ret(new EnvisionList(STRING).addAll(getParamNames())); }});
	}
	
	//--------------------------
	// Method Overload Handling
	//--------------------------
	
	public EnvisionFunction addOverload(ParameterData paramsIn, EArrayList<Statement> bodyIn) {
		return overloads.addIfR(!hasOverload(paramsIn), makeOverload(paramsIn, bodyIn), this);
	}
	
	public EnvisionFunction makeOverload(ParameterData paramsIn, EArrayList<Statement> bodyIn) {
		return new EnvisionFunction(this, paramsIn).setBody(bodyIn);
	}
	
	public boolean hasOverload(ParameterData dataIn) { return (getOverload(dataIn) != null); }

	/** Attempts to return an overload with parameters matching the given arguments. */
	public EnvisionFunction getOverloadFromArgs(EArrayList<EnvisionObject> args) {
		ParameterData callParams = (args != null) ? new ParameterData(args) : new ParameterData();
		
		//check if the base method parameters match the given arguments
		if (params.compare(callParams)) return this;
		
		//if not, check for any overloads
		EnvisionFunction m = null;
		
		m = getOverload(callParams);
		//if there are no overloads with matching parameters -- throw an error
		if (m == null) throw new NoOverloadError(this, callParams);
		
		return m;
	}
	
	/** Attempts to find an overload of the same method name but with different parameters. */
	public EnvisionFunction getOverload(ParameterData dataIn) {
		for (EnvisionFunction overload : overloads) {
			if (overload.argLength() == dataIn.size()) {
				if (overload.params.compare(dataIn)) {
					return overload;
				}
			}
		}
		return null;
	}
	
	//---------
	// Methods
	//---------
	
	/** Attempts to run the method with the given arguments. */
	//public void invoke(EnvisionInterpreter interpreter, Object... args) {
	//	EArrayList pass = new EArrayList();
	//	for (Object o : args) { pass.addIfNotNull(o); }
	//	
	//	invoke(interpreter, pass);
	//}
	
	/**
	 * Attempts to run the method with the given arguments.
	 */
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		Scope scope = new Scope(methodScope);
		
		EArrayList<EnvisionObject> callArgs = ObjectCreator.createArgs(args);
		EnvisionFunction m = getOverloadFromArgs(callArgs);
		
		EArrayList<String> names = m.params.getNames();
		for (int i = 0; i < m.params.size(); i++) {
			String n = names.get(i);
			scope.define(n, callArgs.get(i).setName(n));
		}
		
		//if this is a constructor, check if any of the arguments are assignment args
		//as in if any argument name matches an existing body level variable -- assign it directly
		if (isConstructor) {
			for (EnvisionObject objArg : scope.values()) {
				Scope ps = scope.getParentScope();
				String n = objArg.getName();
				var scopeValue = ps.getTyped(objArg.getName());
				
				//ensure that the scope entry exists
				if (scopeValue != null) {
					EnvisionDatatype scope_var_type = scopeValue.getA();
					EnvisionDatatype incomming_type = objArg.getDatatype();
					
					//ensure that the scope entry's type and the incoming type match
					if (scope_var_type.compareType(incomming_type)) {
						ps.set(n, objArg);
					}
					else if (EnvisionDatatype.isNumber(scopeValue.getA())) {
						EnvisionObject obj = CastingUtil.castToNumber(objArg, scopeValue.getA());
						ps.set(n, obj);
					}
					else {
						throw new InvalidArgumentError("'" + getName() + ":" + "' Invalid argument! Expected '" +
													   scopeValue.getA() + "' but got '" + objArg.getTypeString() + "'!");
					}
				}
			}
		}
		
		try {
			interpreter.executeBlock(m.statements, scope);
		}
		catch (ReturnValue r) {
			if (isConstructor) ret(scope.get("this"));
			ret(r.object);
		}
		
		if (isConstructor) ret(scope.get("this"));
	}
	
	public EnvisionFunction addStatement(Statement statementIn) {
		return statements.addR(statementIn, this);
	}
	
	//---------
	// Getters
	//---------
	
	public Scope getScope() { return methodScope; }
	public ParameterData getParams() { return params; }
	public EArrayList<Statement> getStatements() { return statements; }
	public EArrayList<EnvisionFunction> getOverloads() { return overloads; }
	public EArrayList<Statement> getBody() { return statements; }
	public EArrayList<EnvisionDatatype> getParamTypes() { return params.getDataTypes(); }
	public EArrayList<String> getParamNames() { return params.getNames(); }
	@Override public int argLength() { return params.size(); }
	public EnvisionDatatype getReturnType() { return returnType; }
	public boolean isVoid() { return Primitives.getDataType(returnType) == Primitives.VOID; }
	public boolean isConstructor() { return isConstructor; }
	public boolean isOperator() { return isOperator; }
	public Operator getOperator() { return operatorKeyword; }
	
	//---------
	// Setters
	//---------
	
	public EnvisionFunction setScope(Scope scopeIn) { methodScope = scopeIn; return this; }
	public EnvisionFunction setBody(EArrayList<Statement> in) { statements.clear(); statements.addAll(in); return this; }
	public EnvisionFunction setParams(ParameterData dataIn) { params.clear(); params.set(dataIn); return this; }
	public EnvisionFunction assignParentClass(EnvisionClass parentClassIn) { parentClass = parentClassIn; return this; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static EnvisionFunction copy(EnvisionFunction m) {	
		return new EnvisionFunction(m);
	}
	
}
