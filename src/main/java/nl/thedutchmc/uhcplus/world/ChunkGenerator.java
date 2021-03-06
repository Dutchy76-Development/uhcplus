package nl.thedutchmc.uhcplus.world;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.lib.PaperLib;
import nl.thedutchmc.uhcplus.UhcPlus;
import nl.thedutchmc.uhcplus.events.GameStateChangedEvent;
import nl.thedutchmc.uhcplus.uhc.GameState;
import nl.thedutchmc.uhcplus.uhc.UhcHandler;

public class ChunkGenerator {

	static World overworld;
	static int x = 700;
	static int z;
	static int cIndex = 0;
	static Chunk chunk = null;
	static int chunksDone = 0;
	static int checkEvery;
	static int checkEveryOriginal;
	static int progress = 0;

	public void generateChunks() {

		int worldRadius = 700;

		int startingCoordZ = worldRadius;

		z = -startingCoordZ;

		overworld = Bukkit.getServer().getWorld("uhcworld");

		// Calculate the amount of chunks
		double chunkCount = Math.pow((worldRadius / 8), 2);
		checkEvery = (int) chunkCount / 20;
		checkEveryOriginal = checkEvery;

		// Generate the chunks
		new BukkitRunnable() {

			@Override
			public void run() {

				if (!(Runtime.getRuntime().freeMemory() < 524288000)) { // In Bytes, so 500MB
					if (x > -worldRadius) {
						Location location = new Location(overworld, x, 0, z);
						PaperLib.getChunkAtAsync(location, true);

						chunk = location.getChunk();

						if (z > worldRadius) {
							x -= 16;
							z = -startingCoordZ;
						}

						cIndex++;
						chunksDone++;

						if (cIndex == 100) {
							overworld.save();
							cIndex = 0;
						}

						if (chunksDone == checkEvery) {
							progress += 5;
							checkEvery += checkEveryOriginal;
							UhcPlus.logInfo("Chunk generation progress: " + progress + "%");
						}

						if (chunk != null) {
							overworld.unloadChunkRequest(x, z);
						}

						z += 16;
					} else {
						UhcPlus.logInfo("World generated.Players may now join");
						Bukkit.getPluginManager().callEvent(new GameStateChangedEvent(UhcHandler.getGameState(), GameState.LOBBY));
						UhcHandler.setGameState(GameState.LOBBY);

						this.cancel();
					}
				}
			}
		}.runTaskTimer(UhcPlus.INSTANCE, 200, 1);
	}

}
