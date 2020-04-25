package nl.thedutchmc.uhcplus.teams;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Team {

	private List<UUID> teamMembers;
	private ChatColor teamColor;
	private List<UUID> teamMembersAlive;
	private int teamId;
	
	public Team(int teamId) {
		this.teamId = teamId;
		teamMembers = null;
	}
	
	public List<UUID> getTeamMembers() {
		return teamMembers;
	}
	
	public void setTeamMembers(List<UUID> teamMembers) {
		this.teamMembers = teamMembers;
	}
	
	public ChatColor getTeamColor() {
		return teamColor;
	}
	
	public void setTeamColor(ChatColor teamColor) {
		this.teamColor = teamColor;
	}
	
	public List<UUID> getAliveteamMembers() {
		return teamMembersAlive;
	}
	
	public void setAliveTeamMembers(List<UUID> teamMembersAlive) {
		this.teamMembersAlive = teamMembersAlive;
	}
	
	public int getTeamId() {
		return teamId;
	}
	
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	public int getTeamSize() {
		return teamMembers.size();
	}
	
	public void playerJoinTeam(UUID playerUuid) {
		teamMembers.add(playerUuid);
	}
	
	public void playerLeaveTeam(UUID playerUuid) {
		teamMembers.remove(playerUuid);
	}
	
	public boolean isPlayerInTeam(UUID playerUuid) {
		return teamMembers.contains(playerUuid);
	}
	
}
