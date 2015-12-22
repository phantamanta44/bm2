package io.github.phantamanta44.bm2.core;

import io.github.phantamanta44.bm2.core.event.EventManager;
import io.github.phantamanta44.bm2.core.event.world.WorldChangeListener;
import io.github.phantamanta44.bm2.core.gui.TitleScreenInterceptor;
import io.github.phantamanta44.bm2.core.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BM2Const.MOD_ID, version = BM2Const.MOD_VERS_STR)
public class BM2 {
	
	@Instance(BM2Const.MOD_ID)
	public static BM2 instance;
	private static Logger logger = LogManager.getLogger("BM2");
	
	private EventManager eventManager = new EventManager();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(eventManager);
		FMLCommonHandler.instance().bus().register(eventManager);
		MinecraftForge.EVENT_BUS.register(new WorldChangeListener());
		MinecraftForge.EVENT_BUS.register(new TitleScreenInterceptor());
		info("BM2 enabled. Searching for valid modules...");
		ModuleManager.searchForModules();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	public static void info(String format, Object... data) {
		logger.info(String.format(format, data));
	}
	
	public static void warn(String format, Object... data) {
		logger.warn(String.format(format, data));
	}
	
	public static void error(String format, Object... data) {
		logger.error(String.format(format, data));
	}
	
}
