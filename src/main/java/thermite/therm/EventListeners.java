package thermite.therm;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.Random;

import static thermite.therm.ThermMod.modVersion;

public class EventListeners {

    public static void register() {

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

            ServerState serverState = ServerState.getServerState(handler.player.getWorld().getServer());
            ThermPlayerState playerState = ServerState.getPlayerState(handler.player);

            if (!Objects.equals(serverState.worldVersion, modVersion)) {

                serverState.windTempModifierRange = 8;
                serverState.windRandomizeTick = 24000;
                serverState.worldVersion = modVersion;

                serverState.players.forEach((uuid, state) -> {
                    state.windTurbulence = 23;
                });

                serverState.markDirty();
                ThermMod.LOGGER.info("Updated Thermite ServerState.");

            }

        });

        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            ServerState serverState = ServerState.getServerState(server);

            if (serverState.windRandomizeTick >= 24000) {
                serverState.windRandomizeTick = 0;

                Random rand = new Random();
                serverState.windPitch = 360*Math.PI/180;
                serverState.windYaw = rand.nextDouble(0, 360)*Math.PI/180;
                serverState.windTempModifier = rand.nextDouble(-serverState.windTempModifierRange, serverState.windTempModifierRange);
                serverState.precipitationWindModifier = rand.nextDouble(-serverState.windTempModifierRange, 0);

                serverState.markDirty();
                ThermMod.LOGGER.info("========WIND RANDOMIZED========");

            }
            serverState.windRandomizeTick += 1;

            server.getPlayerManager().getPlayerList().forEach((player) -> {

                //ThermMod.LOGGER.info()

            });

        });

    }

    private static void temperatureTick(MinecraftServer server, ServerPlayerEntity player) {

    }
}
