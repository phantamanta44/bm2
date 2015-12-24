package io.github.phantamanta44.bm2.core.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Event listener. Mark listener methods with {@link ListenTo} and register with {@link io.github.phantamanta44.bm2.core.event.EventManager EventManager}.
 * @author Phanta
 */
public interface IListener {
	
	/**
	 * Denotes an event listener method.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ListenTo { }
	
}
