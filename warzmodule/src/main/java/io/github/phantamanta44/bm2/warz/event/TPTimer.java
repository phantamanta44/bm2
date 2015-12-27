package io.github.phantamanta44.bm2.warz.event;

import io.github.phantamanta44.bm2.core.event.chat.CommandSwallower;
import io.github.phantamanta44.bm2.core.notification.NotificationManager;
import io.github.phantamanta44.bm2.core.notification.TextNotification;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TPTimer {

	private static Timer timer;
	
	public static void start() { 
		timer = new Timer();
	}
	
	public static void stop() {
		timer.cancel();
	}
	
	public static void clear() {
		stop();
		start();
	}
	
	public static void requestSent() {
		new CommandSwallower("/tpa", "Sorry, you cannot teleport at this moment! Please wait (\\d*) seconds").promise(r -> {
			Matcher m = Pattern.compile("wait (\\d*) seconds").matcher(r[0]);
			m.find();
			timer.schedule(new TPNotifyTask(), Integer.parseInt(m.group(1)));
		}).dispatch();
	}
	
	private static class TPNotifyTask extends TimerTask {

		@Override
		public void run() {
			NotificationManager.putNotification(new TextNotification("TPA is off cooldown."));
		}
		
	}

}
