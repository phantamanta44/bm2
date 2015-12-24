package io.github.phantamanta44.bm2.core.notification;

/**
 * A notification compatible with {@link io.github.phantamanta44.bm2.core.notification.NotificationManager NotificationManager}.
 * @author Phanta
 */
public interface INotification {
	
	/**
	 * Ticks the notification; run every render tick.
	 * @return	Whether the notification should be killed immediately.
	 */
	public boolean tick();
	
	/**
	 * Gets how long the notification should last.
	 * @return	Milliseconds the notification should last. Should be at least 1400.
	 */
	public int getTTL();
	
	/**
	 * Gets how tall the notification's render box is.
	 * @return	An int value representing height.
	 */
	public int getHeight();
	
	/**
	 * Renders the notification. The notificaton should render in the top-left corner of the screen, at x = 0.
	 * @param y	The y-coordinate for the notification's top-left corner.
	 * @param y	A float between 0.0 and 1.0 that represents how transparent the notification should be.
	 */
	public void render(int y, float fade);
	
}
