package io.github.phantamanta44.bm2.core.util;

public class Mutable<T> {

	private T value;
	
	public Mutable(T initialValue) {
		this.value = initialValue;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return this.value;
	}
	
}
