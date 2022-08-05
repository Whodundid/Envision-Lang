package main;

public class Java_Performance_Reference {
	
	private int x, y, z;
	
	public Java_Performance_Reference(int xIn, int yIn, int zIn) {
		x = xIn;
		y = yIn;
		z = zIn;
	}
	
	@Override
	public String toString() {
		return "<"+x+", "+y+", "+z+">";
	}
	
	public Java_Performance_Reference add(Java_Performance_Reference t) {
		return new Java_Performance_Reference(x + t.x, y + t.y, z + t.z);
	}
	
}
