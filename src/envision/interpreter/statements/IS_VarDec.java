package envision.interpreter.statements;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.DuplicateObjectError;
import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.UndefinedTypeError;
import envision.exceptions.errors.VoidAssignmentError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.CastingUtil;
import envision.interpreter.util.TypeManager;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.objects.EnvisionVoidObject;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.data.DataModifier;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statementUtil.VariableDeclaration;
import envision.parser.statements.statements.VariableStatement;
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
		TypeManager typeMan = interpreter.getTypeManager();
		
		//extract statement base parts
		ParserDeclaration statement_declaration = statement.getDeclaration();
		EArrayList<VariableDeclaration> unprocessed_var_declarations = statement.vars;
		EnvisionDatatype var_dec_datatype = new EnvisionDatatype(statement_declaration.getReturnType());
		
		//if the rType correspondes to a specific user defined type, grab its class
		EnvisionClass typeClass = typeMan.getTypeClass(var_dec_datatype);
		
		
		
		//process each variable declaration
		for (VariableDeclaration d : unprocessed_var_declarations) {
			
			//---------------------------------------------------------
			
			//the name of the variable to be declared
			String var_name = d.getName();
			//this is the evaluated result of the assignment value -- if there is one
			Object assignment_value = null;
			//dynamically determine type for type checking
			EnvisionDatatype assignment_value_datatype = null;
			
			//---------------------------------------------------------
			
			
			//check that the variable doesn't already exist locally
			if (scope().existsLocally(var_name)) throw new DuplicateObjectError(var_name);
			
			//evaluate the assignment value (if there is one)
			if (d.assignment_value != null) {
				assignment_value = evaluate(d.assignment_value);
				
				//don't allow void assignment
				if (assignment_value instanceof EnvisionVoidObject) throw new VoidAssignmentError(var_name);
				
				//convert variable objects to their primitives
				if (assignment_value instanceof EnvisionVariable env_var) assignment_value = env_var.get();
				
				//determine the type of the assignment value
				assignment_value_datatype = EnvisionDatatype.dynamicallyDetermineType(assignment_value);
				
				//check that the assignment value actually matches the variable type being created
				CastingUtil.assert_expected_datatype(var_dec_datatype, assignment_value_datatype);
				
				//convert variable type to primitive Java types
				if (assignment_value instanceof EnvisionVariable v) assignment_value = v.get();
			}
			
			//throw error if the type is either null, isn't a primitive type, or isn't defined within the interpreter
			if (isNull(var_dec_datatype)) {
				throw new UndefinedTypeError("No type defined for the variable(s): " + unprocessed_var_declarations);
			}
			else if (!var_dec_datatype.isPrimitiveType() && !typeMan.isTypeDefined(var_dec_datatype)) {
				throw new UndefinedTypeError("No type defined for the variable(s): " + unprocessed_var_declarations);
			}
			
			
			//---------------------------------------------------------
			
			
			//create the default instance of the variable : defaults to a null object
			EnvisionObject obj = new EnvisionNullObject();
			
			//create the variable with its initial value(s)
			if (assignment_value != null) {
				//first check if list
				if (assignment_value instanceof EnvisionList env_list) {
					obj = env_list;
				}
				//if it's a class instance, assign the instance name
				else if (assignment_value instanceof ClassInstance env_class_inst) {
					env_class_inst.setName(var_name);
				}
				//check for standard object type
				else if (assignment_value instanceof EnvisionObject env_obj) {
					obj = env_obj;
					
					//if typeClass isn't null, check that the types match
					checkClassType(typeClass, assignment_value);
				}
				//otherwise, create new object with the given assignment_value
				else {
					obj = ObjectCreator.createObject(var_name, var_dec_datatype, assignment_value, false);
				}
			}
			//the assignment_value is null -- handle as if class defined type
			else {
				//assign default value
				obj = ObjectCreator.createDefault(var_name, var_dec_datatype, false);
			}
			
			
			//---------------------------------------------------------
			
			
			//assign name
			obj.setName(var_name);
			
			//Set visibility
			obj.setVisibility(statement_declaration.getVisibility());
			
			//Set data modifiers
			for (DataModifier m : statement_declaration.getMods()) {
				switch (m) {
				case FINAL: obj.setFinal(); break;
				case STATIC: obj.setStatic(); break;
				case STRONG: obj.setStrong(); break;
				default:
					throw new EnvisionError("Invalid variable data modifier '" + m + "'");
				}
			}
			
			//define the object within the current interpreter scope
			scope().define(var_name, var_dec_datatype, obj);
		}
	}
	
	private void checkClassType(EnvisionClass typeClass, Object value) {
		//if typeClass isn't null, check that the types match
		if (typeClass != null) {
			if (!typeClass.isInstanceof(value))
				throw new InvalidDatatypeError("Invalid type: '" + value +
											   "' expected '" + typeClass.getTypeString() + "'!");
		}
	}
	
}
