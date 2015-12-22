package io.github.phantamanta44.bm2.core.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IListener {
	
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ListenTo { }
	
}
