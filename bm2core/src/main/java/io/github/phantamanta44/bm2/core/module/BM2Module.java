package io.github.phantamanta44.bm2.core.module;

import io.github.phantamanta44.bm2.core.util.PropertyMap;

import java.util.function.Predicate;

/**
 * Parent class of all modules. Override {@link #onLoad()} instead of writing a constructor.
 * @author Phanta
 */
public class BM2Module {
	
	public final PropertyMap meta;
	
	public BM2Module(PropertyMap data) {
		this.meta = data;
	}
	
	/**
	 * Called immediately after construction.
	 */
	public void onLoad() {
		// NO-OP
	}
	
	/**
	 * Called when the game is exiting.
	 */
	public void onUnload() {
		// NO-OP
	}
	
	/**
	 * Called when the module is enabled.
	 */
	public void onEnable() {
		// NO-OP
	}
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		// NO-OP
	}
	
	/**
	 * Defines which servers this module is enabled on.
	 * @return	A {@link java.util.function.Predicate Predicate} consuming the server IP.
	 */
	public Predicate<String> getEnablementConditions() {
		return ip -> true;
	}
	
}
