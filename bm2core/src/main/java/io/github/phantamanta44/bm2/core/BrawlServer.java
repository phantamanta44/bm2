package io.github.phantamanta44.bm2.core;

public enum BrawlServer {

	LOBBY("lobby", "lobby.brawl.com"),
	WAR("war", "mc-war.com"),
	WARZ("warz", "mc-warz.com"),
	CREATIVE("creative", "creative.brawl.com"),
	PVP("pvp", "pvp.brawl.com"),
	PARTY("party", "minecraftparty.com"),
	SKYWARS("skywars", "skywars.brawl.com"),
	MINESTRIKE("minestrike", "minestrike.brawl.com"),
	CTF("ctf", "mcctf.com"),
	HG("hg", "mc-hg.com"),
	BUILD("build", "minecraftbuild.com"),
	KITPVP("kitpvp", "kitbrawl.com"),
	EVENT("event", "event.brawl.com");
	
	private static final String KEY_ROOT = "bm2.server.";
	
	public final String name;
	public final String ipRoot;
	
	private BrawlServer(String name, String ipRoot) {
		this.name = name;
		this.ipRoot = ipRoot;
	}
	
	public String getLangKey() {
		return KEY_ROOT + this.name;
	}
	
	public String getIpRegex() {
		return "\\d+\\." + this.ipRoot.replaceAll("\\.", "\\.");
	}
	
}
