package io.github.phantamanta44.bm2.core.module;

import java.io.File;

import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.common.DummyModContainer;

public class ModuleContainer extends DummyModContainer {
	
	private final File file;
	
	public ModuleContainer(File file) {
		this.file = file;
	}
	
	@Override
	public String getName() {
		return "BM2Module";
	}
	
	@Override
	public String getModId() {
		return "brawlmodularmod";
	}
	
	@Override
	public File getSource() {
		return file;
	}
	
	@Override
	public Class<?> getCustomResourcePackClass() {
		return FMLFileResourcePack.class;
	}

}
