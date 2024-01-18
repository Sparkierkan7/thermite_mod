package thermite.therm.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import thermite.therm.ThermClient;
import thermite.therm.ThermMod;

import java.util.Objects;

public class TemperatureHudOverlay implements HudRenderCallback {

    private static final Identifier THERMOMETER_FRAME = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_frame.png");
    private static final Identifier THERMOMETER_GAUGE = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_gauge_fix_1.png");
    private static final Identifier THERMOMETER_HAND = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_hand.png");
    private static final Identifier THERMOMETER_SNOWFLAKE = new Identifier(ThermMod.modid, "textures/thermometer/snowflake_icon_8x8.png");
    private static final Identifier THERMOMETER_FLAME = new Identifier(ThermMod.modid, "textures/thermometer/flame_icon_8x8.png");
    private static final Identifier THERMOMETER_STILL = new Identifier(ThermMod.modid, "textures/thermometer/temperate_icon.png");

    private static final Identifier THERMOMETER_DISPLAY = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_display.png");

    //glass thermometer
    private static final Identifier TEMPERATE_GLASS = new Identifier(ThermMod.modid, "textures/glass_thermometer/temperate_glass.png");
    private static final Identifier COLD_GLASS = new Identifier(ThermMod.modid, "textures/glass_thermometer/cold_glass.png");
    private static final Identifier FROZEN_GLASS = new Identifier(ThermMod.modid, "textures/glass_thermometer/frozen_glass.png");
    private static final Identifier HOT_GLASS = new Identifier(ThermMod.modid, "textures/glass_thermometer/hot_glass.png");
    private static final Identifier BLAZING_GLASS = new Identifier(ThermMod.modid, "textures/glass_thermometer/blazing_glass.png");

    private static final Identifier COOLING_OUTLINE = new Identifier(ThermMod.modid, "textures/glass_thermometer/cooling_outline.png");
    private static final Identifier COOLING_OUTLINE_SMALL = new Identifier(ThermMod.modid, "textures/glass_thermometer/cooling_small_outline.png");
    private static final Identifier HEATING_OUTLINE = new Identifier(ThermMod.modid, "textures/glass_thermometer/heating_outline.png");
    private static final Identifier HEATING_OUTLINE_SMALL = new Identifier(ThermMod.modid, "textures/glass_thermometer/heating_small_outline.png");

    public static final Identifier TEMPERATURE_EXTREME_OVERLAY = new Identifier(ThermMod.modid, "textures/misc/temp_extreme_1.png");
    public static final Identifier TEMPERATURE_EXTREME_OVERLAY2 = new Identifier(ThermMod.modid, "textures/misc/temp_extreme_2.png");

    @Override
    public void onHudRender(MatrixStack matrix, float tickDelta) {

        TextureManager drawContext = MinecraftClient.getInstance().getTextureManager();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (ThermClient.showGui) {
            if (Objects.equals(ThermMod.config.temperatureDisplayType, "gauge")) {
                int x = 0;
                int y = 0;
                ItemStack offHand = ItemStack.EMPTY;
                MinecraftClient client = MinecraftClient.getInstance();
                if (client != null) {
                    x = (client.getWindow().getScaledWidth() / 2) + ThermMod.config.temperatureXPos;
                    y = client.getWindow().getScaledHeight() + ThermMod.config.temperatureYPos;
                    assert client.player != null;
                    offHand = client.player.getOffHandStack();
                }
                //TODO fix this monstrosity
                float pixelMultiplier = 1.5f;
                float tempFract = ((ThermClient.clientStoredTemperature / 100f) * Math.round(40*pixelMultiplier));
                if (((ThermClient.clientStoredTemperature / 100f) * Math.round(40*pixelMultiplier)) > 59.0f) { tempFract = ((97 / 100f) * Math.round(40*pixelMultiplier)); } else if ((ThermClient.clientStoredTemperature / 100f) < 0) { tempFract = 0f; }



                RenderSystem.setShaderTexture(0, THERMOMETER_GAUGE);
                DrawableHelper.drawTexture(matrix, x - ((44 + 149) - Math.round(2*pixelMultiplier)), y - (Math.round(8 * pixelMultiplier) + Math.round(3*pixelMultiplier) + 1),  0, 0, Math.round(40*pixelMultiplier), Math.round(9*pixelMultiplier), Math.round(40*pixelMultiplier), Math.round(9*pixelMultiplier));

                RenderSystem.setShaderTexture(0,THERMOMETER_HAND);
                DrawableHelper.drawTexture(matrix, x - (int)( ((44 + 149) - Math.round(2*pixelMultiplier)) - tempFract ), y - (Math.round(8 * pixelMultiplier) + Math.round(3*pixelMultiplier) + 1),  0, 0, Math.round(1), Math.round(9*pixelMultiplier), Math.round(1), Math.round(9*pixelMultiplier));

                int frameY = y - (Math.round(13 * pixelMultiplier) + 1);

                RenderSystem.setShaderTexture(0,THERMOMETER_FRAME);
                DrawableHelper.drawTexture(matrix, x - (44 + 149), frameY,  0, 0, Math.round(44*pixelMultiplier), Math.round(13*pixelMultiplier), Math.round(44*pixelMultiplier), Math.round(13*pixelMultiplier));

                if (ThermClient.clientStoredTempDir > 0) {
                    RenderSystem.setShaderTexture(0,THERMOMETER_FLAME);
                    DrawableHelper.drawTexture(matrix, x - (17 + 149), y - (Math.round(22 * pixelMultiplier)),  0, 0, Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier));
                } else if (ThermClient.clientStoredTempDir < 0) {
                    RenderSystem.setShaderTexture(0,THERMOMETER_SNOWFLAKE);
                    DrawableHelper.drawTexture(matrix, x - (17 + 149), y - (Math.round(22 * pixelMultiplier)),  0, 0, Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier));
                } else {
                    RenderSystem.setShaderTexture(0,THERMOMETER_STILL);
                    DrawableHelper.drawTexture(matrix, x - (17 + 149), y - (Math.round(22 * pixelMultiplier)),  0, 0, Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier));
                }

                if (offHand.getItem() == ThermMod.THERMOMETER_ITEM) {
                    RenderSystem.setShaderTexture(0,THERMOMETER_DISPLAY);
                    DrawableHelper.drawTexture(matrix, (x - (x - 16)) + ThermMod.config.thermometerXPos, frameY + ThermMod.config.thermometerYPos,0, 0, Math.round(16*pixelMultiplier), Math.round(13*pixelMultiplier), Math.round(16*pixelMultiplier), Math.round(13*pixelMultiplier));
                    assert client != null;
                    textRenderer.draw(matrix, "§7" + ThermClient.clientStoredTemperature, ((x - (x - 16)) + 6) + ThermMod.config.thermometerXPos, (frameY + 7) + ThermMod.config.thermometerYPos,16777215);
                }
            } else if (Objects.equals(ThermMod.config.temperatureDisplayType, "glass_thermometer")) {

                int tx = 0;
                int ty = 0;
                float pixelMultiplier = 1.5f;
                ItemStack offHand = ItemStack.EMPTY;
                MinecraftClient client = MinecraftClient.getInstance();
                int x = 0;
                int y = 0;
                int width = 0;
                int height = 0;
                boolean creative = false;
                if (client != null) {
                    tx = (client.getWindow().getScaledWidth() / 2) + ThermMod.config.temperatureXPos;
                    ty = client.getWindow().getScaledHeight() + ThermMod.config.temperatureYPos;

                    x = (client.getWindow().getScaledWidth() / 2) + ThermMod.config.temperatureXPos;
                    y = (client.getWindow().getScaledHeight() - 48) + ThermMod.config.temperatureYPos;

                    width = client.getWindow().getScaledWidth();
                    height = client.getWindow().getScaledHeight();

                    assert client.player != null;
                    offHand = client.player.getOffHandStack();
                }
                int tFrameY = ty - (Math.round(13 * pixelMultiplier) + 1);
                int temp = (int) ThermClient.clientStoredTemperature;

                assert client != null;
                if (!client.player.isSpectator() && !client.player.isCreative()) {
                    if (temp < ThermMod.config.freezeThreshold1 + 1 && temp > ThermMod.config.freezeThreshold2) {
                        ThermClient.glassShakeTickMax = 4;
                        ThermClient.glassShakeAxis = true;
                    } else if (temp < ThermMod.config.freezeThreshold2 + 1) {
                        ThermClient.glassShakeTickMax = 3;
                        ThermClient.glassShakeAxis = true;
                    } else if (temp > ThermMod.config.burnThreshold1 - 1 && temp < ThermMod.config.burnThreshold2) {
                        ThermClient.glassShakeTickMax = 4;
                        ThermClient.glassShakeAxis = false;
                    } else if (temp > ThermMod.config.burnThreshold2 - 1) {
                        ThermClient.glassShakeTickMax = 3;
                        ThermClient.glassShakeAxis = false;
                    } else {
                        ThermClient.glassShakeTickMax = 0;
                    }
                    if (ThermClient.glassShakeTickMax != 0) {
                        ThermClient.glassShakeTick += 1;
                        if (ThermClient.glassShakeTick >= ThermClient.glassShakeTickMax) {
                            ThermClient.glassShakeTick = 0;
                            if (ThermClient.glassShakePM == 1) {
                                ThermClient.glassShakePM = -1;
                            } else if (ThermClient.glassShakePM == -1) {
                                ThermClient.glassShakePM = 1;
                            }
                        }
                        if (ThermClient.glassShakeAxis) {
                            x += ThermClient.glassShakePM;
                        } else {
                            y += ThermClient.glassShakePM;
                        }
                    }

                    if (temp < ThermMod.config.burnThreshold1 - 10 && temp > ThermMod.config.freezeThreshold1 + 10) {
                        RenderSystem.setShaderTexture(0,TEMPERATE_GLASS);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (temp < ThermMod.config.freezeThreshold1 + 11 && temp > ThermMod.config.freezeThreshold1 + 5) {
                        RenderSystem.setShaderTexture(0,COLD_GLASS);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (temp < ThermMod.config.freezeThreshold1 + 6) {
                        RenderSystem.setShaderTexture(0,FROZEN_GLASS);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (temp > ThermMod.config.burnThreshold1 - 11 && temp < ThermMod.config.burnThreshold1 - 5) {
                        RenderSystem.setShaderTexture(0,HOT_GLASS);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (temp > ThermMod.config.burnThreshold1 - 6) {
                        RenderSystem.setShaderTexture(0,BLAZING_GLASS);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    }

                    if (ThermClient.clientStoredTempDir < 0 && ThermClient.clientStoredTempDir > -10) {
                        RenderSystem.setShaderTexture(0,COOLING_OUTLINE_SMALL);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (ThermClient.clientStoredTempDir < -9) {
                        RenderSystem.setShaderTexture(0,COOLING_OUTLINE);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (ThermClient.clientStoredTempDir > 0 && ThermClient.clientStoredTempDir < 10) {
                        RenderSystem.setShaderTexture(0,HEATING_OUTLINE_SMALL);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    } else if (ThermClient.clientStoredTempDir > 9) {
                        RenderSystem.setShaderTexture(0,HEATING_OUTLINE);
                        DrawableHelper.drawTexture(matrix, x - (8), y - (10), 0, 0, 16, 21, 16, 21);
                    }

                }

                if (offHand.isOf(ThermMod.THERMOMETER_ITEM)) {
                    RenderSystem.setShaderTexture(0,THERMOMETER_DISPLAY);
                    DrawableHelper.drawTexture(matrix, (tx - (tx - 16)) + ThermMod.config.thermometerXPos, tFrameY + ThermMod.config.thermometerYPos,0, 0, Math.round(16*pixelMultiplier), Math.round(13*pixelMultiplier), Math.round(16*pixelMultiplier), Math.round(13*pixelMultiplier));
                    assert client != null;
                    textRenderer.draw(matrix, "§7" + ThermClient.clientStoredTemperature, ((tx - (tx - 16)) + 6) + ThermMod.config.thermometerXPos, (tFrameY + 7) + ThermMod.config.thermometerYPos,16777215);
                }

            }

        }
    }

    private void renderOverlay(MatrixStack matrix, Identifier texture, float opacity, int width, int height, float r, float g, float b) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(r, g, b, opacity);
        RenderSystem.setShaderTexture(0,texture);
        DrawableHelper.drawTexture(matrix, 0, 0, -90, 0.0f, 0.0f, width, height, width, height);
        RenderSystem.depthMask(false);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
    }
}
