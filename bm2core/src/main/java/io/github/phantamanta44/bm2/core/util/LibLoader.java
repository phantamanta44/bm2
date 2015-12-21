package io.github.phantamanta44.bm2.core.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import net.minecraftforge.fml.common.FMLLog;

public class LibLoader {

	private static LibLoader loader = new LibLoader();
	
	public static void loadByUrl(URL url) {
		loader.addURL(url);
	}
	
	private URLClassLoader sysLoader;
	private Method addUrl;
	
	private LibLoader() {
		try {
			this.sysLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
			this.addUrl = URLClassLoader.class.getMethod("addURL", URL.class);
		} catch (Exception e) {
			FMLLog.severe("Something completely broke in LibLoader!");
			e.printStackTrace();
		}
	}
	
	private void addURL(URL url) {
		try {
			this.addUrl.invoke(sysLoader, url);
		} catch (Exception e) {
			FMLLog.severe("Something completely broke in LibLoader!");
			e.printStackTrace();
		}
	}
	
}
