package envision.interpreter.statements;

import envision.exceptions.errors.DuplicateObjectError;
import envision.exceptions.errors.InvalidDataTypeError;
import envision.exceptions.errors.UndefinedTypeError;
import envision.exceptions.errors.VoidAssignmentError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.CastingUtil;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.creationUtil.VariableCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.objects.EnvisionVoidObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.DataModifier;
import envision.lang.variables.EnvisionVariable;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statementUtil.VariableDeclaration;
import envision.parser.statements.types.VariableStatement;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class IS_VarDec extends StatementExecutor<VariableStatement> {
	
	public IS_VarDec(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, VariableStatement s) {
		new IS_VarDec(in).run(s);
	}

	@Override
	public void run(VariableStatement statement) {
		ParserDeclaration dec = statement.getDeclaration();
		EArrayList<VariableDeclaration> vars = statement.vars;
		EnvisionDataType type = dec.getReturnType().getDataType();
		String rType = dec.getReturnType().lexeme;
		
		//if the rType correspondes to a specific user defined type, grab its class
		EnvisionClass typeClass = interpreter.getTypeManager().getTypeClass(rType);
		
		//process each variable declaration
		for (VariableDeclaration d : vars) {
			String name = d.name.lexeme;
			Object value = d.value;
			
			//check that the variable doesn't already exist
			if (scope().exists(name)) throw new DuplicateObjectError(name);
			
			//determine the value (if there is one)
			if (d.value != null) {
				value = evaluate(d.value);
				if (value instanceof EnvisionVoidObject) { throw new VoidAssignmentError(name); }
			}
			
			//create the default instance of the variable : defaults to null object
			EnvisionObject obj = new EnvisionNullObject();
			
			//check that the type exists in some capacity
			if ((type == null || type == EnvisionDataType.NULL) && !interpreter.getTypeManager().isTypeDefined(rType)) {
				throw new UndefinedTypeError("The type '" + rType + "' is not defined within the current scope!");
			}
			
			//check if the value being assigned isn't null
			if (value != null) {
				//first, check if the 'typeClass' exists -- if so check for user defined assignment operator overloads
				//if (typeClass != null) {
				//	System.out.println("here: " + name + " = " + value);
				//	OverloadHandler.handleAssignmentOverload(interpreter, name, typeClass, value);
				//}
				//else {
					//check that the created object is actually the same as the object it is being assigned to
					if (type != null) { CastingUtil.checkType(rType, value); }
					
					if (type == EnvisionDataType.LIST) {
						//first check if the value is a list
						if (value instanceof EnvisionList) {
							obj = ((EnvisionList) value).setName(name);
						}
						else {
							Token param = dec.getParams().getFirst();
							if (param != null) { obj = VariableCreator.createList(scope().getType(param.lexeme), name); }
							else { obj = VariableCreator.createList(name); }
							
							//EnvisionList l = (EnvisionList) obj;
						}
					}
					else if (value instanceof EnvisionVariable)
						obj = ObjectCreator.createObject(rType, name, EnvisionObject.convert(value));
					else if (value instanceof EnvisionObject) {
						obj = (EnvisionObject) value;
						
						//if typeClass isn't null, check that the types match
						checkClassType(typeClass, value);
						
						// if it's a class instance, assign the instance name
						if (value instanceof ClassInstance) {
							ClassInstance inst = (ClassInstance) value;
							inst.setName(name);
						}
					}
					else if (type != null) { obj = ObjectCreator.createObject(rType, name, value); }
					else {
						checkClassType(typeClass, value);
						obj = ObjectCreator.createObject(name, value);
					}
				//}
				
				//Set Visibility
				obj.setVisibility(dec.getVisibility());
				
				//Set data modifiers
				for (DataModifier m : dec.getMods()) {
					switch (m) {
					case FINAL: obj.setFinal(); break;
					case STATIC: obj.setStatic(); break;
					case STRONG: obj.setStrong(); break;
					default: break; // ERROR
					}
				}
				
				//define the variable with the resulting obj value
				if (obj instanceof ClassInstance) scope().define(name, obj.getTypeString(), obj);
				else scope().define(name, type, obj);
			}
			//the value is null -- treat as if class defined type
			else {
				//if the declaration type is null -- this is an error
				if (rType == null) throw new UndefinedTypeError("No type defined for the variable(s): " + vars);
				//if the EnvisionDataType is not null -- this is a standard envision datatype -- assign type's default state
				if (type != null) scope().define(name, rType, ObjectCreator.createDefault(name, type));
				//if the EnvisionDataType is null -- this is a class defined type -- set as null
				else scope().define(name, rType, new EnvisionNullObject());
			}
		}
	}
	
	private void checkClassType(EnvisionClass typeClass, Object value) {
		//if typeClass isn't null, check that the types match
		if (typeClass != null) {
			if (!typeClass.isInstanceof(value))
				throw new InvalidDataTypeError("Invalid type: '" + value +
											   "' expected '" + typeClass.getTypeString() + "'!");
		}
	}
	
}
