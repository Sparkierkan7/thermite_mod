package thermite.therm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import thermite.therm.ServerState;
import thermite.therm.ThermMod;
import thermite.therm.ThermPlayerState;

public class DrinkIceJuiceC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        /*ServerState serverState = ServerState.getServerState(server);
        ThermPlayerState playerState = ServerState.getPlayerState(player);

        playerState.temp -= 10;
        if (playerState.temp < 0) {playerState.temp = 0;}
        serverState.markDirty();*/

    }

}
