package envision_lang.packages.env.image;

import envision_lang.packages.EnvisionLangPackage;
import envision_lang.packages.env.file.EnvisionFileClass;

public class ImagePackage extends EnvisionLangPackage {

	public ImagePackage() {
		super("image");
	}

	@Override
	public void buildClasses() {
		packageScope.defineClass(EnvisionFileClass.FILE_CLASS);
	}
	
}
