package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_x_store {
	
	public static void x_store(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case LSTORE:
		case CSTORE:
		case ISTORE: store0(vm, code, args); break;
		case LSTORE_0: BC_ArgParser.expect(args, 0); vm.scope().lstore(0, vm.stack().pop()); break;
		case LSTORE_1: BC_ArgParser.expect(args, 0); vm.scope().lstore(1, vm.stack().pop()); break;
		case LSTORE_2: BC_ArgParser.expect(args, 0); vm.scope().lstore(2, vm.stack().pop()); break;
		case CSTORE_0: BC_ArgParser.expect(args, 0); vm.scope().cstore(0, vm.stack().pop()); break;
		case CSTORE_1: BC_ArgParser.expect(args, 0); vm.scope().cstore(1, vm.stack().pop()); break;
		case CSTORE_2: BC_ArgParser.expect(args, 0); vm.scope().cstore(2, vm.stack().pop()); break;
		case ISTORE_0: BC_ArgParser.expect(args, 0); vm.scope().istore(0, vm.stack().pop()); break;
		case ISTORE_1: BC_ArgParser.expect(args, 0); vm.scope().istore(1, vm.stack().pop()); break;
		case ISTORE_2: BC_ArgParser.expect(args, 0); vm.scope().istore(2, vm.stack().pop()); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void store0(EnvisionVM vm, ByteCode code, String[] args) {
		BC_ArgParser.expect(args, 1);
		
		int pos = Integer.parseInt(args[0]);
		
		switch (code) {
		case LSTORE: vm.scope().lstore(pos, vm.stack().pop()); break;
		case CSTORE: vm.scope().cstore(pos, vm.stack().pop()); break;
		case ISTORE: vm.scope().istore(pos, vm.stack().pop()); break;
		default: break;
		}
	}
	
}
