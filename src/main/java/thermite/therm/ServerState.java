package thermite.therm;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {

    String worldVersion = "3.2.0";
    int testInt = 0;
    public int season = 0;
    public int seasonTick = 0;
    public long currentSeasonTick = 0;
    public int seasonalWeatherTick = 0;

    public HashMap<UUID, ThermPlayerState> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {

        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            NbtCompound playerStateNbt = new NbtCompound();

            playerStateNbt.putInt("testplayerint", playerSate.testplayerint);
            playerStateNbt.putDouble("temp", playerSate.temp);
            playerStateNbt.putDouble("tempRate", playerSate.tempRate);
            playerStateNbt.putDouble("restingTemp", playerSate.restingTemp);
            playerStateNbt.putDouble("minTemp", playerSate.minTemp);
            playerStateNbt.putDouble("maxTemp", playerSate.maxTemp);
            playerStateNbt.putString("damageType", playerSate.damageType);
            playerStateNbt.putInt("damageTick", playerSate.damageTick);
            playerStateNbt.putInt("maxDamageTick", playerSate.maxDamageTick);
            playerStateNbt.putInt("searchFireplaceTick", playerSate.searchFireplaceTick);
            playerStateNbt.putInt("tempModifier", playerSate.tempModifier);

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.put("players", playersNbtCompound);

        nbt.putString("worldVersion", worldVersion);
        nbt.putInt("testInt", testInt);
        nbt.putInt("season", season);
        nbt.putInt("seasonTick", seasonTick);
        nbt.putLong("currentSeasonTick", currentSeasonTick);
        nbt.putInt("seasonalWeatherTick", seasonalWeatherTick);
        return nbt;
    }

    public static ServerState createFromNbt(NbtCompound tag) {

        ServerState serverState = new ServerState();

        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            ThermPlayerState playerState = new ThermPlayerState();

            playerState.testplayerint = playersTag.getCompound(key).getInt("testplayerint");
            playerState.temp = playersTag.getCompound(key).getDouble("temp");
            playerState.tempRate = playersTag.getCompound(key).getDouble("tempRate");
            playerState.restingTemp = playersTag.getCompound(key).getDouble("restingTemp");
            playerState.minTemp = playersTag.getCompound(key).getDouble("minTemp");
            playerState.maxTemp = playersTag.getCompound(key).getDouble("maxTemp");
            playerState.damageType = playersTag.getCompound(key).getString("damageType");
            playerState.damageTick = playersTag.getCompound(key).getInt("damageTick");
            playerState.maxDamageTick = playersTag.getCompound(key).getInt("maxDamageTick");
            playerState.searchFireplaceTick = playersTag.getCompound(key).getInt("searchFireplaceTick");
            playerState.tempModifier = playersTag.getCompound(key).getInt("tempModifier");

            UUID uuid = UUID.fromString(key);
            serverState.players.put(uuid, playerState);
        });

        serverState.worldVersion = tag.getString("worldVersion");
        serverState.testInt = tag.getInt("testInt");
        serverState.season = tag.getInt("season");
        serverState.seasonTick = tag.getInt("seasonTick");
        serverState.currentSeasonTick = tag.getLong("currentSeasonTick");
        serverState.seasonalWeatherTick = tag.getInt("seasonalWeatherTick");

        return serverState;
    }

    public static ServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server
                .getWorld(World.OVERWORLD).getPersistentStateManager();

        ServerState serverState = persistentStateManager.getOrCreate(
                ServerState::createFromNbt,
                ServerState::new,
                ThermMod.modid);

        return serverState;
    }

    public static ThermPlayerState getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(player.getWorld().getServer());

        ThermPlayerState playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new ThermPlayerState());

        return playerState;
    }
}