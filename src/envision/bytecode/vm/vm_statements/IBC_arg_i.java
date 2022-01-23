package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_arg_i {
	
	public static void arg_i(EnvisionVM vm, ByteCode code, String[] args) {
		
		throw new EnvisionError("Invalid Bytecode statement! " + code);
	}
	
}
