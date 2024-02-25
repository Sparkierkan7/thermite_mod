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

    String worldVersion = "4.1.0.8";
    int testInt = 0;
    public int season = 0;
    public int seasonTick = 0;
    public long currentSeasonTick = 0;
    public int seasonalWeatherTick = 0;

    public double windPitch = 360*Math.PI/180;
    public double windYaw = 0;
    public int windRandomizeTick = 0;
    public double windTempModifierRange = 8;
    public double windTempModifier = 0;
    public double precipitationWindModifier = 0;

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
            playerStateNbt.putDouble("baseWindTemp", playerSate.baseWindTemp);
            playerStateNbt.putDouble("windTemp", playerSate.windTemp);
            playerStateNbt.putDouble("windTurbulence", playerSate.windTurbulence);

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.put("players", playersNbtCompound);

        nbt.putString("worldVersion", worldVersion);
        nbt.putInt("testInt", testInt);
        nbt.putInt("season", season);
        nbt.putInt("seasonTick", seasonTick);
        nbt.putLong("currentSeasonTick", currentSeasonTick);
        nbt.putInt("seasonalWeatherTick", seasonalWeatherTick);
        nbt.putDouble("windPitch", windPitch);
        nbt.putDouble("windYaw", windYaw);
        nbt.putInt("windRandomizeTick", windRandomizeTick);
        nbt.putDouble("windTempModifierRange", windTempModifierRange);
        nbt.putDouble("windTempModifier", windTempModifier);
        nbt.putDouble("precipitationWindModifier", precipitationWindModifier);

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
            playerState.baseWindTemp = playersTag.getCompound(key).getDouble("baseWindTemp");
            playerState.windTemp = playersTag.getCompound(key).getDouble("windTemp");
            playerState.windTurbulence = playersTag.getCompound(key).getDouble("windTurbulence");

            UUID uuid = UUID.fromString(key);
            serverState.players.put(uuid, playerState);
        });

        serverState.worldVersion = tag.getString("worldVersion");
        serverState.testInt = tag.getInt("testInt");
        serverState.season = tag.getInt("season");
        serverState.seasonTick = tag.getInt("seasonTick");
        serverState.currentSeasonTick = tag.getLong("currentSeasonTick");
        serverState.seasonalWeatherTick = tag.getInt("seasonalWeatherTick");
        serverState.windPitch = tag.getDouble("windPitch");
        serverState.windYaw = tag.getDouble("windYaw");
        serverState.windRandomizeTick = tag.getInt("windRandomizeTick");
        serverState.windTempModifierRange = tag.getDouble("windTempModifierRange");
        serverState.windTempModifier = tag.getDouble("windTempModifier");
        serverState.precipitationWindModifier = tag.getDouble("precipitationWindModifier");

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