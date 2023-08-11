package thermite.therm.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.datafixer.fix.ChunkPalettedStorageFix;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import thermite.therm.ThermMod;
import thermite.therm.block.entity.FireplaceBlockEntity;

import java.util.Objects;

public class FireplaceBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public FireplaceBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, false).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FireplaceBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL) {
            world.setBlockState(pos, state.with(LIT, true).with(FACING, state.get(FACING)));
            FireplaceBlockEntity blockEntity = (FireplaceBlockEntity) world.getBlockEntity(pos);
            blockEntity.setTime(blockEntity.getTime() + 1200);
            blockEntity.markDirty();
            stack.setCount(stack.getCount() - 1);
            world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.25f, 0.5f);
        } else if (stack.getItem() == Items.COAL_BLOCK) {
            world.setBlockState(pos, state.with(LIT, true).with(FACING, state.get(FACING)));
            FireplaceBlockEntity blockEntity = (FireplaceBlockEntity) world.getBlockEntity(pos);
            blockEntity.setTime(blockEntity.getTime() + 10800);
            blockEntity.markDirty();
            stack.setCount(stack.getCount() - 1);
            world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.25f, 0.5f);
        } else if (stack.getItem() == Items.STICK) {
            world.setBlockState(pos, state.with(LIT, true).with(FACING, state.get(FACING)));
            FireplaceBlockEntity blockEntity = (FireplaceBlockEntity) world.getBlockEntity(pos);
            blockEntity.setTime(blockEntity.getTime() + 100);
            blockEntity.markDirty();
            stack.setCount(stack.getCount() - 1);
            world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 0.25f, 0.5f);
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return FireplaceBlock.checkType(type, ThermMod.FIREPLACE_BLOCK_ENTITY, (world1, pos, state1, be) -> FireplaceBlockEntity.tick(world1, pos, state1, be));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {

        if (state.get(LIT)) {
            for (int i = 0; i < 10; i++) {
                if (!Objects.equals(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1 + i, pos.getZ())).getBlock().getName(), Blocks.BRICK_WALL.getName()) && !Objects.equals(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1 + i, pos.getZ())).getBlock().getName(), Blocks.COBBLESTONE_WALL.getName()) && !Objects.equals(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1 + i, pos.getZ())).getBlock().getName(), Blocks.STONE_BRICK_WALL.getName())) {
                    if (i == 0) {
                        world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX() + 0.5,(double)pos.getY() + 1,(double)pos.getZ() + 0.5, 0.0, 0.03125, 0.0);
                        break;
                    } else if (i != 0) {
                        world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX() + 0.5,(double)pos.getY() + 1 + i,(double)pos.getZ() + 0.5, 0.0, 0.03125, 0.0);
                        break;
                    }
                }
            }
            if (random.nextInt(10) == 0) {
                world.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
            }
        }

        super.randomDisplayTick(state, world, pos, random);
    }
}