package main;

import envision_lang.lang.java.EnvisionJavaObject;
import envision_lang.lang.java.annotations.EConstructor;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.java.annotations.EOperator;

public class TestPoint extends EnvisionJavaObject {
	
	//--------
	// Fields
	//--------

	@EField
	private int x, y;
	
	//--------------
	// Constructors
	//--------------
	
//	@EConstructor
//	public TestPoint() { this(0, 0); }
	@EConstructor
	public TestPoint(int x, int y) {
		init_bridge();
		set("x", x);
		set("y", y);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	@EFunction
	public String toString() {
		System.out.println("[" + x + ", " + y + "]");
		return "<{x}, {y}>";
	}
	
	//-----------
	// Functions
	//-----------
	
	//@EFunction(params="x, y")
	public TestPoint add(int x, int y) {
		System.out.println("RUNNING ADD: " + x + " : " + y);
		return new TestPoint(this.x + x, this.y + y);
	}
	
	//@EFunction(params="x, y")
	public TestPoint sub(int x, int y) {
		System.out.println("RUNNING SUB: " + x + " : " + y);
		return new TestPoint(this.x - x, this.y - y);
	}
	
	@EFunction
	public void cat(byte b, float f, String s ) {
		System.out.println("FROM TestPoint 'cat' HOI! [" + b + ", " + f + ", " + s + "]");
	}
	
	//--------------------
	// Operator Overloads
	//--------------------
	
	@EOperator
	public TestPoint __ADD__(TestPoint t) {
		return add(t.x, t.y);
	}
	
	@EOperator
	public TestPoint __SUB__(TestPoint t) {
		return sub(t.x, t.y);
	}
	
}
