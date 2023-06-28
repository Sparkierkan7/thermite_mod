package thermite.therm.item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import thermite.therm.networking.ThermNetworkingPackets;

public class IceJuiceItem extends Item {

    public IceJuiceItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        playerEntity.playSound(SoundEvents.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.0F, 1.0F);

        if (hand == Hand.MAIN_HAND && world.isClient) {

            ClientPlayNetworking.send(ThermNetworkingPackets.DRINK_ICE_JUICE_C2S_PACKET_ID, PacketByteBufs.create());

        } else if (hand == Hand.MAIN_HAND && !world.isClient) {
            playerEntity.getStackInHand(hand).setCount(playerEntity.getStackInHand(hand).getCount() - 1);
            playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
        }

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
