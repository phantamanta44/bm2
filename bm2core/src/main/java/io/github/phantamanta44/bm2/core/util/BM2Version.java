package io.github.phantamanta44.bm2.core.util;

public class BM2Version {
	
	public final int major, minor, patch;
	
	public BM2Version(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}
	
	public BM2Version(String version) {
		if (!version.matches("\\d*\\.\\d*\\.\\d*"))
			throw new IllegalArgumentException("Illegal version number " + version + "!");
		String[] digits = version.split("\\.");
		this.major = Integer.parseInt(digits[0]);
		this.minor = Integer.parseInt(digits[1]);
		this.patch = Integer.parseInt(digits[2]);
	}
	
	@Override
	public String toString() {
		return String.format("%d.%d.%d", this.major, this.minor, this.patch);
	}
	
}
