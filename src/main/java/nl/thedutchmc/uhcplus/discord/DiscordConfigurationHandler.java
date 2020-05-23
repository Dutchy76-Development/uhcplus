package nl.thedutchmc.uhcplus.discord;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import nl.thedutchmc.uhcplus.UhcPlus;

public class DiscordConfigurationHandler {

	public static String token;
	public static List<String> voiceChannelIds;
	
	private UhcPlus plugin;
	 
	public DiscordConfigurationHandler(UhcPlus plugin) {
		this.plugin = plugin;
	}
	
	private File configFile;
	private FileConfiguration config;
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public void loadDiscordConfig() {
		configFile = new File(plugin.getDataFolder(), "discordConfig.yml");
		
		if(!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			
			plugin.saveResource("discordConfig.yml", false);
		}
		
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void readConfig() {
		token = this.getConfig().getString("token");
		voiceChannelIds = (List<String>) this.getConfig().getList("voiceChannels");
	}
}
