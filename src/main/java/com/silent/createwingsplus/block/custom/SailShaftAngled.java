package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SailShaftAngledEntity;
import com.silent.createwingsplus.block.entity.SailShaftStraightEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class SailShaftAngled extends DirectionalKineticBlock implements IBE<SailShaftAngledEntity>, BlockSubLevelLiftProvider {

    private static final VoxelShape SHAPE = Block.box(0.0,5.0,0.0,16.0,10.0,16.0);
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public SailShaftAngled(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Axis axis = context.getHorizontalDirection().getAxis();
        Direction direction = Direction.getFacingAxis(context.getPlayer(), axis);
        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, direction);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() != state.getValue(AXIS);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        if (state.getValue(BlockStateProperties.FACING) == Direction.EAST || state.getValue(BlockStateProperties.FACING) == Direction.WEST){
            return Axis.X;
        }
        else if (state.getValue(BlockStateProperties.FACING) == Direction.NORTH || state.getValue(BlockStateProperties.FACING) == Direction.SOUTH) {
            return Axis.Z;
        }
        else {
            return Axis.Y;
        }
    }

    @Override
    public Class<SailShaftAngledEntity> getBlockEntityClass() {
        return SailShaftAngledEntity.class;
    }

    @Override
    public BlockEntityType<? extends SailShaftAngledEntity> getBlockEntityType() {
        return ModBlockEntities.SAIL_SHAFT_ANGLED_BE.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState state) {
        return Direction.DOWN;
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