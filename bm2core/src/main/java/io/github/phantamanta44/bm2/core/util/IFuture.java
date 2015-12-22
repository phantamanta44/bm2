package io.github.phantamanta44.bm2.core.util;

import java.util.function.Consumer;

public interface IFuture<T> {
	
	public boolean isDone();
	
	public T getResult();
	
	public IFuture<T> promise(Consumer<T> callback);
	
}
