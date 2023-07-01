package thermite.therm.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ThermometerItem extends Item {

    public ThermometerItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (hand == Hand.MAIN_HAND && world.isClient) {



        }

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

}
