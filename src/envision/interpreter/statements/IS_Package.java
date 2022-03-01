package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statements.PackageStatement;

public class IS_Package extends StatementExecutor<PackageStatement> {

	public IS_Package(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(PackageStatement statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, PackageStatement s) {
		new IS_Package(in).run(s);
	}
	
}