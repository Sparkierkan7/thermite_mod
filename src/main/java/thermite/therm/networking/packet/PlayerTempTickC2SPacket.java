package thermite.therm.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import thermite.therm.ServerState;
import thermite.therm.ThermPlayerState;
import thermite.therm.ThermUtil;
import thermite.therm.block.ThermBlocks;
import thermite.therm.networking.ThermNetworkingPackets;
import thermite.therm.util.BlockStatePosPair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
            playerState.restingTemp = 25;
            nightRTemp = -10;
        } else if (Objects.equals(climate, "cold")) {
            playerState.minTemp = 0;
            playerState.maxTemp = 100;
            playerState.restingTemp = 30;
            nightRTemp = -10;
        } else if (Objects.equals(climate, "temperate")) {
            playerState.minTemp = 0;
            playerState.maxTemp = 100;
            playerState.restingTemp = 50;
            nightRTemp = -10;
        } else if (Objects.equals(climate, "hot")) {
            playerState.minTemp = 40;
            playerState.maxTemp = 120;
            playerState.restingTemp = 55;
            nightRTemp = -8;
        } else if (Objects.equals(climate, "arid")) {
            playerState.minTemp = 40;
            playerState.maxTemp = 120;
            playerState.restingTemp = 70;
            nightRTemp = -15;
        }

        DimensionType dim = player.getWorld().getDimension();

        if (dim.natural()) {
            //time >= 13313 && time <= 23013
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
                playerState.restingTemp -= 18;
            }
        }

        Vec3d pos = player.getPos();
        int torches = 0;
        int lava = 0;
        int campfires = 0;
        int fires = 0;
        ArrayList<BlockStatePosPair> box = ThermUtil.getBlockBox(player.getWorld(), (int)(pos.x - 3), (int)(pos.y - 3), (int)(pos.z - 3), (int)(pos.x + 3), (int)(pos.y + 3), (int)(pos.z + 3));
        for (int i = 0; i < box.size(); i++) {
            if (Objects.equals(box.get(i).blockState.getBlock().getName(), Blocks.TORCH.getName())) {
                torches += 1;
            } else if (Objects.equals(box.get(i).blockState.getBlock().getName(), Blocks.LAVA.getName())) {
                lava += 1;
            } else if (Objects.equals(box.get(i).blockState.getBlock().getName(), Blocks.CAMPFIRE.getName()) || Objects.equals(box.get(i).blockState.getBlock().getName(), Blocks.SOUL_CAMPFIRE.getName())) {
                campfires += 1;
            } else if (Objects.equals(box.get(i).blockState.getBlock().getName(), Blocks.FIRE.getName()) || Objects.equals(box.get(i).blockState.getBlock().getName(), Blocks.SOUL_FIRE.getName())) {
                fires += 1;
            }
        }
        playerState.restingTemp += (torches * 3);
        playerState.restingTemp += (fires * 3);
        playerState.restingTemp += (lava * 8);
        playerState.restingTemp += (campfires * 15);

        AtomicInteger ice = new AtomicInteger();
        AtomicInteger packed_ice = new AtomicInteger();
        AtomicInteger blue_ice = new AtomicInteger();
        Stream<BlockState> coldBox = player.getWorld().getStatesInBox(Box.of(pos, 2, 3, 2));
        coldBox.forEach((state) -> {
            if (Objects.equals(state.getBlock().getName(), Blocks.ICE.getName())) {
                ice.addAndGet(1);
            } else if (Objects.equals(state.getBlock().getName(), Blocks.PACKED_ICE.getName())) {
                packed_ice.addAndGet(1);
            } else if (Objects.equals(state.getBlock().getName(), Blocks.BLUE_ICE.getName())) {
                blue_ice.addAndGet(1);
            } else if (Objects.equals(state.getBlock().getName(), ThermBlocks.ICE_BOX_FROZEN_BLOCK.getName())) {
                packed_ice.addAndGet(1);
            }
        });
        /*for (int j = 0; j < coldBox.size; j++) {
            if (Objects.equals(box.get(j).blockState.getBlock().getName(), Blocks.ICE.getName())) {
                ice += 1;
            } else if (Objects.equals(box.get(j).blockState.getBlock().getName(), Blocks.PACKED_ICE.getName())) {
                packed_ice += 1;
            } else if (Objects.equals(box.get(j).blockState.getBlock().getName(), Blocks.BLUE_ICE.getName())) {
                blue_ice += 1;
            }
        }*/
        playerState.restingTemp -= (ice.get() * 1);
        playerState.restingTemp -= (packed_ice.get() * 3);
        playerState.restingTemp -= (blue_ice.get() * 6);

        if (player.isTouchingWater()) {
            playerState.restingTemp -= 10;
        }

        if (player.getInventory().getArmorStack(0).getItem() == Items.LEATHER_BOOTS) { //boot
            playerState.restingTemp += 1;
        }
        if (player.getInventory().getArmorStack(1).getItem() == Items.LEATHER_LEGGINGS) { //leggings
            playerState.restingTemp += 2;
        }
        if (player.getInventory().getArmorStack(2).getItem() == Items.LEATHER_CHESTPLATE) { //chestplate
            playerState.restingTemp += 3;
        }
        if (player.getInventory().getArmorStack(3).getItem() == Items.LEATHER_HELMET) { //helmet
            playerState.restingTemp += 1;
        }


        short tempDir = 16;

        if (Math.round(playerState.restingTemp) > Math.round(playerState.temp)) { //TODO fix temprate
            playerState.temp += 0.125;
            tempDir = 1;
        } else if (Math.round(playerState.restingTemp) < Math.round(playerState.temp)) {
            playerState.temp -= 0.125;
            tempDir = -1;
        } else if (Math.round(playerState.restingTemp) == Math.round(playerState.temp)) {tempDir = 0;}

        if (playerState.temp <= 35 && playerState.temp > 25) {
            playerState.damageType = "freeze";
            playerState.maxDamageTick = 6;
        } else if (playerState.temp <= 25) {
            playerState.damageType = "freeze";
            playerState.maxDamageTick = 4;
        } else if (playerState.temp >= 65 && playerState.temp < 75) {
            playerState.damageType = "burn";
            playerState.maxDamageTick = 6;
        } else if (playerState.temp >= 75) {
            playerState.damageType = "burn";
            playerState.maxDamageTick = 4;
        } else {
            playerState.damageTick = 0;
            playerState.damageType = "";
        }

        if (Objects.equals(playerState.damageType, "freeze")) {
            if (playerState.damageTick < playerState.maxDamageTick) {
                playerState.damageTick += 1;
            } else if (playerState.damageTick >= playerState.maxDamageTick) {
                playerState.damageTick = 0;
                player.damage(player.getWorld().getDamageSources().freeze(), 1);
                player.getHungerManager().setSaturationLevel(player.getHungerManager().getSaturationLevel() - 6);
                if (player.getHungerManager().getSaturationLevel() < 0) {
                    player.getHungerManager().setSaturationLevel(0f);
                }
            }
        } else if (Objects.equals(playerState.damageType, "burn")) {

            boolean res = false;

            try {
                String fireRes = Objects.requireNonNull(player.getStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE).getEffectType())).getEffectType().getName().getString();
                res = true;
            } catch (NullPointerException err) {res = false;}

            if (!res) {
                if (playerState.damageTick < playerState.maxDamageTick) {
                    playerState.damageTick += 1;
                } else if (playerState.damageTick >= playerState.maxDamageTick) {
                    playerState.damageTick = 0;
                    player.damage(player.getWorld().getDamageSources().onFire(), 1);
                    player.damage(player.getWorld().getDamageSources().freeze(), 1);
                    player.getHungerManager().setSaturationLevel(player.getHungerManager().getSaturationLevel() - 6);
                    if (player.getHungerManager().getSaturationLevel() < 0) {
                        player.getHungerManager().setSaturationLevel(0f);
                    }
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
        ServerPlayNetworking.send(player, ThermNetworkingPackets.SEND_THERMPLAYERSTATE_C2S_PACKET_ID, sendingdata);

        serverState.markDirty();

        //ThermMod.LOGGER.info("temp: " + playerState.temp);
        //ThermMod.LOGGER.info("p: " + player.getWorld().getBiome(player.getBlockPos()).value().getPrecipitation(player.getBlockPos()) + " rain?: " + player.getWorld().isRaining());
    }

}
