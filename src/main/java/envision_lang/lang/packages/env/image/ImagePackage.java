package envision_lang.lang.packages.env.image;

import envision_lang.lang.packages.EnvisionLangPackage;
import envision_lang.lang.packages.env.file.EnvisionFileClass;

public class ImagePackage extends EnvisionLangPackage {

	public ImagePackage() {
		super("image");
	}

	@Override
	public void buildClasses() {
		define(EnvisionFileClass.FILE_CLASS);
	}
	
}
