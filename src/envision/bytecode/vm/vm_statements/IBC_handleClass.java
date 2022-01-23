package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_handleClass {
	
	public static void handleClass(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case NEW:
		case INVOKE:
		case INVOKECLASS:
		case RET: class0(vm, code, args);
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void class0(EnvisionVM vm, ByteCode code, String[] args) {
		
	}
}
