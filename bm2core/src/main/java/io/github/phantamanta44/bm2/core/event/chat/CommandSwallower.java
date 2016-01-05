package io.github.phantamanta44.bm2.core.event.chat;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.util.IFuture;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Dispatches a command and swallows the output based on a {@link Predicate}.
 * @author Phanta
 */
public class CommandSwallower implements IFuture<String[]> {
	
	private final String command;
	private final Predicate<IChatComponent> filter;
	private final String[] response;
	private int index;
	private boolean done = false;
	private Consumer<String[]> callback;
	
	/**
	 * Constructs a CommandSwallower with the given command.
	 * @param command	Command to dispatch.
	 */
	public CommandSwallower(String command) {
		this(command, ".*");
	}
	
	/**
	 * Constructs a CommandSwallower with the given command and output filter.
	 * @param command	Command to dispatch.
	 * @param regex		Regex to match the output against.
	 */
	public CommandSwallower(String command, String regex) {
		this(command, msg -> msg.getUnformattedText().matches(regex), 1);
	}
	
	/**
	 * Constructs a CommandSwallower with the given command and output filter.
	 * @param command	Command to dispatch.
	 * @param filter	{@link Predicate} to match the output against.
	 */
	public CommandSwallower(String command, Predicate<IChatComponent> filter) {
		this(command, filter, 1);
	}
	
	/**
	 * Constructs a CommandSwallower with the given command, output filter, and output length.
	 * @param command	Command to dispatch.
	 * @param regex		Regex to match the output against.
	 * @param lineCount	Number of lines to capture before closing the swallower.
	 */
	public CommandSwallower(String command, String regex, int lineCount) {
		this(command, msg -> msg.getUnformattedText().matches(regex), lineCount);
	}
	
	/**
	 * Constructs a CommandSwallower with the given command, output filter, and output length.
	 * @param command	Command to dispatch.
	 * @param regex		{@link Predicate} to match the output against.
	 * @param lineCount	Number of lines to capture before closing the swallower.
	 */
	public CommandSwallower(String command, Predicate<IChatComponent> filter, int lineCount) {
		this.command = command;
		this.filter = filter;
		this.response = new String[lineCount];
	}
	
	@Override
	public void dispatch() {
		BM2.info("Dispatching CommandSwallower with msg \"%s\"", this.command);
		MinecraftForge.EVENT_BUS.register(this);
		Minecraft.getMinecraft().thePlayer.sendChatMessage(this.command);
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if (this.filter.test(event.message)) {
			this.response[this.index++] = event.message.getUnformattedText();
			event.setCanceled(true);
		}
		if (this.index >= this.response.length) {
			this.done = true;
			MinecraftForge.EVENT_BUS.unregister(this);
			this.callback.accept(this.response);
			BM2.info("CommandSwallower \"%s\" closed", this.command);
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
	public CommandSwallower promise(Consumer<String[]> callback) {
		this.callback = callback;
		return this;
	}
	
}
