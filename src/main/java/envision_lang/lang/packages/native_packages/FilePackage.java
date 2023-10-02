package envision_lang.lang.packages.native_packages;

import envision_lang.lang.file.EnvisionFileClass;

public final class FilePackage extends NativePackage {

	public FilePackage() {
		super("file");
	}

	@Override
	public void buildClasses() {
		define(EnvisionFileClass.FILE_CLASS);
	}
	
}
