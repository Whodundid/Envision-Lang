package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.file.EnvisionFileClass;

public final class ImagePackage extends NativePackage {

	public ImagePackage() {
		super("image");
	}

	@Override
	public void buildClasses() {
		define(EnvisionFileClass.FILE_CLASS);
	}
	
}
