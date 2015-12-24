package io.github.phantamanta44.bm2.etc.friend;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.util.StreamUtils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.client.Minecraft;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.Maps;

public class FriendManager {
	
	private static final String URL_FORMAT = "http://www.brawl.com/players/%s/";
	private static final Pattern CRAVATAR_EXTRACT = Pattern.compile("http://cravatar\\.eu/helmavatar/(\\w*)/32\\.png");
	private static final Map<UUID, FriendInfo> friends = Maps.newHashMap();
	private static Timer updateTimer;
	private static TimerTask updateTask;
	
	public static Collection<FriendInfo> getFriends() {
		return friends.values();
	}
	
	public static boolean isFriend(String name) {
		return friends.values().stream()
			.anyMatch(f -> f.name.equals(name));
	}
	
	public static boolean isFriend(UUID id) {
		return friends.containsKey(id);
	}
	
	public static FriendInfo getFriend(String name) {
		return friends.values().stream()
			.filter(f -> f.name.equals(name))
			.findFirst()
			.orElse(null);
	}
	
	public static FriendInfo getFriend(UUID id) {
		return friends.get(id);
	}
	
	public static void enableUpdates() {
		updateTimer = new Timer();
		updateTask = new UpdateFriendsTask();
	}
	
	public static void disableUpdates() {
		updateTimer.cancel();
	}
	
	public static boolean updateFriendList() {
		updateTask.cancel();
		updateTimer.purge();
		updateTask = new UpdateFriendsTask();
		updateTimer.schedule(updateTask, 1200000L);
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputStream stream = getPlayerURL().openStream();
			Document doc = docBuilder.parse(stream);
			stream.close();
			parseFriendNode(doc.getElementById("friends").getFirstChild().getChildNodes());
			parseFriendNode(doc.getElementById("friendsOverlap").getChildNodes());
			return true;
		} catch (Exception ex) {
			BM2.warn("Could not retrieve friend list!");
			ex.printStackTrace();
			return false;
		}
	}
	
	private static URL getPlayerURL() throws MalformedURLException {
		return new URL(String.format(URL_FORMAT, Minecraft.getMinecraft().thePlayer.getGameProfile().getName()));
	}
	
	private static void parseFriendNode(NodeList nodes) {
		StreamUtils.streamify(nodes, nodes.getLength(), (l, i) -> l.item(i))
			.filter(n -> n.getNodeType() == 1 && ((Element)n).getTagName().equals("a"))
			.map(e -> (Element)e.getFirstChild())
			.forEach(e -> {
				String name = ((Element)e.getElementsByTagName("div")).getAttribute("title");
				Matcher idMatcher = CRAVATAR_EXTRACT.matcher(((Element)e.getElementsByTagName("img")).getAttribute("src"));
				idMatcher.find();
				UUID id = UUID.fromString(idMatcher.group(0));
				friends.put(id, new FriendInfo(name, id));
			});
	}

	public static class FriendInfo {
		
		public final String name;
		public final UUID id;
		
		public FriendInfo(String name, UUID id) {
			this.name = name;
			this.id = id;
		}
		
	}
	
	public static class UpdateFriendsTask extends TimerTask {

		@Override
		public void run() {
			updateFriendList();
		}
		
	}
	
}
