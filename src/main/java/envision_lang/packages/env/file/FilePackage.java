package envision_lang.packages.env.file;

import envision_lang.packages.EnvisionLangPackage;

public class FilePackage extends EnvisionLangPackage {

	public FilePackage() {
		super("file");
	}

	@Override
	public void buildClasses() {
		define(EnvisionFileClass.FILE_CLASS);
	}
	
}
