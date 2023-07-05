package thermite.therm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class IceBoxEmptyBlock extends Block {

    public IceBoxEmptyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() == Items.POTION) {
                world.setBlockState(pos, ThermBlocks.ICE_BOX_FREEZING_BLOCK.getDefaultState());
                player.getStackInHand(hand).setCount(player.getStackInHand(hand).getCount() - 1);
                player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return ActionResult.SUCCESS;
    }

}