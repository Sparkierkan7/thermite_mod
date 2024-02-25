package thermite.therm.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import thermite.therm.ThermMod;
import thermite.therm.block.FireplaceBlock;
import thermite.therm.block.ThermBlocks;

public class FireplaceBlockEntity extends BlockEntity {

    private int number = 7;
    private int time = 0;

    public FireplaceBlockEntity(BlockPos pos, BlockState state) {
        super(ThermMod.FIREPLACE_BLOCK_ENTITY, pos, state);
    }


    @Override
    public void writeNbt(NbtCompound nbt) {
        // Save the current value of the number to the nbt
        nbt.putInt("number", number);
        nbt.putInt("time", time);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        number = nbt.getInt("number");
        time = nbt.getInt("time");
    }

    public void setTime(int value) {
        time = value;
    }

    public int getTime() {
        return time;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    //tick
    public static void tick(World world, BlockPos pos, BlockState state, FireplaceBlockEntity be) {

        if (world.getBlockState(pos).isOf(ThermBlocks.FIREPLACE_BLOCK)) {
            if (world.getBlockState(pos).get(FireplaceBlock.LIT)) {
                if (be.time > 0) {be.time -= 1;}
                if (be.time <= 0) {
                    world.setBlockState(pos, ThermBlocks.FIREPLACE_BLOCK.getDefaultState().with(FireplaceBlock.LIT, false).with(FireplaceBlock.FACING, state.get(FireplaceBlock.FACING)));
                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                }
                be.markDirty();
            }
        }

    }

}