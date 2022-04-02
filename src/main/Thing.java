package main;

public class Thing {
	
	private int x, y, z;
	
	public Thing(int xIn, int yIn, int zIn) {
		x = xIn;
		y = yIn;
		z = zIn;
	}
	
	@Override
	public String toString() {
		return "<"+x+", "+y+", "+z+">";
	}
	
	public Thing add(Thing t) {
		return new Thing(x + t.x, y + t.y, z + t.z);
	}
	
}
