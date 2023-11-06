package main;

import envision_lang.lang.java.annotations.EClass;
import envision_lang.lang.java.annotations.EOperator;

@EClass
public class TestPoint {
	
	//========
	// Fields
	//========

	private int x, y;
	
	//==============
	// Constructors
	//==============
	
	public TestPoint() { this(0, 0); }
	public TestPoint(int x, int y) {
	    this.x = x;
	    this.y = y;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
	
	//===========
	// Functions
	//===========
	
	public TestPoint add(int x, int y) {
		System.out.println("RUNNING ADD: " + x + " : " + y);
		return new TestPoint(this.x + x, this.y + y);
	}
	
	public TestPoint add(TestPoint p) {
	    System.out.println("RUNNING ADD: " + p);
	    return new TestPoint(x + p.x, y + p.y);
	}
	
	public TestPoint sub(int x, int y) {
		System.out.println("RUNNING SUB: " + x + " : " + y);
		return new TestPoint(this.x - x, this.y - y);
	}
	
	public void cat(byte b, float f, String s ) {
		System.out.println("FROM TestPoint 'cat' HOI! [" + b + ", " + f + ", " + s + "]");
	}
	
	//=========
	// Getters
	//=========
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	//=========
	// Setters
	//=========
	
	public void setX(int xIn) { x = xIn; }
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
