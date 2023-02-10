package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.CastingUtil;
import envision_lang.interpreter.util.UserDefinedTypeManager;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.datatypes.EnvisionVoid;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.AlreadyDefinedError;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.exceptions.errors.UndefinedTypeError;
import envision_lang.lang.exceptions.errors.VoidAssignmentError;
import envision_lang.lang.natives.DataModifier;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.parser.util.VariableDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class IS_VarDec extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_VarDef s) {
		UserDefinedTypeManager typeMan = interpreter.getTypeManager();
		
		//extract base statement parts
		ParserDeclaration statement_declaration = s.getDeclaration();
		EList<VariableDeclaration> unprocessed_var_declarations = s.vars;
		Token token_returntype = statement_declaration.getReturnType();
		IDatatype var_dec_datatype = NativeTypeManager.datatypeOf(token_returntype);
		
		//---------------------------------------------------------------------------------------------------------------
		
		//throw error if the type is either null, isn't a primitive type, or isn't defined within the interpreter
		if (isNull(var_dec_datatype)) {
			throw new UndefinedTypeError("No type defined for the variable(s): " + unprocessed_var_declarations);
		}
		//else if (!typeMan.isTypeDefined(var_dec_datatype)) {
		//	throw new UndefinedTypeError("The type '" + var_dec_datatype + "' is undefined within the current scope!");
		//}
		
		//if the rType corresponds to a specific user defined type, grab its class
		EnvisionClass typeClass = typeMan.getTypeClass(var_dec_datatype);
		
		//---------------------------------------------------------------------------------------------------------------
		
		//process each variable declaration
		int size = unprocessed_var_declarations.size();
		for (int i = 0; i < size; i++) {
			VariableDeclaration d = unprocessed_var_declarations.get(i);
			
			//---------------------------------------------------------
			
			//the name of the variable to be declared
			String var_name = d.getName();
			//this is the evaluated result of the assignment value -- if there is one
			EnvisionObject assignment_value = null;
			//dynamically determine type for type checking
			IDatatype assignment_value_datatype = null;
			
			//---------------------------------------------------------
			
			//check that the variable doesn't already exist locally
			if (interpreter.scope().existsLocally(var_name)) throw new AlreadyDefinedError(var_name);
			
			//evaluate the assignment value (if there is one)
			if (d.assignment_value != null) {
				assignment_value = interpreter.evaluate(d.assignment_value);
				
				//don't allow void assignment
				if (assignment_value instanceof EnvisionVoid) throw new VoidAssignmentError(var_name);
				
				//convert variable objects to their primitives
				if (assignment_value instanceof EnvisionVariable env_var) assignment_value = env_var.get();
				
				//determine the type of the assignment value
				assignment_value_datatype = IDatatype.dynamicallyDetermineType(assignment_value);
				
				//check that the assignment value actually matches the variable type being created
				CastingUtil.assert_expected_datatype(var_dec_datatype, assignment_value_datatype);
				
				//convert variable type to primitive Java types
				//if (assignment_value instanceof EnvisionVariable v) assignment_value = v.get();
			}
			
			
			//---------------------------------------------------------
			
			
			//create the default instance of the variable : defaults to a null object
			//EnvisionObject obj = obj = ObjectCreator.createDefault(var_dec_datatype);
			EnvisionObject obj;
			
			//check for null assignment
			if (assignment_value == EnvisionNull.NULL) {
				obj = EnvisionNull.NULL;
			}
			//create the variable with its initial value(s)
			else if (assignment_value != null) {
				//first check if list
				if (assignment_value instanceof EnvisionList env_list) {
					obj = env_list;
				}
				//handle primitive class instance creation separately
				else if (typeClass != null && typeClass.isPrimitive()) {
					EnvisionObject[] args = { assignment_value };
					obj = typeClass.newInstance(interpreter, args);
				}
				//if it's a class instance, assign the instance name
				else if (assignment_value instanceof ClassInstance env_class_inst) {
					//env_class_inst.setName(var_name);
					obj = env_class_inst;
				}
				//otherwise, create new object with the given assignment_value
				else {
					obj = assignment_value;
				}
			}
			//the assignment_value is null -- handle as if class defined type
			else {
				//assign default value
				//obj = ObjectCreator.createDefault(var_dec_datatype, false);
				obj = EnvisionNull.NULL;
			}
			
			
			//---------------------------------------------------------
			
			
			//assign name
			//obj.setName(var_name);
			
			//Set visibility
			obj.setVisibility(statement_declaration.getVisibility());
			
			//Set data modifiers
			for (DataModifier m : statement_declaration.getMods()) {
				switch (m) {
				case FINAL: obj.setFinal(); break;
				case STATIC: obj.setStatic(); break;
				case STRONG: obj.setStrong(); break;
				default:
					throw new EnvisionLangError("Invalid variable data modifier '" + m + "'");
				}
			}
			
			//define the object within the current interpreter scope
			interpreter.scope().define(var_name, var_dec_datatype, obj);
			//System.out.println("def Scope: " + interpreter.scope());
			//System.out.println("def scope: " + var_name + " : " + interpreter.scope().getTyped(var_name));
		}
	}
	
	private static void checkClassType(EnvisionClass typeClass, Object value) {
		//if typeClass isn't null, check that the types match
		if (typeClass != null) {
			if (!typeClass.isInstanceof(value))
				throw new InvalidDatatypeError("Invalid type: '" + value +
											   "' expected '" + typeClass.getTypeString() + "'!");
		}
	}
	
}
