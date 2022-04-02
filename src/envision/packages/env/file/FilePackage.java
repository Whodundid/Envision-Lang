package envision.packages.env.file;

import envision.packages.EnvisionLangPackage;

public class FilePackage extends EnvisionLangPackage {

	public FilePackage() {
		super("file");
	}

	@Override
	public void buildClasses() {
		packageScope.defineClass(EnvisionFileClass.FILE_CLASS);
	}
	
}
