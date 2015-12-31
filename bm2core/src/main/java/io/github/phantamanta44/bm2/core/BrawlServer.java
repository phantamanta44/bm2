package io.github.phantamanta44.bm2.core;

import java.util.Arrays;

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
	
	private static final String KEY_ROOT = "bm2.server.", REGEX_ROOT = "(\\d+\\.)?";
	private static final String[] DIRECT_IPS = new String[] {
		"192.99.150.162", "192.99.46.105", "192.99.150.141", "192.99.21.112"
	};
	
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
		return REGEX_ROOT + this.ipRoot.replaceAll("\\.", "\\.");
	}

	public static boolean isBrawlServer(String ip) {
		return ip.matches("(\\S+\\.)?brawl\\.com")
			|| Arrays.stream(values())
			.anyMatch(s -> ip.matches(s.getIpRegex()))
			|| Arrays.stream(DIRECT_IPS)
			.anyMatch(i -> ip.trim().equalsIgnoreCase(i));
	}
	
}
