package nl.thedutchmc.uhcplus.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import nl.thedutchmc.uhcplus.commands.subcommands.Kits;
import nl.thedutchmc.uhcplus.commands.subcommands.Preset;
import nl.thedutchmc.uhcplus.commands.subcommands.Teams;
import nl.thedutchmc.uhcplus.uhc.UhcHandler;
import nl.thedutchmc.uhcplus.UhcPlus;

public class UhcpCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		ChatColor cr = ChatColor.RED;
		ChatColor cg = ChatColor.GOLD;
		ChatColor cw = ChatColor.WHITE;

		// Check if the entered command is equal to uhcp, because this is our base
		// command we dont need to handle anything else
		if (command.getName().equalsIgnoreCase("uhcp")) {

			// Check if the player gave any arguments ('subcommands'), if none are given,
			// they just entered /uhcp, show them how to get the help page.
			if (args.length == 0) {
				sender.sendMessage(cr + "Invalid usage! See /uhcp help for help.");

			} else { // Anything else than /uhcp, thus some arguments are given

				// uhcp help
				if (args[0].equalsIgnoreCase("help")) { // Check if args[0] (the first 'subcommand') is help, if so send
														// the player the help page

					if (!sender.hasPermission("uhcp.help")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
						return true;
					}

					sender.sendMessage(cg + "UHCPlus Help Page");
					sender.sendMessage(cg + "---------------");
					sender.sendMessage("- " + cg + "/uhcp " + cw + "UHCPlus base command.");
					sender.sendMessage("- " + cg + "/uhcp help " + cw + "Shows this page.");
					sender.sendMessage(
							"- " + cg + "/uhcp version " + cw + "Shows you the version of UHCPlus you are on.");
					sender.sendMessage("- " + cg + "/uhcp preset " + cw + "Manage the presets.");
					sender.sendMessage("- " + cg + "/uhcp start <resort teams> " + cw
							+ "Start the UHC. The <resort teams> is not required, and defaults to false.");

					// uhcp version
				} else if (args[0].equalsIgnoreCase("version")) { // Returns the version to the sender

					if (!sender.hasPermission("uhcp.version")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
						return true;
					}

					sender.sendMessage(cg + "You are on UHCPlus version " + cr + UhcPlus.INSTANCE.getDescription().getVersion() + cg + ".");

					// uhcp start
				} else if (args[0].equalsIgnoreCase("start")) { // /uhcp start

					if (!sender.hasPermission("uhcp.start")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
						return true;
					}

					UhcHandler uhcHandler = new UhcHandler();

					if (args.length == 2) {

						// Check if the second arg is true or false, this means resort or do not resort
						// teams.
						if (args[1].equalsIgnoreCase("true")) {
							uhcHandler.startUhc(true);
						} else {
							uhcHandler.startUhc(false);
						}

						// the second arg is not given, so default to false.
					} else {
						uhcHandler.startUhc(false);
					}

					// uhcp presets <args>
				} else if (args[0].equalsIgnoreCase("preset")) {

					// process the command in the preset class
					Preset presets = new Preset();
					presets.presetSubcommand(sender, command, label, args);

					// uhcp teams <args>
				} else if (args[0].equalsIgnoreCase("teams")) {

					// process the command in the teams class
					Teams teams = new Teams();
					teams.teamsSubcommand(sender, command, label, args);

					
				} else if(args[0].equalsIgnoreCase("kits")) {
					
					Kits kits = new Kits();
					kits.kitCommand(sender, command, label, args);
				}
			}
		}
		return true;
	}
}
