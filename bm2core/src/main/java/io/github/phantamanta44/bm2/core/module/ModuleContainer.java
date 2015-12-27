package io.github.phantamanta44.bm2.core.module;

import io.github.phantamanta44.bm2.core.util.IPropertyMap;

import java.io.File;

import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.common.DummyModContainer;

public class ModuleContainer extends DummyModContainer {
	
	private final File file;
	private final IPropertyMap data;
	
	public ModuleContainer(File file, IPropertyMap data) {
		this.file = file;
		this.data = data;
	}
	
	@Override
	public String getName() {
		return data.getSafely("name");
	}
	
	@Override
	public String getModId() {
		return data.get("moduleId");
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
