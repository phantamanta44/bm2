package io.github.phantamanta44.bm2.core.module;

import java.io.File;

import net.minecraftforge.fml.common.DummyModContainer;

public class ModuleContainer extends DummyModContainer {
	
	private final File file;
	
	public ModuleContainer(File file) {
		this.file = file;
	}
	
	@Override
	public File getSource() {
		return file;
	}

}
