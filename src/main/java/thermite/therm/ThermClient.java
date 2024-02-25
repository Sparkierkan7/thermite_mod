package thermite.therm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import thermite.therm.client.TemperatureHudOverlay;
import thermite.therm.networking.ThermNetworkingPackets;

import java.util.Objects;
import java.util.Random;

public class ThermClient implements ClientModInitializer {

    public static long clientStoredTemperature = 70;
    public static short clientStoredTempDir = 32;
    public static double clientStoredWindPitch = 0;
    public static double clientStoredWindYaw = 0;
    public static double clientStoredWindTemp = 0;

    public static boolean windParticles = false;

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
                GLFW.GLFW_KEY_UNKNOWN,
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
                            windParticles = false;
                        }
                        if (!paused && !client.player.isCreative() && !client.player.isSpectator()) {
                            ClientPlayNetworking.send(ThermNetworkingPackets.PLAYER_TEMP_TICK_C2S_PACKET_ID, PacketByteBufs.create());
                            windParticles = true;
                        }
                        tempTickCounter = 0;
                    }

                    if (windParticles && ThermMod.config.enableWindParticles) {
                        Random rand = new Random();

                        int bound = 16+(int)clientStoredWindTemp;
                        if (bound <= 0) {bound = 1;}

                        int shouldSpawn = rand.nextInt(0, bound);

                        if (clientStoredWindTemp < -3 && shouldSpawn == 0) {
                            for (int i = 0; i < 1; i++) {
                                Vec3d dir = new Vec3d((Math.cos(clientStoredWindPitch) * Math.cos(clientStoredWindYaw)), (Math.sin(clientStoredWindPitch) * Math.cos(clientStoredWindYaw)), Math.sin(clientStoredWindYaw));

                                dir = dir.negate();

                                double randX = rand.nextDouble(-10, 10);
                                double randY = rand.nextDouble(-5, 10);
                                double randZ = rand.nextDouble(-10, 10);

                                client.world.addParticle(ParticleTypes.CLOUD, client.player.getX()+randX-dir.x*7, client.player.getY()+randY, client.player.getZ()+randZ-dir.z*7, dir.x, dir.y, dir.z);
                            }
                        }
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

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, list) -> {

            if (stack.getNbt() != null) {
                NbtCompound nbt = stack.getNbt();
                int warmth = nbt.getInt("wool");
                if (stack.isOf(Items.LEATHER_HELMET)) {
                    warmth += ThermMod.config.helmetTempItems.get("leather_helmet");
                    list.add(Text.literal("§9+" + warmth + " Warmth"));
                } else if (stack.isOf(Items.LEATHER_CHESTPLATE)) {
                    warmth += ThermMod.config.chestplateTempItems.get("leather_chestplate");
                    list.add(Text.literal("§9+" + warmth + " Warmth"));
                } else if (stack.isOf(Items.LEATHER_LEGGINGS)) {
                    warmth += ThermMod.config.leggingTempItems.get("leather_leggings");
                    list.add(Text.literal("§9+" + warmth + " Warmth"));
                } else if (stack.isOf(Items.LEATHER_BOOTS)) {
                    warmth += ThermMod.config.bootTempItems.get("leather_boots");
                    list.add(Text.literal("§9+" + warmth + " Warmth"));
                }
            }

        });

    }

}
