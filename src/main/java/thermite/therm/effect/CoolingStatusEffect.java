package thermite.therm.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import thermite.therm.ServerState;
import thermite.therm.ThermMod;
import thermite.therm.ThermPlayerState;

public class CoolingStatusEffect extends StatusEffect {
    public CoolingStatusEffect() {
        super(
                StatusEffectCategory.NEUTRAL,
                0x6bb0ff);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
        }
    }

}