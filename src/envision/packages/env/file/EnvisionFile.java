package envision.packages.env.file;

import java.io.File;

import envision.exceptions.errors.file.NoSuchFileError;
import envision.lang.classes.ClassInstance;
import envision.lang.internal.JavaObjectWrapper;
import eutil.datatypes.EArrayList;

public class EnvisionFile extends ClassInstance {
	
	public File iFile;
	public EArrayList<String> toBeWritten = new EArrayList();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionFile(String pathIn) {
		super(EnvisionFileClass.FILE_CLASS);
		iFile = new File(pathIn);
	}
	
	//---------
	// Methods
	//---------
	
	public File getF() {
		return iFile;
	}
	
	public File gfe() {
		if (iFile.exists()) return iFile;
		throw new NoSuchFileError(iFile);
	}
	
	//-----------------------------------------
	
	public static boolean isFile(Object in) {
		if (in instanceof ClassInstance) return isFile((ClassInstance) in);
		return false;
	}
	
	public static boolean isFile(ClassInstance inst) {
		if (EnvisionFileClass.FILE_CLASS.isInstanceof(inst)) {
			JavaObjectWrapper file_wrapper = (JavaObjectWrapper) inst.get("_iFile_");
			return file_wrapper != null && file_wrapper.getJavaObject() instanceof File;
		}
		return false;
	}
	
	public static String getFilePath(ClassInstance inst) {
		if (!isFile(inst)) return null;
		JavaObjectWrapper file_wrapper = (JavaObjectWrapper) inst.get("_iFile_");
		return ((File) file_wrapper.getJavaObject()).getAbsolutePath();
	}
	
}
