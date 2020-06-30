package nl.thedutchmc.uhcplus.presets;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.thedutchmc.uhcplus.UhcPlus;

public class DefaultPreset {
	
	private UhcPlus plugin;
	
	public DefaultPreset(UhcPlus plugin) {
		this.plugin = plugin;
	}
	
	private File presetConfigFile;
	private FileConfiguration presetConfig;
	
	public FileConfiguration getPresetConfig() {
		return this.presetConfig;
	} 
	
	public void loadPreset(String presetName, boolean shouldReturn) {
		
		presetConfigFile = new File(plugin.getDataFolder() + File.separator + "presets", presetName + ".yml");
		
		presetConfig = YamlConfiguration.loadConfiguration(presetConfigFile);
		
		//Check if the config file exists, if it doesnt, create it.		
		if(!presetConfigFile.exists()) {
			presetConfigFile.getParentFile().mkdirs();
			
			try {
				FileUtils.copyToFile(plugin.getResource("default.yml"), presetConfigFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Load the preset configuration file.
		try {
			presetConfig.load(presetConfigFile);
			
			if(shouldReturn) {
				readPreset();
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			//TODO better error handling
		}
	}
	
	public void readPreset() {
		
		//String
		PresetHandler.maxTeamCount = this.getPresetConfig().getString("maxTeamCount");
		PresetHandler.maxPlayerCountPerTeam = this.getPresetConfig().getString("maxPlayerCountPerTeam");
		
		//Boolean
		PresetHandler.moduleOreAutoSmelt = Boolean.valueOf(this.getPresetConfig().getString("moduleOreAutoSmelt"));
		PresetHandler.moduleTreeFullRemove = Boolean.valueOf(this.getPresetConfig().getString("moduleTreeFullRemove"));	
		PresetHandler.moduleLeaveDecay = Boolean.valueOf(this.getPresetConfig().getString("moduleLeaveDecay"));
		PresetHandler.moduleEnchantedTools = Boolean.valueOf(this.getPresetConfig().getString("moduleEnchantedTools"));
		PresetHandler.moduleInfiniteEnchanting = Boolean.valueOf(this.getPresetConfig().getString("moduleInfiniteEnchanting"));
		PresetHandler.moduleSheepDropString = Boolean.valueOf(this.getPresetConfig().getString("moduleSheepDropString"));
		PresetHandler.moduleGravelDropArrow = Boolean.valueOf(this.getPresetConfig().getString("moduleGravelDropArrow"));
		PresetHandler.moduleDissalowGrindingEnchantedTools = Boolean.valueOf(this.getPresetConfig().getString("moduleDissalowGrindingEnchantedTools"));
		PresetHandler.moduleLightGoldenApple = Boolean.valueOf(this.getPresetConfig().getString("moduleLightGoldenApple"));
		PresetHandler.moduleLightAnvil = Boolean.valueOf(this.getPresetConfig().getString("moduleLightAnvil"));
		PresetHandler.moduleAntiCheat = Boolean.valueOf(this.getPresetConfig().getString("moduleAntiCheat"));
		PresetHandler.moduleAxeOfDestruction = Boolean.valueOf(this.getPresetConfig().getString("moduleAxeOfDestruction"));
		PresetHandler.axeOfDestructionLevelling = Boolean.valueOf(this.getPresetConfig().getString("axeOfDestructionLevelling"));

		
		//Integer
		PresetHandler.moduleOreAutoSmeltIngotDrop = Integer.valueOf(this.getPresetConfig().getString("moduleOreAutoSmeltIngotDrop"));	
		PresetHandler.timeToPvp = Integer.valueOf(this.getPresetConfig().getString("timeToPvp"));
		PresetHandler.worldBorderSize = Integer.valueOf(this.getPresetConfig().getString("worldBorderSize"));
		PresetHandler.worldBorderShrinkAfter = Integer.valueOf(this.getPresetConfig().getString("worldBorderShrinkAfter"));
		PresetHandler.worldBorderShrinkTo = Integer.valueOf(this.getPresetConfig().getString("worldBorderShrinkTo"));
		PresetHandler.gameTime = Integer.valueOf(this.getPresetConfig().getString("gameTime"));
		PresetHandler.moduleAntiCheatTime = Integer.valueOf(this.getPresetConfig().getString("moduleAntiCheatTime"));
	
	}
	
	public void writePreset(String presetName) {
		loadPreset(presetName, false);
		
		this.getPresetConfig().set("maxTeamCount", PresetHandler.maxTeamCount);
		this.getPresetConfig().set("maxPlayerCountPerTeam", PresetHandler.maxPlayerCountPerTeam);
		this.getPresetConfig().set("moduleOreAutoSmelt", PresetHandler.moduleOreAutoSmelt);
		this.getPresetConfig().set("moduleOreAutoSmeltIngotDrop", PresetHandler.moduleOreAutoSmeltIngotDrop);
		this.getPresetConfig().set("timeToPvp", PresetHandler.timeToPvp);
		this.getPresetConfig().set("worldBorderSize", PresetHandler.worldBorderSize);
		this.getPresetConfig().set("worldBorderShrinkAfter", PresetHandler.worldBorderShrinkAfter);
		this.getPresetConfig().set("worldBorderShrinkTo", PresetHandler.worldBorderShrinkTo);
		this.getPresetConfig().set("gameTime", PresetHandler.gameTime);
		this.getPresetConfig().set("moduleTreeFullRemove", PresetHandler.moduleTreeFullRemove);
		this.getPresetConfig().set("moduleLeaveDecay", PresetHandler.moduleLeaveDecay);
		this.getPresetConfig().set("moduleEnchantedTools", PresetHandler.moduleEnchantedTools);
		this.getPresetConfig().set("moduleInfiniteEnchanting", PresetHandler.moduleInfiniteEnchanting);
		this.getPresetConfig().set("moduleSheepDropString", PresetHandler.moduleSheepDropString);
		this.getPresetConfig().set("moduleGravelDropArrow", PresetHandler.moduleGravelDropArrow);
		this.getPresetConfig().set("moduleDissalowGrindingEnchantedTools", PresetHandler.moduleDissalowGrindingEnchantedTools);
		this.getPresetConfig().set("moduleLightGoldenApple", PresetHandler.moduleLightGoldenApple);
		this.getPresetConfig().set("moduleLightAnvil", PresetHandler.moduleLightAnvil);
		this.getPresetConfig().set("moduleAntiCheat", PresetHandler.moduleAntiCheat);
		this.getPresetConfig().set("moduleAntiCheatTime", PresetHandler.moduleAntiCheatTime);
		this.getPresetConfig().set("moduleAxeOfDestruction", PresetHandler.moduleAxeOfDestruction);
		this.getPresetConfig().set("axeOfDestructionLevelling", PresetHandler.axeOfDestructionLevelling);
		
		savePreset();
	}
	
	public void savePreset() {
		try {
			this.getPresetConfig().save(presetConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}