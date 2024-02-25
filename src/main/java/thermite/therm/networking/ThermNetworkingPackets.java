package thermite.therm.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import thermite.therm.ThermClient;
import thermite.therm.ThermMod;
import thermite.therm.networking.packet.DrinkIceJuiceC2SPacket;
import thermite.therm.networking.packet.PlayerTempTickC2SPacket;

public class ThermNetworkingPackets {

    //c2s
    public static final Identifier PLAYER_TEMP_TICK_C2S_PACKET_ID = new Identifier(ThermMod.modid, "player_temp_tick_c2s_packet");
    public static final Identifier DRINK_ICE_JUICE_C2S_PACKET_ID = new Identifier(ThermMod.modid, "drink_ice_juice_c2s_packet");

    //s2c
    public static final Identifier SEND_THERMPLAYERSTATE_S2C_PACKET_ID = new Identifier(ThermMod.modid, "send_thermplayerstate_s2c_packet");

    public static void registerC2SPackets() {

        ServerPlayNetworking.registerGlobalReceiver(PLAYER_TEMP_TICK_C2S_PACKET_ID, PlayerTempTickC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DRINK_ICE_JUICE_C2S_PACKET_ID, DrinkIceJuiceC2SPacket::receive);

    }

    public static void registerS2CPackets() {

        ClientPlayNetworking.registerGlobalReceiver(SEND_THERMPLAYERSTATE_S2C_PACKET_ID, (client, handler, buf, responseSender) -> {
            double temperature = buf.readDouble();
            short td = buf.readShort();
            double windPitch = buf.readDouble();
            double windYaw = buf.readDouble();
            double windTemp = buf.readDouble();
            client.execute(() -> {

                ThermClient.clientStoredTemperature = Math.round(temperature);
                ThermClient.clientStoredTempDir = td;
                ThermClient.clientStoredWindPitch = windPitch;
                ThermClient.clientStoredWindYaw = windYaw;
                ThermClient.clientStoredWindTemp = windTemp;

            });
        });

    }
}