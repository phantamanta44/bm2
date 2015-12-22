package io.github.phantamanta44.bm2.core.util;

import io.github.phantamanta44.bm2.core.BM2;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class LibLoader {

	private static LibLoader instance = new LibLoader();
	
	public static void loadByUrl(URL url, ClassLoader loader) {
		instance.addURL(url, (URLClassLoader)loader);
	}
	
	private Method addUrl;
	
	private LibLoader() {
		try {
			this.addUrl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			this.addUrl.setAccessible(true);
		} catch (Exception e) {
			BM2.error("Something completely broke in LibLoader!");
			e.printStackTrace();
		}
	}
	
	private void addURL(URL url, URLClassLoader loader) {
		try {
			this.addUrl.invoke(loader, url);
		} catch (Exception e) {
			BM2.error("Something completely broke in LibLoader!");
			e.printStackTrace();
		}
	}
	
}
