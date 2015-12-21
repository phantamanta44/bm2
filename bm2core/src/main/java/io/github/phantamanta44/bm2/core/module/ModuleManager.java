package io.github.phantamanta44.bm2.core.module;

import io.github.phantamanta44.bm2.core.util.IniData;
import io.github.phantamanta44.bm2.core.util.LibLoader;
import io.github.phantamanta44.bm2.core.util.Mutable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLLog;

import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public class ModuleManager {

	private static final File moduleDir = new File(Minecraft.getMinecraft().mcDataDir, "brawlmods");
	private static final Map<String, BM2Module> loadedMods = Maps.newHashMap();
	private static final Map<String, Mutable<Boolean>> modStatusMap = Maps.newHashMap();
	
	private static void loadModule(String moduleId, Class<? extends BM2Module> modClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		BM2Module module = modClass.getConstructor().newInstance();
		loadedMods.put(moduleId, module);
		module.onLoad();
		modStatusMap.put(moduleId, new Mutable<>(false));
	}

	public static void searchForModules() {
		if (!moduleDir.exists()) {
			moduleDir.mkdirs();
			return;
		}
		for (File file : moduleDir.listFiles()) {
			String fname = file.getName();
			if (!fname.matches(".*\\.jar"))
				continue;
			FMLLog.info("Loading module %s...", fname);
			try {
				ZipFile asJar = new ZipFile(file);
				ZipEntry metaFile = asJar.getEntry("module.ini");
				if (metaFile == null) {
					FMLLog.warning("Skipping module %s because there was no module.cfg.", fname);
					continue;
				}
				IniData metaProps = new IniData(asJar.getInputStream(metaFile));
				asJar.close();
				String coreClass;
				if ((coreClass = metaProps.get("coreclass")) == null) {
					FMLLog.warning("Skipping module %s because the core class didn't exist.", fname);
					continue;
				}
				String moduleId;
				if ((moduleId = metaProps.get("moduleid")) == null) {
					FMLLog.warning("Skipping module %s because the module id wasn't there.", fname);
					continue;
				}
				LibLoader.loadByUrl(file.toURI().toURL());
				loadModule(moduleId, (Class<? extends BM2Module>)Class.forName(coreClass));
			} catch (Exception e) {
				FMLLog.warning("Error loading module %s!", fname);
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isEnabled(String moduleId) {
		if (modStatusMap.containsKey(moduleId))
			return modStatusMap.get(moduleId).getValue();
		return false;
	}
	
}
