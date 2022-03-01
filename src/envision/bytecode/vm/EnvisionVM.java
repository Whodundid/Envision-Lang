package envision.bytecode.vm;

import envision.bytecode.ByteCode;
import envision.bytecode.util.BC_Scope;
import envision.bytecode.vm.vm_statements.IBC_a_then_i;
import envision.bytecode.vm.vm_statements.IBC_arg_i;
import envision.bytecode.vm.vm_statements.IBC_endDef;
import envision.bytecode.vm.vm_statements.IBC_getset;
import envision.bytecode.vm.vm_statements.IBC_handleClass;
import envision.bytecode.vm.vm_statements.IBC_math;
import envision.bytecode.vm.vm_statements.IBC_stack;
import envision.bytecode.vm.vm_statements.IBC_startDef;
import envision.bytecode.vm.vm_statements.IBC_x_def;
import envision.bytecode.vm.vm_statements.IBC_x_load;
import envision.bytecode.vm.vm_statements.IBC_x_num;
import envision.bytecode.vm.vm_statements.IBC_x_store;
import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.util.VisibilityType;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class EnvisionVM implements ByteCodeStatementHandler {
	
	public static void interpretByteCode(File f) throws FileNotFoundException, IOException {
		EnvisionVM vm = new EnvisionVM();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
			int next = reader.read();
			
			while (next != -1) {
				ByteCode code = ByteCode.get(next);
				if (code != ByteCode.NEWLINE) {
					int pos = 0;
					byte[] parsedArgs = new byte[256];
					
					while (next != -1) {
						if ((next = reader.read()) == ByteCode.NEWLINE.code()) break;
						parsedArgs[pos++] = (byte) next;
						
						//check if overflow
						if (pos == parsedArgs.length) {
							byte[] bigger = new byte[parsedArgs.length * 4];
							System.arraycopy(parsedArgs, 0, bigger, 0, pos);
							parsedArgs = bigger;
						}
					}
					
					String combinedArgs = new String(parsedArgs, 0, pos, Charset.defaultCharset());
					String[] args = (!combinedArgs.trim().isEmpty()) ? combinedArgs.trim().split(" ") : new String[0];
					
					vm.execute(code, args);
				}
				
				next = reader.read();
			}
		}
	}
	
	public static void compileByteCode(File f) throws FileNotFoundException, IOException {
		File saveFile = new File(f.getParent(), f.getName() + 'c');
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
			PrintWriter writer = new PrintWriter(saveFile);
			String line = reader.readLine();
			
			//int i = 0;
			//byte[] bytes = new byte[(int) f.length()];
			
			while (line != null) {
				if (!line.trim().isEmpty()) {
					line = line.trim().replace("\t", "").replace("\n", "");
					//ignore comments
					if (line.startsWith("//")) {
						line = reader.readLine();
						continue;
					}
					
					int argEnd = StringUtil.findStartingIndex(line, " ");
					String instruction = (argEnd > 0) ? line.substring(0, argEnd) : line;
					String[] args = (argEnd > 0) ? line.substring(argEnd + 1).split(" ") : new String[0];
					
					ByteCode code = ByteCode.getCodeString(instruction);
					writer.print((char) code.code());
					for (String s : args) {
						writer.print(' ');
						for (byte b : s.getBytes()) writer.print((char) b);
					}
					writer.print((char) ByteCode.NEWLINE.code());
				}
				
				line = reader.readLine();
			}
			
			writer.close();
		}
	}
	
	//--------
	// Fields
	//--------
	
	private EnvisionVM vm;
	private BC_Scope scope = new BC_Scope();
	
	// 0 = local, 1 = public, 2 = protected, 3 = private
	private int vis = 0;
	// static
	private boolean stat = false;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionVM() {
		vm = this;
	}
	
	private void execute(ByteCode code, String[] args) {
		switch (code) {
		case SCOPE: pushScope(); break;
		case _SCOPE: popScope(); break;
		
		case ALOADI:
		case ASTOREI: a_then_i(code, args); break;
		
		case GETI:
		case STOREI: arg_i(code, args); break;
		
		case _LOCAL:
		case _PUBLIC:
		case _PROTECTED:
		case _PRIVATE:
		case _STATIC:
		case _CLASS:
		case _METH:
		case _CONST:
		case _OP: endDef(code, args); break;
		
		case CMAKEGET:
		case CMAKESET:
		case IMAKEGET:
		case IMAKESET: getset(code, args); break;
		
		case NEW:
		case INVOKE:
		case INVOKECLASS:
		case RET: handleClass(code, args); break;
		
		case ADD:
		case SUB:
		case MUL:
		case DIV: math(code, args); break;
		
		case PUSH_I:
		case PUSH_D:
		case PUSH_C:
		case PUSH_S:
		case POP:
		case CONCAT: stack(code, args); break;
		
		case LOCAL:
		case PUBLIC:
		case PROTECTED:
		case PRIVATE:
		case STATIC:
		case CLASS:
		case METH:
		case CONST:
		case OP: startDef(code, args); break;
		
		case LDEF:
		case CDEF:
		case IDEF: x_def(code, args); break;
		
		case ALOAD:
		case LLOAD:
		case CLOAD:
		case ILOAD:
		case ALOAD_0:
		case ALOAD_1: 
		case ALOAD_2:
		case LLOAD_0:
		case LLOAD_1: 
		case LLOAD_2:
		case CLOAD_0:
		case CLOAD_1: 
		case CLOAD_2:
		case ILOAD_0:
		case ILOAD_1: 
		case ILOAD_2:
		case ALOAD_0_1:
		case LLOAD_0_1:
		case CLOAD_0_1:
		case ILOAD_0_1: x_load(code, args); break;
		
		case LNUM: scope.lnum(Integer.parseInt(args[0])); break;
		case CNUM: scope.cnum(Integer.parseInt(args[0])); break;
		case INUM: scope.inum(Integer.parseInt(args[0])); break;
		
		case LSTORE:
		case CSTORE:
		case ISTORE:
		case LSTORE_0:
		case LSTORE_1:
		case LSTORE_2:
		case CSTORE_0:
		case CSTORE_1:
		case CSTORE_2:
		case ISTORE_0:
		case ISTORE_1:
		case ISTORE_2: x_store(code, args); break;
		
		case STACK:
			System.out.println("-------------------------------");
			for (int i = 0; i < stack().size(); i++) {
				System.out.println(stack().get(i));
			}
			System.out.println("-------------------------------");
			break;
		default:
			break;
		}
	}
	
	//---------
	// Methods
	//---------
	
	public int getVis() { return vis; }
	public VisibilityType getVisType() {
		switch (vis) {
		case 0: return VisibilityType.SCOPE;
		case 1: return VisibilityType.PUBLIC;
		case 2: return VisibilityType.PROTECTED;
		case 3: return VisibilityType.PRIVATE;
		default: throw new EnvisionError("Invalid bytecode visibility type! [" + vis + "]");
		}
	}
	public boolean isStatic() { return stat; }
	
	public void setVis(int in) { vis = in; }
	public void setStatic(boolean in) { stat = in; }
	
	public BC_Scope scope() { return scope; }
	public EArrayList<EnvisionObject> stack() { return scope.getStack(); }
	
	public void pushScope() { scope = new BC_Scope(scope); }
	public void popScope() {
		BC_Scope parent = scope.getParent();
		if (parent != null) scope = parent;
	}

	//-----------
	// Overrides
	//-----------
	
	@Override public void a_then_i(ByteCode code, String[] args) { IBC_a_then_i.a_then_i(vm, code, args); }
	@Override public void arg_i(ByteCode code, String[] args) { IBC_arg_i.arg_i(vm, code, args); }
	@Override public void endDef(ByteCode code, String[] args) { IBC_endDef.endDef(vm, code, args); }
	@Override public void handleClass(ByteCode code, String[] args) { IBC_handleClass.handleClass(vm, code, args); }
	@Override public void getset(ByteCode code, String[] args) { IBC_getset.getset(vm, code, args); }
	@Override public void math(ByteCode code, String[] args) { IBC_math.math(vm, code, args); }
	@Override public void stack(ByteCode code, String[] args) { IBC_stack.stack(vm, code, args); }
	@Override public void startDef(ByteCode code, String[] args) { IBC_startDef.startDef(vm, code, args); }
	@Override public void x_def(ByteCode code, String[] args) { IBC_x_def.x_def(vm, code, args); }
	@Override public void x_load(ByteCode code, String[] args) { IBC_x_load.x_load(vm, code, args); }
	@Override public void x_num(ByteCode code, String[] args) { IBC_x_num.x_num(vm, code, args); }
	@Override public void x_store(ByteCode code, String[] args) { IBC_x_store.x_store(vm, code, args); }
	
}
