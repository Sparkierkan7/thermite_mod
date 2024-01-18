package thermite.therm.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thermite.therm.ThermClient;
import thermite.therm.ThermMod;
import thermite.therm.client.TemperatureHudOverlay;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract void renderOverlay(Identifier texture, float opacity);

    @Inject(method = "renderVignetteOverlay", at = @At(value = "HEAD"), cancellable = true)
    private void temperatureOverlay(Entity entity, CallbackInfo cir) {

        if (ThermMod.config.enableTemperatureVignette) {
            MinecraftClient client = MinecraftClient.getInstance();
            //assert client.player != null;
            if (!client.player.isCreative() && !client.player.isSpectator()) {
                float r = 0;
                float g = 0;
                float b = 0;
                float a = 0;
                boolean extreme = false;

                if (ThermClient.clientStoredTemperature < 41 && ThermClient.clientStoredTemperature > 35) {
                    r = 0.25f;
                    g = 0.5f;
                    b = 0.8f;
                    a = 0.5f;
                } else if (ThermClient.clientStoredTemperature < 36 && ThermClient.clientStoredTemperature > 25) {
                    r = 0.25f;
                    g = 0.5f;
                    b = 0.8f;
                    a = 1f;
                } else if (ThermClient.clientStoredTemperature < 26) {
                    r = 0.60f;
                    g = 0.75f;
                    b = 1.0f;
                    a = 2.5f;
                    //extreme = true;
                } else if (ThermClient.clientStoredTemperature > 59 && ThermClient.clientStoredTemperature < 65) {
                    r = 0.8f;
                    g = 0.3f;
                    b = 0.15f;
                    a = 0.5f;
                } else if (ThermClient.clientStoredTemperature > 64 && ThermClient.clientStoredTemperature < 75) {
                    r = 0.8f;
                    g = 0.3f;
                    b = 0.15f;
                    a = 1f;
                } else if (ThermClient.clientStoredTemperature > 74) {
                    r = 0.9f;
                    g = 0.4f;
                    b = 0.15f;
                    a = 02.5f;
                }

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
                RenderSystem.setShaderColor(r, g, b, a);
                if (!extreme) {
                    renderOverlay(TemperatureHudOverlay.TEMPERATURE_EXTREME_OVERLAY, 0);
                } else {
                    renderOverlay(TemperatureHudOverlay.TEMPERATURE_EXTREME_OVERLAY2, 0);
                }
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.defaultBlendFunc();
            }
        }
    }
}