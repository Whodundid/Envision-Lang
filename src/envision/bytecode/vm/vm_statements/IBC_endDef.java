package envision.bytecode.vm.vm_statements;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_ArgParser;
import envision.bytecode.vm.EnvisionVM;
import envision.exceptions.EnvisionError;

public class IBC_endDef {
	
	public static void endDef(EnvisionVM vm, ByteCode code, String[] args) {
		switch (code) {
		case _LOCAL:
		case _PUBLIC:
		case _PROTECTED:
		case _PRIVATE: BC_ArgParser.expect(args, 0); vm.setVis(0); break;
		case _STATIC: BC_ArgParser.expect(args, 0); vm.setStatic(false); break;
		case _METH: break;
		case _CONST: break;
		case _OP: break;
		default: throw new EnvisionError("Invalid Bytecode statement! " + code);
		}
	}
	
}
