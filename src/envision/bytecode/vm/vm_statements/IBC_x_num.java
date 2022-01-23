package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_x_num {
	
	public static void x_num(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case LNUM: BC_ArgParser.expect(args, 1); vm.scope().lnum(Integer.parseInt(args[0])); break;
		case CNUM: BC_ArgParser.expect(args, 1); vm.scope().cnum(Integer.parseInt(args[0])); break;
		case INUM: BC_ArgParser.expect(args, 1); vm.scope().inum(Integer.parseInt(args[0])); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
}
