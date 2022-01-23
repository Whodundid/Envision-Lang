package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_x_def {
	
	public static void x_def(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case LDEF:
		case CDEF:
		case IDEF: def0(vm, code, args); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void def0(EnvisionVM vm, ByteCode code, String[] args) {
		BC_ArgParser.expect(args, 2);
		
		String[] parsed = BC_ArgParser.parseDef(args[1]);
		
		int pos = Integer.parseInt(args[0]);
		String name = parsed[0];
		String type = parsed[1];
		
		switch (code) {
		case LDEF: vm.scope().ldef(pos, name, type, vm.getVisType()); break;
		case CDEF: vm.scope().cdef(pos, name, type, vm.getVisType()); break;
		case IDEF: vm.scope().idef(pos, name, type, vm.getVisType()); break;
		default: break;
		}
	}
	
}
