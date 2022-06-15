package envision.packages.env.image;

import envision.packages.EnvisionLangPackage;
import envision.packages.env.file.EnvisionFileClass;

public class ImagePackage extends EnvisionLangPackage {

	public ImagePackage() {
		super("image");
	}

	@Override
	public void buildClasses() {
		packageScope.defineClass(EnvisionFileClass.FILE_CLASS);
	}
	
}
