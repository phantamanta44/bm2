package io.github.phantamanta44.bm2.etc.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LennyCommand implements ICommand {

	private static final String LENNY = "%s ( \u0361\u00B0 \u035C\u0296 \u0361\u00B0)";
	
	@Override
	public int compareTo(Object o) {
		return this.getName().compareTo(((ICommand)o).getName());
	}

	@Override
	public String getName() {
		return "lenny";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/lenny <message>";
	}

	@Override
	public List getAliases() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		StringBuilder msg = new StringBuilder();
		Arrays.stream(args).forEach(s -> msg.append(s + " "));
		Minecraft.getMinecraft().thePlayer.sendChatMessage(String.format(LENNY, msg.toString()));
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return (List)Minecraft.getMinecraft().getNetHandler().func_175106_d().stream()
			.map(n -> ((NetworkPlayerInfo)n).getGameProfile().getName())
			.collect(Collectors.toList());
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return true;
	}

}
