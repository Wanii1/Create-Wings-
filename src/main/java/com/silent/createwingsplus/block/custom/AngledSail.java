package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.bearing.SailBlock;
import com.simibubi.create.foundation.utility.BlockHelper;
import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.VoxelShaper;
import net.createmod.catnip.placement.IPlacementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AngledSail extends SailBlock implements BlockSubLevelLiftProvider {
    private static final VoxelShape SHAPE1 = Block.box(0.0,5.0,0.0,8.0,10.0,16.0);
    private static final VoxelShape SHAPE2 = Block.box(8.0,5.0,0.0,16.0,10.0,8.0);
    private static final VoxelShape SHAPE = Shapes.join(SHAPE1, SHAPE2, BooleanOp.OR);
    private static final VoxelShaper CSHAPE = VoxelShaper.forDirectional(SHAPE, Direction.NORTH);
    public AngledSail(Properties properties, DyeColor color) {
        super(properties, false, DyeColor.WHITE);
    }

    public static AngledSail withCanvas(Properties properties, DyeColor color) {
        return new AngledSail(properties, color);
    }

    @Override
    public void applyDye(BlockState state, Level world, BlockPos pos, Vec3 hit, @Nullable DyeColor color) {
        BlockState newState =
                (color == null ? AllBlocks.SAIL_FRAME : ModBlocks.DYED_ANGLED_SAILS.get(color)).getDefaultState();
        newState = BlockHelper.copyProperties(state, newState);

        // Dye the block itself
        if (state != newState) {
            world.setBlockAndUpdate(pos, newState);
            return;
        }

        List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, hit, state.getValue(FACING)
                .getAxis());
        for (Direction d : directions) {
            BlockPos offset = pos.relative(d);
            BlockState adjacentState = world.getBlockState(offset);
            Block block = adjacentState.getBlock();
            if (!(block instanceof SailBlock) || ((AngledSail) block).frame)
                continue;
            if (state.getValue(FACING) != adjacentState.getValue(FACING))
                continue;
            if (state == adjacentState)
                continue;
            world.setBlockAndUpdate(offset, newState);
            return;
        }

        // Dye all the things
        List<BlockPos> frontier = new ArrayList<>();
        frontier.add(pos);
        Set<BlockPos> visited = new HashSet<>();
        int timeout = 100;
        while (!frontier.isEmpty()) {
            if (timeout-- < 0)
                break;

            BlockPos currentPos = frontier.remove(0);
            visited.add(currentPos);

            for (Direction d : Iterate.directions) {
                if (d.getAxis() == state.getValue(FACING)
                        .getAxis())
                    continue;
                BlockPos offset = currentPos.relative(d);
                if (visited.contains(offset))
                    continue;
                BlockState adjacentState = world.getBlockState(offset);
                Block block = adjacentState.getBlock();
                if (!(block instanceof SailBlock) || ((AngledSail) block).frame && color != null)
                    continue;
                if (adjacentState.getValue(FACING) != state.getValue(FACING))
                    continue;
                if (state != adjacentState)
                    world.setBlockAndUpdate(offset, newState);
                frontier.add(offset);
                visited.add(offset);
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        DyeColor color = DyeColor.getColor(stack);
        if (color != null) {
            if (!level.isClientSide)
                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0f, 1.1f - level.random.nextFloat() * .2f);
            applyDye(state, level, pos, hitResult.getLocation(), color);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState state) {
        return Direction.DOWN;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if (state.getValue(BlockStateProperties.FACING) == Direction.UP || state.getValue(BlockStateProperties.FACING) == Direction.DOWN) {
            return CSHAPE.get(Direction.NORTH);
        }

        return CSHAPE.get(state.getValue(FACING));
    }

    @Override
    public float sable$getParallelDragScalar() {
        if (AllBlocks.SAIL.get() instanceof BlockSubLevelLiftProvider blockSubLevelLiftProvider){
            return blockSubLevelLiftProvider.sable$getParallelDragScalar() * 0.5f;
        }
        return 0.0f;
    }

    @Override
    public float sable$getLiftScalar() {
        if (AllBlocks.SAIL.get() instanceof BlockSubLevelLiftProvider blockSubLevelLiftProvider){
            return blockSubLevelLiftProvider.sable$getLiftScalar() * 0.5f;
        }
        return 0.0f;
    }
}