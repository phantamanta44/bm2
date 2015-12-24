package io.github.phantamanta44.bm2.core.notification;

import io.github.phantamanta44.bm2.core.util.Mutable;

import java.util.List;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.Lists;

public class NotificationManager {

	private static List<NotificationState> active = Lists.newArrayList(); 
	
	public static void putNotification(INotification notification) {
		active.add(0, new NotificationState(notification));
	}
	
	@SubscribeEvent
	public void renderIngameGui(RenderGameOverlayEvent.Post event) {
		if (event.type != ElementType.EXPERIENCE)
			return;
		active.removeIf(n -> {
			n.getNotification().render(n.y, n.getTransparency());
			n.tick();
			return n.getNotification().tick() || n.ttlReached();
		});
		final Mutable<Integer> ht = new Mutable<>(0);
		active.forEach(n -> {
			n.targetY = ht.getValue();
			ht.setValue(ht.getValue() + n.getNotification().getHeight());
		});
	}
	
	private static class NotificationState {
		
		private long lastUpdate;
		private INotification not;
		private int y = 0, targetY = 0, timeLived = 0;
		private int ttl;

		public NotificationState(INotification notification) {
			this.not = notification;
			this.ttl = notification.getTTL();
			this.lastUpdate = System.currentTimeMillis();
		}
		
		public boolean ttlReached() {
			return this.timeLived >= this.ttl;
		}

		public void tick() {
			long currentTime = System.currentTimeMillis();
			this.timeLived += currentTime - this.lastUpdate;
			this.lastUpdate = currentTime;
			if (this.y > this.targetY)
				this.y -= (int)Math.max(1F, (float)(this.targetY - this.y) / 4F);
			if (this.y < this.targetY)
				this.y += (int)Math.max(1F, (float)(this.targetY - this.y) / 4F);
		}
		
		public float getTransparency() {
			int timeLeft = this.ttl - this.timeLived;
			if (timeLeft <= 800)
				return (float)timeLeft / 800F;
			if (this.timeLived <= 600)
				return (float)this.timeLived / 600F;
			return 1F;
		}
		
		public INotification getNotification() {
			return not;
		}
		
	}
	
}
