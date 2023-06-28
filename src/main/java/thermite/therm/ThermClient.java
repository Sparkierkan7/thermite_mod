package thermite.therm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import thermite.therm.client.TemperatureHudOverlay;
import thermite.therm.networking.ThermNetworkingPackets;

public class ThermClient implements ClientModInitializer {

    public static long clientStoredTemperature = 70;
    public static short clientStoredTempDir = 32;

    public static int tempTickCounter = 0;
    public static final int tempTickCount = 10;

    @Override
    public void onInitializeClient() {

        ThermNetworkingPackets.registerS2CPackets();

        //hud
        HudRenderCallback.EVENT.register(new TemperatureHudOverlay());

        //tick
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (client.world != null) {
                if (client.world.isClient()) {

                    if (tempTickCounter < tempTickCount) {
                        tempTickCounter += 1;
                    } else if (tempTickCounter >= tempTickCount) {
                        tempTickCounter = 0;
                        ClientPlayNetworking.send(ThermNetworkingPackets.PLAYER_TEMP_TICK_C2S_PACKET_ID, PacketByteBufs.create());
                    }
                }
            }
        });

    }

}
