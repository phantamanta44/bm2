package io.github.phantamanta44.bm2.etc.command;

import io.github.phantamanta44.bm2.etc.NameLookup;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Lists;



@SuppressWarnings("rawtypes")
public class NameLookupCommand implements ICommand {

	private static final String USERNAME_REGEX = "[a-zA-Z0-9_]{3,16}";
	private static final String RESP_HEADER = EnumChatFormatting.AQUA + "<== NAME HISTORY FOR %s ========>";
	private static final String RESP_DATA = EnumChatFormatting.GRAY + "%1$16s" + EnumChatFormatting.DARK_GRAY + " | " + EnumChatFormatting.GRAY + "%s";
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
		return Lists.newArrayList("nmc");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1 || !args[0].matches(USERNAME_REGEX))
			throw new SyntaxErrorException(getCommandUsage(sender));
		new NameLookup(args[0]).promise(r -> {
			sender.addChatMessage(new ChatComponentText(String.format(RESP_HEADER, args[0])));
			r.history.forEach(e -> {
				String date = e.changeDate > 0L ? DATE_FORMAT.format(new Date(e.changeDate)) : "Initial name";
				sender.addChatMessage(new ChatComponentText(String.format(RESP_DATA, e.name, date)));
			});
		});
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}

}
