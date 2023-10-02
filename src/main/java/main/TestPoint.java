package main;

import envision_lang.lang.java.annotations.EClass;
import envision_lang.lang.java.annotations.EConstructor;
import envision_lang.lang.java.annotations.EField;
import envision_lang.lang.java.annotations.EFunction;
import envision_lang.lang.java.annotations.EOperator;

@EClass
public class TestPoint {
	
	//========
	// Fields
	//========

	@EField
	private int x, y;
	
	//==============
	// Constructors
	//==============
	
	@EConstructor
	public TestPoint() { this(0, 0); }
	@EConstructor
	public TestPoint(int x, int y) {
	    this.x = x;
	    this.y = y;
	}
	
	//===========
	// Overrides
	//===========
	
	@EFunction
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
	
	//===========
	// Functions
	//===========
	
	@EFunction
	public TestPoint add(int x, int y) {
		System.out.println("RUNNING ADD: " + x + " : " + y);
		return new TestPoint(this.x + x, this.y + y);
	}
	
	@EFunction
	public TestPoint sub(int x, int y) {
		System.out.println("RUNNING SUB: " + x + " : " + y);
		return new TestPoint(this.x - x, this.y - y);
	}
	
	@EFunction
	public void cat(byte b, float f, String s ) {
		System.out.println("FROM TestPoint 'cat' HOI! [" + b + ", " + f + ", " + s + "]");
	}
	
	//=========
	// Getters
	//=========
	
	@EFunction
	public int getX() { return x; }
	
	@EFunction
	public int getY() { return y; }
	
	//=========
	// Setters
	//=========
	
	@EFunction
	public void setX(int xIn) { x = xIn; }
	
	@EFunction
	public void setY(int yIn) { y = yIn; }
	
	//====================
	// Operator Overloads
	//====================
	
	@EOperator
	public TestPoint __ADD__(TestPoint t) {
		return add(t.x, t.y);
	}
	
	@EOperator
	public TestPoint __SUB__(TestPoint t) {
		return sub(t.x, t.y);
	}
	
}
