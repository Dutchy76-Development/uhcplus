package nl.thedutchmc.uhcplus.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.thedutchmc.uhcplus.UhcPlus;
import nl.thedutchmc.uhcplus.players.PlayerHandler;
import nl.thedutchmc.uhcplus.players.PlayerObject;
import nl.thedutchmc.uhcplus.presets.PresetHandler;

public class TeamHandler {
	
	@SuppressWarnings("unused")
	private UhcPlus plugin;

	private int maxTeamCount = Integer.valueOf(PresetHandler.maxTeamCount);
	private int maxPlayerCountPerTeam = Integer.valueOf(PresetHandler.maxPlayerCountPerTeam);
	
	private boolean shouldReturn = false;
	
	public static boolean teamManuallySelect = false;
	
	private CommandSender sender;
	public static List<Team> teams = new ArrayList<>();
	
	public TeamHandler(UhcPlus plugin, CommandSender sender, boolean shouldReturn) {
		this.plugin = plugin;
		this.sender = sender;
		this.shouldReturn = shouldReturn;
	}
	
	public void createTeams() {		
		for(int i = 0; i < maxTeamCount; i++) {
			teams.add(new Team(i));
		}	
	}
	
	
	public void playerJoinTeam(int teamId, UUID uuid) {
	
		//Check if teams have been made yet, if not do so
		if(teams.isEmpty()) {
			createTeams();
		}
		
		System.out.println("1");
		
		//Loop over all teams
		for(Team team : teams) {
			
			//Check if the current team is the team the player wants to join
			if(team.getTeamId() == teamId) {
				
				System.out.println("2");
				
				//Check if the player is already in the team, if yes inform them
				if(team.getTeamMembers().contains(uuid)) {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + "You are already in this team!");
				} else {
					
					//Check if the selected team isnt full yet
					if(team.getTeamSize() < maxPlayerCountPerTeam) {
						
						System.out.println("3");

						//Add the player to the team
						team.playerJoinTeam(uuid);
						
						//Set the player object values
						PlayerHandler playerHandler = new PlayerHandler();
						PlayerObject playerObject = playerHandler.addPlayerToListAndReturn(uuid);
						playerObject.setTeam(team);
						playerObject.setTeamChatEnabled(true);
						
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + "You joined team " + ChatColor.RED + team.getTeamId());
					} else {

						Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + "Sorry, this team is full.");
					}
				}
			}
		}
	}
	
	public void sortPlayersNotInTeam() {
		
		List<UUID> playersInTeams = new ArrayList<>();
		List<UUID> playersToBeSorted = new ArrayList<>();
				
		for(Team team : teams) {
			
			for(UUID player : team.getTeamMembers()) {
				
				playersInTeams.add(player);	
			}
		}
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			
			System.out.println("1");
			
			if(!playersInTeams.contains(player.getUniqueId())) {
				
				System.out.println("2");
				
				playersToBeSorted.add(player.getUniqueId());
			}
		}
		
		System.out.println("ptbs size " + playersToBeSorted.size());
		
		List<UUID> playersLeftUnsorted = new ArrayList<>();
		
		for(UUID uuid : playersToBeSorted) {
			
			boolean playerInTeam = false;
			
			for(Team team : teams) {
				
				if(team.getTeamSize() != maxPlayerCountPerTeam && !playerInTeam) {
					team.playerJoinTeam(uuid);
					
					PlayerHandler playerHandler = new PlayerHandler();
					PlayerObject playerObject = playerHandler.addPlayerToListAndReturn(uuid);
					
					playerObject.setTeam(team);
					playerObject.setTeamChatEnabled(true);
					
					playerInTeam = true;
					
					break;
				}	
			}
			
			if(!playerInTeam) {
				playersLeftUnsorted.add(uuid);
			}
		}
		
		System.out.println("plu size " + playersLeftUnsorted.size());
		
		if(playersLeftUnsorted.size() != 0) {
			
			for(UUID uuid : playersLeftUnsorted) {
				Bukkit.getPlayer(uuid).sendMessage(ChatColor.GOLD + "You were not put into a team since all teams were full. You will be a spectator");
				Bukkit.getPlayer(uuid).setGameMode(GameMode.SPECTATOR);
			}
			
		}
	}
	
	public void playerRandomTeamJoiner() {
		
		teams.clear();
		createTeams();
		
		Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
		List<UUID> playersNotInTeam = new ArrayList<>();
		Bukkit.getServer().getOnlinePlayers().toArray(players);
		
		if((Bukkit.getServer().getOnlinePlayers().size() / maxTeamCount) > maxPlayerCountPerTeam && shouldReturn) {
			sender.sendMessage(ChatColor.RED + "There are more players than that can be fit into all the teams! There might be spectators!");
		}
		
		for(int i = 0; i < players.length; i++) {
			
			UUID playerUuid = players[i].getUniqueId();
			
			boolean isPlayerInTeam = false;
			
			for(Team team : teams) {
				
				if(team.getTeamSize() != maxPlayerCountPerTeam && !team.isPlayerInTeam(playerUuid)) {
					team.playerJoinTeam(playerUuid);
					
					PlayerHandler playerHandler = new PlayerHandler();
					PlayerObject playerObject = playerHandler.addPlayerToListAndReturn(playerUuid);
					playerObject.setTeam(team);
					playerObject.setTeamChatEnabled(true);
					
					isPlayerInTeam = true;
					Bukkit.getServer().getPlayer(playerUuid).sendMessage(ChatColor.GOLD + "You are now in team " + ChatColor.RED + team.getTeamId());
					break;
				}
			}
			
			if(!isPlayerInTeam) {
				playersNotInTeam.add(playerUuid);
			}
			
		}
		
		String playersNotInTeamNames = "";
		
		for(UUID uuid : playersNotInTeam) {
			Player player = Bukkit.getServer().getPlayer(uuid);
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage(ChatColor.GOLD + "You are not put into a team, and are now a spectator.");
			
			playersNotInTeamNames += new String(player.getName() + ", ");
		}
		
		if(playersNotInTeamNames.length() > 1 && shouldReturn) {
			sender.sendMessage(ChatColor.GOLD + "The following players were not added to a team, and have been put in spectator mode: " + playersNotInTeamNames);
		}
	}
	
	
	boolean isTeamFull(int teamId) {
		
		for(Team team : teams) {
			
			if(team.getTeamId() == teamId) {
				return !(team.getTeamSize() <= maxPlayerCountPerTeam);
			}
		}
		return false;
	}
	
	public HashMap<Integer, Integer> getTeamSizes() {
		
		HashMap<Integer, Integer> returnTeamSizes = new HashMap<>();
		
		for(Team team : teams) {
			returnTeamSizes.put(team.getTeamId(), team.getTeamSize());
		}
		
		return returnTeamSizes;
		
	}
	
	public int teamsWithPlayers() {
		
		int teamsWithPlayers = 0;
		
		for(Team team : teams) {
			if(team.getTeamMembers().size() > 0) {
				teamsWithPlayers++;
			}
		}
		
		return teamsWithPlayers;
	}

	public int teamsAlive() {
		
		int teamsAlive = 0;
		
		for(Team team : teams) {
			if(team.getAliveTeamMembers().size() > 0) {
				teamsAlive++;
			}
		}
		
		return teamsAlive;
	}
	
	public static List<Team> getAliveTeams() {
		
		List<Team> aliveTeams = new ArrayList<>();
		
		for(Team team : teams) {
			if(team.getAliveTeamMembers().size() > 0) {
				aliveTeams.add(team);
			}
		}
		
		return aliveTeams;
	}

	public void playerDied(UUID uuid) {
		
		for(Team team : teams) {
			
			if(team.getAliveTeamMembers().contains(uuid)) {
				List<UUID> newAliveTeamMembers = new ArrayList<>();
				newAliveTeamMembers = team.getAliveTeamMembers();
				newAliveTeamMembers.remove(uuid);
				
				team.setAliveTeamMembers(newAliveTeamMembers);
			}
		}
	}
}
