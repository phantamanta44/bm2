package io.github.phantamanta44.bm2.etc.command;

import io.github.phantamanta44.bm2.core.lang.Lang;
import io.github.phantamanta44.bm2.etc.EtcLang;
import io.github.phantamanta44.bm2.etc.NameLookup;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Lists;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NameLookupCommand implements ICommand {

	private static final List ALIASES = Lists.newArrayList("nmc");
	private static final String USERNAME_REGEX = "[a-zA-Z0-9_]{3,16}";
	private static final String RESP_HEADER = EnumChatFormatting.AQUA + "<== %s ========>";
	private static final String RESP_DATA = EnumChatFormatting.GRAY + "%s" + EnumChatFormatting.DARK_GRAY + " | " + EnumChatFormatting.GRAY + "%s";
	private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL);
	
	@Override
	public int compareTo(Object o) {
		return this.getName().compareTo(((ICommand)o).getName());
	}

	@Override
	public String getName() {
		return "namemc";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/namemc <name>";
	}

	@Override
	public List getAliases() {
		return ALIASES;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1 || !args[0].matches(USERNAME_REGEX))
			throw new SyntaxErrorException(getCommandUsage(sender));
		new NameLookup(args[0]).promise(r -> {
			if (r == null) {
				sender.addChatMessage(new ChatComponentText(Lang.get(EtcLang.NL_ERROR)));
				return;
			}
			if (r.history.isEmpty()) {
				sender.addChatMessage(new ChatComponentText(Lang.get(EtcLang.NL_FALSE)));
				return;
			}
			sender.addChatMessage(new ChatComponentText(String.format(String.format(RESP_HEADER, Lang.get(EtcLang.NL_HEADER)), r.history.get(r.history.size() - 1).name)));
			r.history.forEach(e -> {
				String date = e.changeDate > 0L ? DATE_FORMAT.format(new Date(e.changeDate)) : "Initial name";
				sender.addChatMessage(new ChatComponentText(String.format(RESP_DATA, e.name, date)));
			});
		}).dispatch();
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
		return index == 0;
	}

}
