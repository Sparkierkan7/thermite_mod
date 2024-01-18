package thermite.therm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import thermite.therm.ThermUtil;

import java.util.Objects;
import java.util.Random;

public class IceBoxFreezingBlock extends Block {

    public IceBoxFreezingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float temp = 23;
        String climate = "err";
        if (!world.isClient) {
            temp = world.getBiome(pos).value().getTemperature();
            climate = ThermUtil.getClimate(temp);
            if (Objects.equals(climate, "cold") || Objects.equals(climate, "frigid")) {
                if (random.nextInt(5) == 0) {
                    world.setBlockState(pos, ThermBlocks.ICE_BOX_FROZEN_BLOCK.getDefaultState());
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }
}