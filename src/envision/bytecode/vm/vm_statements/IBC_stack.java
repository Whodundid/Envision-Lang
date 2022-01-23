package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionString;
import eutil.datatypes.util.EDataType;

public class IBC_stack {
	
	public static void stack(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case PUSH_I: BC_ArgParser.expect(args, 1); push(vm, EnvisionDataType.INT, args[0]); break;
		case PUSH_D: BC_ArgParser.expect(args, 1); push(vm, EnvisionDataType.DOUBLE, args[0]); break;
		case PUSH_C: BC_ArgParser.expect(args, 1); push(vm, EnvisionDataType.CHAR, args[0]); break;
		case PUSH_S: BC_ArgParser.expect(args, 1); push(vm, EnvisionDataType.STRING, args[0]); break;
		case POP: BC_ArgParser.expect(args, 0); vm.stack().pop(); break;
		case CONCAT: BC_ArgParser.expect(args, 1); concat(vm, args[0]); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void push(EnvisionVM vm, EnvisionDataType type, String val) {
		EDataType valType = EDataType.getStringDataType(val);
		Object obj = EDataType.castTo(val, valType);
		vm.stack().push(ObjectCreator.createObject(type, obj));
	}
	
	// consumes 2 elements on stack, concatenates their string values, then pushes the string onto the stack
	private static void concat(EnvisionVM vm, String val) {
		Object a = vm.stack().pop();
		Object b = vm.stack().pop();
		vm.stack().push(new EnvisionString(new StringBuilder().append(a).append(b).toString()));
	}
	
}
