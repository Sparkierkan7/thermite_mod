package thermite.therm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;

public class ThermBlocks {

    public static final IceBoxEmptyBlock ICE_BOX_EMPTY_BLOCK = new IceBoxEmptyBlock(AbstractBlock.Settings.of(Material.WOOD).strength(1.0f));
    public static final IceBoxFreezingBlock ICE_BOX_FREEZING_BLOCK = new IceBoxFreezingBlock(AbstractBlock.Settings.of(Material.ICE).strength(1.0f).ticksRandomly());
    public static final IceBoxFrozenBlock ICE_BOX_FROZEN_BLOCK = new IceBoxFrozenBlock(AbstractBlock.Settings.of(Material.ICE).strength(2.0f).ticksRandomly());

    public static final FireplaceBlock FIREPLACE_BLOCK = new FireplaceBlock(AbstractBlock.Settings.of(Material.WOOD).requiresTool().strength(3.5f).luminance(state -> state.get(FireplaceBlock.LIT) != false ? 15 : 0));
    public static final SmokeBlock SMOKE_BLOCK = new SmokeBlock(AbstractBlock.Settings.of(Material.AIR).air());

}
