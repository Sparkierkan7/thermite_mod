package thermite.therm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import thermite.therm.ThermClient;
import thermite.therm.ThermMod;

import java.util.function.Supplier;

public class TemperatureHudOverlay implements HudRenderCallback {

    private static final Identifier THERMOMETER_FRAME = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_frame.png");
    private static final Identifier THERMOMETER_GAUGE = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_gauge_fix_1.png");
    private static final Identifier THERMOMETER_HAND = new Identifier(ThermMod.modid, "textures/thermometer/thermometer_hand.png");
    private static final Identifier THERMOMETER_SNOWFLAKE = new Identifier(ThermMod.modid, "textures/thermometer/snowflake_icon_8x8.png");
    private static final Identifier THERMOMETER_FLAME = new Identifier(ThermMod.modid, "textures/thermometer/flame_icon_8x8.png");
    private static final Identifier THERMOMETER_STILL = new Identifier(ThermMod.modid, "textures/thermometer/temperate_icon.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            x = client.getWindow().getScaledWidth() / 2;
            y = client.getWindow().getScaledHeight();
        }

        float pixelMultiplier = 1.5f;
        float tempFract = ((ThermClient.clientStoredTemperature / 100f) * Math.round(40*pixelMultiplier));

        drawContext.drawTexture(THERMOMETER_GAUGE, x - ((44 + 149) - Math.round(2*pixelMultiplier)), y - (Math.round(8 * pixelMultiplier) + Math.round(3*pixelMultiplier) + 1),  0, 0, Math.round(40*pixelMultiplier), Math.round(9*pixelMultiplier), Math.round(40*pixelMultiplier), Math.round(9*pixelMultiplier));
        drawContext.drawTexture(THERMOMETER_HAND, x - (int)( ((44 + 149) - Math.round(2*pixelMultiplier)) - tempFract ), y - (Math.round(8 * pixelMultiplier) + Math.round(3*pixelMultiplier) + 1),  0, 0, Math.round(1), Math.round(9*pixelMultiplier), Math.round(1), Math.round(9*pixelMultiplier));
        drawContext.drawTexture(THERMOMETER_FRAME, x - (44 + 149), y - (Math.round(13 * pixelMultiplier) + 1),  0, 0, Math.round(44*pixelMultiplier), Math.round(13*pixelMultiplier), Math.round(44*pixelMultiplier), Math.round(13*pixelMultiplier));

        if (ThermClient.clientStoredTempDir == 1) {
            drawContext.drawTexture(THERMOMETER_FLAME, x - (17 + 149), y - (Math.round(22 * pixelMultiplier)),  0, 0, Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier));
        } else if (ThermClient.clientStoredTempDir == -1) {
            drawContext.drawTexture(THERMOMETER_SNOWFLAKE, x - (17 + 149), y - (Math.round(22 * pixelMultiplier)),  0, 0, Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier));
        } else if (ThermClient.clientStoredTempDir == 0) {
            drawContext.drawTexture(THERMOMETER_STILL, x - (17 + 149), y - (Math.round(22 * pixelMultiplier)),  0, 0, Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier), Math.round(8*pixelMultiplier));
        }

        //RenderSystem.setShader(new Supplier<ShaderProgram>());

    }

}
