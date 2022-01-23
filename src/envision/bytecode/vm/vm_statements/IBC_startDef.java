package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_startDef {
	
	public static void startDef(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case LOCAL:     BC_ArgParser.expect(args, 0); vm.setVis(0); break;
		case PUBLIC:    BC_ArgParser.expect(args, 0); vm.setVis(1); break; 
		case PROTECTED: BC_ArgParser.expect(args, 0); vm.setVis(2); break;
		case PRIVATE:   BC_ArgParser.expect(args, 0); vm.setVis(3); break;
		case STATIC:    BC_ArgParser.expect(args, 0); vm.setStatic(true); break;
		case METH:      methDef(vm, args); break;
		case CONST:     constDef(vm, args); break;
		case OP:        opDef(vm, args); break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
	private static void methDef(EnvisionVM vm, String[] args) {
		BC_ArgParser.expect(args, 1);
	}
	
	private static void constDef(EnvisionVM vm, String[] args) {
		BC_ArgParser.expect(args, 1);
	}
	
	private static void opDef(EnvisionVM vm, String[] args) {
		BC_ArgParser.expect(args, 1);
	}
	
}
