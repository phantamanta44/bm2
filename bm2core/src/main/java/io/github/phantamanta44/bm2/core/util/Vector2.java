package io.github.phantamanta44.bm2.core.util;

public class Vector2<T> {
	
	private T x, y;
	
	public Vector2(T initX, T initY) {
		x = initX;
		y = initY;
	}
	
	public T getX() {
		return x;		
	}
	
	public T getY() {
		return y;
	}
	
	public void setX(T i) {
		x = i;
	}
	
	public void setY(T i) {
		y = i;
	}
	
}
