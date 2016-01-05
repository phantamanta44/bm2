package io.github.phantamanta44.bm2.core.util;

import java.util.function.Consumer;

/**
 * Represents an asynchronous processing job that will be finished at a point in the future.
 * @author 		Phanta
 * @param <T>	The return type of the processing job.
 */
public interface IFuture<T> {
	
	/**
	 * Returns whether the job is done yet.
	 * @return	Completion state.
	 */
	public boolean isDone();
	
	/**
	 * Return the results of the processing job, or null if {@link #isDone()} is false.
	 * @return	The results of the processing job.
	 */
	public T getResult();
	
	/**
	 * Register a callback function to be called with the results as a parameter once the job is completed.
	 * @param callback	{@link Consumer} taking the results as a parameter.
	 * @return			The IFuture, allowing for function chaining.
	 */
	public IFuture<T> promise(Consumer<T> callback);
	
	/**
	 * Begin the processing job. Should be called after registering callbacks with {@link #promise(Consumer)}.
	 */
	public void dispatch();
	
}
