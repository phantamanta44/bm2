package io.github.phantamanta44.bm2.core.lang;

import net.minecraft.util.StatCollector;

public enum Lang {
	
	MM_VERSION("bm2.gui.mainmenu.version"),
	MM_MODCOUNT("bm2.gui.mainmenu.modcount");
	
	public final String key;
	
	private Lang(String key) {
		this.key = key;
	}

	public static String get(String key) {
		return StatCollector.translateToLocal(key);
	}
	
	public static String get(Lang key) {
		return StatCollector.translateToLocal(key.key);
	}
	
}
