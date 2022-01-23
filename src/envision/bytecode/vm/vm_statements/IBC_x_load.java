package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.util.BC_Scope;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import eutil.datatypes.EArrayList;

public class IBC_x_load {
	
	public static void x_load(EnvisionVM vm, ByteCode code, String[] args) {
		EArrayList<EnvisionObject> stack = vm.stack();
		BC_Scope scope = vm.scope();
		
		switch (code) {
		case LLOAD:
		case CLOAD:
		case ILOAD: load0(vm, code, args); break;
		//case ALOAD_0: stack.push(scope.lload(0)); break;
		//case ALOAD_1: stack.push(scope.lload(1)); break;
		//case ALOAD_2: stack.push(scope.lload(2)); break;
		case LLOAD_0: expect(args, 0); stack.push(scope.lload(0)); break;
		case LLOAD_1: expect(args, 0); stack.push(scope.lload(1)); break;
		case LLOAD_2: expect(args, 0); stack.push(scope.lload(2)); break;
		case CLOAD_0: expect(args, 0); stack.push(scope.cload(0)); break;
		case CLOAD_1: expect(args, 0); stack.push(scope.cload(1)); break;
		case CLOAD_2: expect(args, 0); stack.push(scope.cload(2)); break;
		case ILOAD_0: expect(args, 0); stack.push(scope.iload(0)); break;
		case ILOAD_1: expect(args, 0); stack.push(scope.iload(1)); break;
		case ILOAD_2: expect(args, 0); stack.push(scope.iload(2)); break;
		//case ALOAD_0_1: stack.push(scope.iload(0)).push(scope.iload(1)); break;
		case LLOAD_0_1: expect(args, 0); stack.pushRT(scope.lload(0)).push(scope.lload(1)); break;
		case CLOAD_0_1: expect(args, 0); stack.pushRT(scope.cload(0)).push(scope.cload(1)); break;
		case ILOAD_0_1: expect(args, 0); stack.pushRT(scope.iload(0)).push(scope.iload(1)); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void load0(EnvisionVM vm, ByteCode code, String[] args) {
		expect(args, 1);
		
		int pos = Integer.parseInt(args[0]);
		
		switch (code) {
		case LLOAD: vm.stack().push(vm.scope().lload(pos)); break;
		case CLOAD: vm.stack().push(vm.scope().cload(pos)); break;
		case ILOAD: vm.stack().push(vm.scope().iload(pos)); break;
		default: break;
		}
	}
	
	private static void expect(String[] args, int num) {
		BC_ArgParser.expect(args, num);
	}
	
}
