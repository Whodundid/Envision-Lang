package envision.lang.packages.env.file;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.packages.EnvisionLangPackage;

public class FilePackage extends EnvisionLangPackage {

	public FilePackage() {
		super("file");
	}

	@Override
	public void buildMethods() {
	}

	@Override
	public void buildFields() {
	}

	@Override
	public void buildClasses(EnvisionInterpreter interpreter) {
		add(new EnvFile(interpreter));
	}

	@Override
	public void buildPackages(EnvisionInterpreter interpreter) {
	}
	
}
