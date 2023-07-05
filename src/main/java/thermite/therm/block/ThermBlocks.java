package thermite.therm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class ThermBlocks {

    public static final IceBoxEmptyBlock ICE_BOX_EMPTY_BLOCK = new IceBoxEmptyBlock(FabricBlockSettings.create().strength(1.0f));
    public static final IceBoxFreezingBlock ICE_BOX_FREEZING_BLOCK = new IceBoxFreezingBlock(FabricBlockSettings.create().strength(1.0f).ticksRandomly());
    public static final IceBoxFrozenBlock ICE_BOX_FROZEN_BLOCK = new IceBoxFrozenBlock(FabricBlockSettings.create().strength(2.0f).ticksRandomly());

}
