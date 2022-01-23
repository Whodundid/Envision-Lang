package envision.bytecode.vm;

import envision.bytecode.ByteCode;

public interface ByteCodeStatementHandler {
	
	public void arg_i(ByteCode code, String[] args);
	public void a_then_i(ByteCode code, String[] args);
	public void endDef(ByteCode code, String[] args);
	public void getset(ByteCode code, String[] args);
	public void handleClass(ByteCode code, String[] args);
	public void math(ByteCode code, String[] args);
	public void stack(ByteCode code, String[] args);
	public void startDef(ByteCode code, String[] args);
	public void x_num(ByteCode code, String[] args);
	public void x_def(ByteCode code, String[] args);
	public void x_load(ByteCode code, String[] args);
	public void x_store(ByteCode code, String[] args);
	
}
