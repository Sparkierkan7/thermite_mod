package thermite.therm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class SmokeBlock extends Block{

    public SmokeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {

        world.addParticle(ParticleTypes.HAPPY_VILLAGER, (double)pos.getX() + 0.5,(double)pos.getY(),(double)pos.getZ() + 0.5, 0.0, 0.0, 0.0);

        super.randomDisplayTick(state, world, pos, random);
    }
}