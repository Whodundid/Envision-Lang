package envision.lang.objects;

import static envision.lang.util.EnvisionDataType.*;

import envision.exceptions.errors.InvalidArgumentError;
import envision.exceptions.errors.NoOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.Scope;
import envision.interpreter.util.creationUtil.CastingUtil;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalMethod;
import envision.lang.util.data.ParameterData;
import envision.lang.util.structureTypes.CallableObject;
import envision.parser.statements.Statement;
import envision.tokenizer.Keyword;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxHolder;

public class EnvisionMethod extends EnvisionObject implements CallableObject {

	protected EArrayList<Statement> statements = new EArrayList();
	protected ParameterData params = new ParameterData();
	protected String returnType;
	protected Scope methodScope;
	protected EArrayList<EnvisionMethod> overloads = new EArrayList();
	protected Keyword operatorKeyword;
	protected boolean isConstructor = false;
	protected boolean isOperator = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionMethod(String nameIn) { super(EnvisionDataType.METHOD, nameIn); }
	public EnvisionMethod(EnvisionDataType returnTypeIn, String nameIn) { this(returnTypeIn.type, nameIn, new ParameterData()); }
	public EnvisionMethod(String returnTypeIn, String nameIn) { this(returnTypeIn, nameIn, new ParameterData()); }
	public EnvisionMethod(EnvisionDataType returnTypeIn, String nameIn, ParameterData paramsIn) { this(returnTypeIn.type, nameIn, paramsIn); }
	public EnvisionMethod(String returnTypeIn, String nameIn, ParameterData paramsIn) {
		super(EnvisionDataType.METHOD, nameIn);
		returnType = returnTypeIn;
		setParams(paramsIn);
	}
	
	/** Used to help create method overrides. */
	public EnvisionMethod(EnvisionMethod mIn, ParameterData paramsIn) {
		super(EnvisionDataType.METHOD, mIn.name);
		returnType = mIn.returnType;
		setParams(paramsIn);
		isConstructor = mIn.isConstructor;
		isOperator = mIn.isOperator;
		operatorKeyword = mIn.operatorKeyword;
		methodScope = mIn.methodScope;
	}
	
	/** Exclusive to class constructors */
	public EnvisionMethod(ParameterData paramsIn) {
		super(EnvisionDataType.METHOD, "init");
		setParams(paramsIn);
		isConstructor = true;
	}
	
	/** Exclusive to operator overloads. */
	public EnvisionMethod(Keyword operatorIn, ParameterData paramsIn) {
		super(EnvisionDataType.METHOD, "operator " + operatorIn.chars);
		setParams(paramsIn);
		operatorKeyword = operatorIn;
		isOperator = true;
	}
	
	private EnvisionMethod(EnvisionMethod toCopy) {
		super(EnvisionDataType.METHOD, toCopy.getName());
		statements = toCopy.statements;
		returnType = toCopy.returnType;
		setParams(toCopy.params);
		isConstructor = toCopy.isConstructor;
		isOperator = toCopy.isOperator;
		operatorKeyword = toCopy.operatorKeyword;
		methodScope = toCopy.methodScope;
		
		for (EnvisionMethod overload : toCopy.overloads) {
			overloads.add(copy(overload));
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public String toString() { return name + params + getHexHash(); }
	
	@Override
	public EnvisionMethod copy() {
		return new EnvisionMethod(this);
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		invoke(interpreter, args);
	}
	
	@Override
	public boolean equals(Object in) {
		if (!(in instanceof EnvisionMethod)) return false;
		
		EnvisionMethod m = (EnvisionMethod) in;
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
		im(new InternalMethod(LIST, "getOverloads") { protected void body(Object[] a) { ret(new EnvisionList(METHOD).addAll(overloads)); }});
		im(new InternalMethod(INT, "getOverloads") { protected void body(Object[] a) { ret(argSize()); }});
		im(new InternalMethod(STRING, "getReturnType") { protected void body(Object[] a) { ret(getReturnType()); }});
		im(new InternalMethod(LIST, "getParamTypes") { protected void body(Object[] a) { ret(new EnvisionList(STRING).addAll(getParamTypes())); }});
		im(new InternalMethod(BOOLEAN, "getParamNames") { protected void body(Object[] a) { ret(new EnvisionList(STRING).addAll(getParamNames())); }});
	}
	
	//--------------------------
	// Method Overload Handling
	//--------------------------
	
	public EnvisionMethod addOverload(ParameterData paramsIn, EArrayList<Statement> bodyIn) {
		return overloads.addIfR(!hasOverload(paramsIn), makeOverload(paramsIn, bodyIn), this);
	}
	
	public EnvisionMethod makeOverload(BoxHolder<String, String> paramsIn, EArrayList<Statement> bodyIn) {
		return makeOverload(ParameterData.of(paramsIn), bodyIn);
	}
	
	public EnvisionMethod makeOverload(ParameterData paramsIn, EArrayList<Statement> bodyIn) {
		return new EnvisionMethod(this, paramsIn).setBody(bodyIn);
	}
	
	public boolean hasOverload(BoxHolder<String, String> paramsIn) { return hasOverload(ParameterData.of(paramsIn)); }
	public boolean hasOverload(ParameterData dataIn) { return (getOverload(dataIn) != null); }

	/** Attempts to return an overload with parameters matching the given arguments. */
	public EnvisionMethod getOverloadFromArgs(EArrayList<EnvisionObject> args) {
		ParameterData callParams = (args != null) ? new ParameterData(args) : new ParameterData();
		
		//check if the base method parameters match the given arguments
		if (params.compare(callParams)) return this;
		
		//if not, check for any overloads
		EnvisionMethod m = null;
		m = getOverload(callParams);
		//if there are no overloads with matching parameters -- throw an error
		if (m == null) { throw new NoOverloadError(this, callParams); }
		
		return m;
	}
	
	/** Attempts to find an overload of the same method name but with different parameters. */
	public EnvisionMethod getOverload(ParameterData dataIn) {
		for (EnvisionMethod overload : overloads) {
			if (overload.argSize() == dataIn.size()) {
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
	
	/** Attempts to run the method with the given arguments. */
	public void invoke(EnvisionInterpreter interpreter, Object... args) {
		Scope scope = new Scope(methodScope);
		
		EArrayList<EnvisionObject> callArgs = ObjectCreator.createArgs(args);
		EnvisionMethod m = getOverloadFromArgs(callArgs);
		
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
				Box2<String, EnvisionObject> scopeValue = ps.getTyped(objArg.getName());
				
				//ensure that the scope entry exists
				if (scopeValue != null) {
					//ensure that the scope entry's type and the incoming type match
					if (CastingUtil.compare(scopeValue.getA(), objArg.getTypeString())) {
						ps.set(n, objArg);
					}
					else if (EnvisionDataType.isNumber(scopeValue.getA())) {
						EnvisionObject obj = CastingUtil.numberCast(objArg, scopeValue.getA());
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
	
	public EnvisionMethod addStatement(Statement statementIn) { return statements.addR(statementIn, this); }
	
	//---------
	// Getters
	//---------
	
	public Scope getScope() { return methodScope; }
	public ParameterData getParams() { return params; }
	public EArrayList<Statement> getStatements() { return statements; }
	public EArrayList<EnvisionMethod> getOverloads() { return overloads; }
	public EArrayList<Statement> getBody() { return statements; }
	public EArrayList<String> getParamTypes() { return params.getDataTypes(); }
	public EArrayList<String> getParamNames() { return params.getNames(); }
	@Override public int argSize() { return params.size(); }
	public String getReturnType() { return returnType; }
	public boolean isVoid() { return EnvisionDataType.getDataType(returnType) == EnvisionDataType.VOID; }
	public boolean isConstructor() { return isConstructor; }
	public boolean isOperator() { return isOperator; }
	public Keyword getOperator() { return operatorKeyword; }
	
	//---------
	// Setters
	//---------
	
	public EnvisionMethod setScope(Scope scopeIn) { methodScope = scopeIn; return this; }
	public EnvisionMethod setBody(EArrayList<Statement> in) { statements.clear(); statements.addAll(in); return this; }
	public EnvisionMethod setParams(BoxHolder<String, String> paramsIn) { params.clear(); params.addAll(paramsIn); return this; }
	public EnvisionMethod setParams(ParameterData dataIn) { params.clear(); params.set(dataIn); return this; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static EnvisionMethod makeOverload(EnvisionMethod baseFunction, BoxHolder<String, String> paramsIn, EArrayList<Statement> bodyIn) {
		return baseFunction.makeOverload(paramsIn, bodyIn);
	}
	
	public static EnvisionMethod copy(EnvisionMethod m) {	
		return new EnvisionMethod(m);
	}
	
}
