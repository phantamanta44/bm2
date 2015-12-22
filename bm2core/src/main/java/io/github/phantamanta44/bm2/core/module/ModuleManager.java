package io.github.phantamanta44.bm2.core.module;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.util.IniData;
import io.github.phantamanta44.bm2.core.util.LibLoader;
import io.github.phantamanta44.bm2.core.util.Mutable;
import io.github.phantamanta44.bm2.core.util.PropertyMap;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.client.Minecraft;

import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public class ModuleManager {

	private static final File moduleDir = new File(Minecraft.getMinecraft().mcDataDir, "brawlmods");
	private static final Map<String, BM2Module> loadedMods = Maps.newHashMap();
	private static final Map<String, Mutable<Boolean>> modStatusMap = Maps.newHashMap();
	
	private static void loadModule(String moduleId, Class<? extends BM2Module> modClass, PropertyMap data) throws Exception {
		BM2Module module = modClass.getConstructor(PropertyMap.class).newInstance(data);
		loadedMods.put(moduleId, module);
		modStatusMap.put(moduleId, new Mutable<>(false));
		module.onLoad();
	}

	public static void searchForModules() {
		if (!moduleDir.exists()) {
			moduleDir.mkdirs();
			return;
		}
		for (File file : moduleDir.listFiles()) {
			String fname = file.getName();
			if (!fname.endsWith(".jar"))
				continue;
			BM2.info("Loading module %s...", fname);
			try {
				ZipFile asJar = new ZipFile(file);
				ZipEntry metaFile = asJar.getEntry("module.ini");
				if (metaFile == null) {
					BM2.warn("Skipping module %s because there was no module.cfg.", fname);
					continue;
				}
				PropertyMap metaProps = new IniData(asJar.getInputStream(metaFile));
				asJar.close();
				String coreClass;
				if ((coreClass = metaProps.get("coreclass")) == null) {
					BM2.warn("Skipping module %s because the core class didn't exist.", fname);
					continue;
				}
				String moduleId;
				if ((moduleId = metaProps.get("moduleid")) == null) {
					BM2.warn("Skipping module %s because the module id wasn't there.", fname);
					continue;
				}
				LibLoader.loadByUrl(file.toURI().toURL(), ModuleManager.class.getClassLoader());
				loadModule(moduleId, (Class<? extends BM2Module>)Class.forName(coreClass), metaProps);
			} catch (Exception e) {
				BM2.warn("Error loading module %s!", fname);
				e.printStackTrace();
			}
		}
	}
	
	public static int getModuleCount() {
		return loadedMods.size();
	}
	
	public static boolean isEnabled(String moduleId) {
		if (modStatusMap.containsKey(moduleId))
			return modStatusMap.get(moduleId).getValue();
		return false;
	}
	
	public static void ipUpdate(String ip) {
		BM2.info("Change detected to %s. Updating modules...", ip);
		for (Entry<String, BM2Module> module : loadedMods.entrySet()) {
			if (module.getValue().getEnablementConditions().test(ip)) {
				modStatusMap.get(module.getKey()).setValue(true);
				module.getValue().onEnable();
			}
			else {
				modStatusMap.get(module.getKey()).setValue(false);
				module.getValue().onDisable();
			}
		}
		BM2.info("E: %s", Arrays.toString(modStatusMap.entrySet().stream()
			.filter(entry -> entry.getValue().getValue())
			.map(entry -> entry.getKey())
			.toArray(size -> new String[size])));
		BM2.info("D: %s", Arrays.toString(modStatusMap.entrySet().stream()
			.filter(entry -> !entry.getValue().getValue())
			.map(entry -> entry.getKey())
			.toArray(size -> new String[size])));
	}
	
}
