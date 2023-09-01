package thermite.therm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import thermite.therm.ServerState;
import thermite.therm.ThermMod;
import thermite.therm.ThermPlayerState;
import thermite.therm.ThermUtil;
import thermite.therm.block.FireplaceBlock;
import thermite.therm.block.ThermBlocks;
import thermite.therm.networking.ThermNetworkingPackets;
import thermite.therm.util.BlockStatePosPair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static thermite.therm.ThermMod.LOGGER;

public class PlayerTempTickC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        ServerState serverState = ServerState.getServerState(server);
        ThermPlayerState playerState = ServerState.getPlayerState(player);

        //playerState.testplayerint -= 1;
        //serverState.markDirty();
        //ThermMod.LOGGER.info("value: " + playerState.testplayerint);

        float temp = player.getWorld().getBiome(player.getBlockPos()).value().getTemperature();
        String climate = ThermUtil.getClimate(temp);
        long time = (player.getWorld().getTimeOfDay()) - (0);

        float nightRTemp = 0;

        if (Objects.equals(climate, "frigid")) {
            playerState.minTemp = 0;
            playerState.maxTemp = 80;
            playerState.restingTemp = ThermMod.config.frigidClimateTemp;
            nightRTemp = -10;
        } else if (Objects.equals(climate, "cold")) {
            playerState.minTemp = 0;
            playerState.maxTemp = 100;
            playerState.restingTemp = ThermMod.config.coldClimateTemp;
            nightRTemp = -10;
        } else if (Objects.equals(climate, "temperate")) {
            playerState.minTemp = 0;
            playerState.maxTemp = 100;
            playerState.restingTemp = ThermMod.config.temperateClimateTemp;
            nightRTemp = -10;
        } else if (Objects.equals(climate, "hot")) {
            playerState.minTemp = 40;
            playerState.maxTemp = 120;
            playerState.restingTemp = ThermMod.config.hotClimateTemp;
            nightRTemp = -8;
        } else if (Objects.equals(climate, "arid")) {
            playerState.minTemp = 40;
            playerState.maxTemp = 120;
            playerState.restingTemp = ThermMod.config.aridClimateTemp;
            nightRTemp = -15;
        }

        if (ThermMod.config.enableSeasonSystem) {
            int season = serverState.season;
            if (season == 1) {
                playerState.restingTemp += (8 * ThermMod.config.seasonTemperatureExtremenessMultiplier);
            } else if (season == 2) {
                playerState.restingTemp -= (8 * ThermMod.config.seasonTemperatureExtremenessMultiplier);
            } else if (season == 3) {
                playerState.restingTemp -= (12 * ThermMod.config.seasonTemperatureExtremenessMultiplier);
            }
        }

        DimensionType dim = player.getWorld().getDimension();

        if (dim.natural()) {
            if (!player.getWorld().isDay()) { //its nighttime in overworld so make colder
                playerState.restingTemp += nightRTemp;
            }
        }

        Biome.Precipitation precip = player.getWorld().getBiome(player.getBlockPos()).value().getPrecipitation(player.getBlockPos());

        //detect if raining or snowing and adjust resting temp
        if (precip == Biome.Precipitation.RAIN) {
            if (player.getWorld().isRaining()) {
                if (player.isWet() && !player.isTouchingWater()) {
                    playerState.restingTemp -= 8;
                }
            }
        } else if (precip == Biome.Precipitation.SNOW) {
            if (player.getWorld().isRaining()) {
                playerState.restingTemp -= 12;
            }
        }

        Vec3d pos = player.getPos();
        Stream<BlockState> heatBlockBox = player.getWorld().getStatesInBox(Box.of(pos, 4, 4, 4));
        heatBlockBox.forEach((state) -> {
            ThermMod.config.heatingBlocks.forEach((b, t) -> {
                if (Objects.equals(state.getBlock().toString(), b)) {
                    playerState.restingTemp += t;
                }
            });
        });
        Stream<BlockState> coldBlockBox = player.getWorld().getStatesInBox(Box.of(pos, 2, 3, 2));
        coldBlockBox.forEach((state) -> {
            ThermMod.config.coolingBlocks.forEach((b, t) -> {
                if (Objects.equals(state.getBlock().toString(), b)) {
                    playerState.restingTemp -= t;
                }
            });
        });

        if (playerState.searchFireplaceTick <= 0) {
            playerState.searchFireplaceTick = 4;
            AtomicInteger fireplaces = new AtomicInteger();
            Stream<BlockState> fireplaceBox = player.getWorld().getStatesInBox(Box.of(pos, 12, 12, 12));
            fireplaceBox.forEach((state) -> {
                if (state.isOf(ThermBlocks.FIREPLACE_BLOCK)) {
                    if (state.get(FireplaceBlock.LIT)) {
                        fireplaces.addAndGet(1);
                    }
                }
            });
            playerState.fireplaces = fireplaces.get();
        }
        playerState.restingTemp += (playerState.fireplaces * 14);
        playerState.searchFireplaceTick -= 1;

        if (player.isTouchingWater()) {
            playerState.restingTemp -= 10;
        }

        //armor items
        ThermMod.config.bootTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(0).getItem().toString(), it)) {
                playerState.restingTemp += t;
            }
        });
        ThermMod.config.leggingTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(1).getItem().toString(), it)) {
                playerState.restingTemp += t;
            }
        });
        ThermMod.config.chestplateTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(2).getItem().toString(), it)) {
                playerState.restingTemp += t;
            }
        });
        ThermMod.config.helmetTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(3).getItem().toString(), it)) {
                playerState.restingTemp += t;
            }
        });
        ThermMod.config.heldTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getMainHandStack().getItem().toString(), it)) {
                playerState.restingTemp += t;
            }
            if (Objects.equals(player.getInventory().offHand.get(0).getItem().toString(), it)) {
                playerState.restingTemp += t;
            }
        });

        //fire protection
        int fireProt = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < player.getInventory().getArmorStack(i).getEnchantments().size(); j++) {
                if (Objects.equals(player.getInventory().getArmorStack(i).getEnchantments().getCompound(j).getString("id"), "minecraft:fire_protection")) {
                    fireProt += player.getInventory().getArmorStack(i).getEnchantments().getCompound(j).getInt("lvl");
                }
            }
        }
        playerState.restingTemp -= (fireProt * ThermMod.config.fireProtectionCoolingMultiplier);

        short tempDir = (short)(playerState.restingTemp - playerState.temp);

        if (Math.round(playerState.restingTemp) > Math.round(playerState.temp)) { //TODO fix temprate
            playerState.temp += 0.25;
        } else if (Math.round(playerState.restingTemp) < Math.round(playerState.temp)) {
            playerState.temp -= 0.25;
        } else if (Math.round(playerState.restingTemp) == Math.round(playerState.temp)) {}

        if (playerState.temp <= ThermMod.config.freezeThreshold1 && playerState.temp > ThermMod.config.freezeThreshold2) {
            playerState.damageType = "freeze";
            playerState.maxDamageTick = ThermMod.config.temperatureDamageInterval;
        } else if (playerState.temp <= ThermMod.config.freezeThreshold2) {
            playerState.damageType = "freeze";
            playerState.maxDamageTick = ThermMod.config.extremetemperatureDamageInterval;
        } else if (playerState.temp >= ThermMod.config.burnThreshold1 && playerState.temp < ThermMod.config.burnThreshold2) {
            playerState.damageType = "burn";
            playerState.maxDamageTick = ThermMod.config.temperatureDamageInterval;
        } else if (playerState.temp >= ThermMod.config.burnThreshold2) {
            playerState.damageType = "burn";
            playerState.maxDamageTick = ThermMod.config.extremetemperatureDamageInterval;
        } else {
            playerState.damageTick = 0;
            playerState.damageType = "";
        }

        if (Objects.equals(playerState.damageType, "freeze")) {
            if (ThermMod.config.temperatureDamageDecreasesSaturation) {player.getHungerManager().setSaturationLevel(0f);}
            if (playerState.damageTick < playerState.maxDamageTick) {
                playerState.damageTick += 1;
            }
            if (playerState.damageTick >= playerState.maxDamageTick) {
                playerState.damageTick = 0;
                player.damage(player.getWorld().getDamageSources().freeze(), ThermMod.config.hypothermiaDamage);
            }
        } else if (Objects.equals(playerState.damageType, "burn")) {
            if (ThermMod.config.temperatureDamageDecreasesSaturation) {player.getHungerManager().setSaturationLevel(0f);}
            boolean res = false;
            try {
                String fireRes = Objects.requireNonNull(player.getStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE).getEffectType())).getEffectType().getName().getString();
                res = true;
            } catch (NullPointerException err) {res = false;}
            if (!res) {
                if (playerState.damageTick < playerState.maxDamageTick) {
                    playerState.damageTick += 1;
                }
                if (playerState.damageTick >= playerState.maxDamageTick) {
                    playerState.damageTick = 0;
                    player.damage(player.getWorld().getDamageSources().onFire(), ThermMod.config.hyperthermiaDamage);
                    player.getHungerManager().setSaturationLevel(player.getHungerManager().getSaturationLevel() - 6);
                }
            }
        }

        if (player.getHealth() <= 0.0) {
            playerState.temp = 50;
            playerState.damageTick = 0;
        }

        PacketByteBuf sendingdata = PacketByteBufs.create();
        sendingdata.writeDouble(playerState.temp);
        sendingdata.writeShort(tempDir);
        ServerPlayNetworking.send(player, ThermNetworkingPackets.SEND_THERMPLAYERSTATE_S2C_PACKET_ID, sendingdata);

        serverState.markDirty();

        //ThermMod.LOGGER.info("temp: " + playerState.temp);
        //ThermMod.LOGGER.info("p: " + player.getWorld().getBiome(player.getBlockPos()).value().getPrecipitation(player.getBlockPos()) + " rain?: " + player.getWorld().isRaining());
    }

}
