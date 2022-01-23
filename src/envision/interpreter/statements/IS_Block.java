package envision.interpreter.statements;

import envision.interpreter.util.Scope;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.types.BlockStatement;
import envision.interpreter.EnvisionInterpreter;

public class IS_Block extends StatementExecutor<BlockStatement> {

	public IS_Block(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(BlockStatement statement) {
		buildBlock(interpreter, statement, new Scope(scope()));
	}
	
	/** Excecutes block statements within the specified scope. */
	public static void buildBlock(EnvisionInterpreter in, BlockStatement s, Scope scope) {
		in.executeBlock(s.statements, scope);
	}
	
	public static void run(EnvisionInterpreter in, BlockStatement s) {
		new IS_Block(in).run(s);
	}
	
}
