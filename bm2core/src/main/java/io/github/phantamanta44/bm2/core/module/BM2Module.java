package io.github.phantamanta44.bm2.core.module;

/**
 * Parent class of all modules.
 * @author Phanta
 */
public class BM2Module {
	
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
	 * @return	A regex pattern for the server IP.
	 */
	public String getEnablementPattern() {
		return ".*";
	}
	
}
