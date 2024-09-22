package envision_lang.lang.packages.native_packages;

import envision_lang.lang.packages.native_packages.math.*;

public final class MathPackage extends NativePackage {
    
    //public static final MathPackage base = new MathPackage();
    
    public MathPackage() {
        super("math");
    }

    @Override
    public void buildFunctions() {
        define(new Abs());
        define(new Sqrt());
        define(new Pow());
        define(new Log());
        define(new Ceil());
        define(new Floor());
        define(new RandInt());
        define(new RandDouble());
        define(new RandStr());
        define(new RandName());
    }
    
}