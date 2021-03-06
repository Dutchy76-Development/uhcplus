package nl.thedutchmc.uhcplus.modules.modules.kits;

import java.io.Serializable;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Kit implements Serializable {

    private static final long serialVersionUID = 1L;
	
	private String kitName;
	private List<ItemStack> kitItems;
	private boolean kitEnabled = false;
	
	public Kit(String kitName) {
		this.kitName = kitName;
	}
	
	public void setKitItems(List<ItemStack> kitItems) {
		this.kitItems = kitItems;
	}
	
	public List<ItemStack> getKitItems() {
		return kitItems;
	}
	
	public void setKitName(String kitName) {
		this.kitName = kitName;
	}
	
	public String getKitName() {
		return kitName;
	}
	
	public void setKitEnabled(boolean enabled) {
		kitEnabled = enabled;
	}
	
	public boolean getKitEnabled() {
		return kitEnabled;
	}
	
}
