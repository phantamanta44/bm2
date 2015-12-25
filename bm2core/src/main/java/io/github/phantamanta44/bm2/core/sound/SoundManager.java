package io.github.phantamanta44.bm2.core.sound;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Maps;

/**
 * Manages and dispatches sounds.
 * @author Phanta
 */
public class SoundManager {

	private static final SoundHandler sh = Minecraft.getMinecraft().getSoundHandler();
	private static final Map<Long, ISound> sndPool = Maps.newHashMap();
	private static long id = 0;
	
	/**
	 * Plays a sound without position (i.e. in the 2d plane).
	 * @param resLoc	Resource location for the sound.
	 * @return			Identifier for the sound.
	 */
	public static long playOrtho(ResourceLocation resLoc) {
		ISound snd = PositionedSoundRecord.create(resLoc);
		return putSound(snd);
	}
	
	/**
	 * Plays a sound with position.
	 * @param resLoc	Resource location for the sound.
	 * @param x			X coordinate for the sound.
	 * @param y			Y coordinate for the sound.
	 * @param z			Z coordinate for the sound.
	 * @return			Identifier for the sound.
	 */
	public static long playPos(ResourceLocation resLoc, float x, float y, float z) {
		ISound snd = PositionedSoundRecord.create(resLoc, x, y, z);
		return putSound(snd);
	}
	
	/**
	 * Stop playing a sound.
	 * @param id
	 * @return
	 */
	public static boolean stopSound(long id) {
		if (!sndPool.containsKey(id) || !sh.isSoundPlaying(sndPool.get(id)))
			return false;
		sh.stopSound(sndPool.get(id));
		return true;
	}
	
	private static long putSound(ISound snd) {
		sh.playSound(snd);
		purgePool();
		sndPool.put(id, snd);
		return id++;
	}
	
	private static void purgePool() {
		sndPool.values().removeIf(s -> !sh.isSoundPlaying(s));
	}
	
}
