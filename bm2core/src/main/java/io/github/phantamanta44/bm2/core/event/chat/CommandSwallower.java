package io.github.phantamanta44.bm2.core.event.chat;

import io.github.phantamanta44.bm2.core.util.IFuture;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandSwallower implements IFuture<String[]> {
	
	private final String command;
	private final Predicate<IChatComponent> filter;
	private final String[] response;
	private int index;
	private boolean done = false;
	private Consumer<String[]> callback;
	
	public CommandSwallower(String command) {
		this(command, ".*");
	}
	
	public CommandSwallower(String command, String regex) {
		this(command, msg -> msg.getUnformattedText().matches(regex), 1);
	}
	
	public CommandSwallower(String command, Predicate<IChatComponent> filter) {
		this(command, filter, 1);
	}
	
	public CommandSwallower(String command, Predicate<IChatComponent> filter, int lineCount) {
		this.command = command;
		this.filter = filter;
		this.response = new String[lineCount];
		this.send();
	}
	
	public void send() {
		MinecraftForge.EVENT_BUS.register(this);
		Minecraft.getMinecraft().thePlayer.sendChatMessage(this.command);
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if (this.filter.test(event.message))
			this.response[this.index++] = event.message.getUnformattedText();
		if (this.index >= this.response.length) {
			this.done = true;
			MinecraftForge.EVENT_BUS.unregister(this);
			this.callback.accept(this.response);
		}
	}
	
	@Override
	public boolean isDone() {
		return this.done;
	}
	
	@Override
	public String[] getResult() {
		return this.done ? this.response : null;
	}

	@Override
	public void promise(Consumer<String[]> callback) {
		this.callback = callback;
	}
	
}
