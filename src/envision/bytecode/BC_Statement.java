package envision.bytecode;

import envision.bytecode.vm.ByteCodeStatementHandler;

public abstract class BC_Statement {
	public abstract void execute(ByteCodeStatementHandler h);
}
