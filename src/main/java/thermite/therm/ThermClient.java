package thermite.therm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import thermite.therm.client.TemperatureHudOverlay;
import thermite.therm.networking.ThermNetworkingPackets;

public class ThermClient implements ClientModInitializer {

    public static long clientStoredTemperature = 70;
    public static short clientStoredTempDir = 32;

    public static int tempTickCounter = 0;
    public static final int tempTickCount = 20;

    public static boolean showGui = true;
    private static KeyBinding showGuiKey;

    public static int glassShakeTick = 0;
    public static int glassShakeTickMax = 0;
    public static int glassShakePM = -1;
    public static boolean glassShakeAxis = false;

    @Override
    public void onInitializeClient() {

        showGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle Temperature Gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "Thermite"
        ));

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
                        boolean paused = false;
                        if (client.isInSingleplayer() && client.isPaused()) {
                            paused = true;
                        }
                        if (!paused && !client.player.isCreative() && !client.player.isSpectator()) {
                            ClientPlayNetworking.send(ThermNetworkingPackets.PLAYER_TEMP_TICK_C2S_PACKET_ID, PacketByteBufs.create());
                        }
                        tempTickCounter = 0;
                    }
                }
            }

            //keybinds
            while (showGuiKey.wasPressed()) {
                if (showGui) {
                    showGui = false;
                } else {
                    showGui = true;
                }
            }

        });

    }

}
