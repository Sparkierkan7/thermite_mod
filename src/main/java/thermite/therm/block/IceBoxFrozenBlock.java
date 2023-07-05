package thermite.therm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import thermite.therm.ThermMod;
import thermite.therm.ThermUtil;

import java.util.Objects;

public class IceBoxFrozenBlock extends Block {

    public IceBoxFrozenBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);
            world.setBlockState(pos, ThermBlocks.ICE_BOX_EMPTY_BLOCK.getDefaultState());
            IceBoxFrozenBlock.dropStack(world, pos, new ItemStack(Items.ICE, 3));
        }

        return ActionResult.SUCCESS;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float temp = 23;
        String climate = "err";
        if (!world.isClient) {
            temp = world.getBiome(pos).value().getTemperature();
            climate = ThermUtil.getClimate(temp);
            if (Objects.equals(climate, "temperate") || Objects.equals(climate, "hot")) {
                if (random.nextInt(7) == 0) {
                    world.setBlockState(pos, ThermBlocks.ICE_BOX_FREEZING_BLOCK.getDefaultState());
                }
            } else if (Objects.equals(climate, "arid")) {
                if (random.nextInt(5) == 0) {
                    world.setBlockState(pos, ThermBlocks.ICE_BOX_FREEZING_BLOCK.getDefaultState());
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }

}