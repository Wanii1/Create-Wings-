package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SymetricalSailShaftStraightEntity;
import com.silent.createwingsplus.block.entity.VerticalSymetricalSailShaftStraightEntity;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.createmod.catnip.math.VoxelShaper;
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

import static com.silent.createwingsplus.block.entity.VerticalSymetricalSailShaftAngledEntity.FACING;


public class VerticalSymetricalSailShaftStraight extends KineticBlock implements IBE<VerticalSymetricalSailShaftStraightEntity>, BlockSubLevelLiftProvider {

    private static final VoxelShape box = Block.box(6, 0.0, 0.0, 10, 16.0, 16.0);
    private static final VoxelShaper SHAPE = VoxelShaper.forDirectional(box, Direction.NORTH);
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public VerticalSymetricalSailShaftStraight(Properties properties) {
        super(properties);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Axis axis = context.getNearestLookingDirection().getAxis();
        Axis vertical = context.getNearestLookingDirection().getAxis();
        Axis horizontal = context.getHorizontalDirection().getAxis();
        Direction finalDirection = Direction.getFacingAxis(context.getPlayer(), axis);

        if (vertical == Axis.Y){
            if (horizontal == Axis.X){
                finalDirection = Direction.DOWN;
            }
            else {
                finalDirection = Direction.UP;
            }
        }

        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, finalDirection);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
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

    public Class<VerticalSymetricalSailShaftStraightEntity> getBlockEntityClass() {
        return VerticalSymetricalSailShaftStraightEntity.class;
    }

    public BlockEntityType<? extends VerticalSymetricalSailShaftStraightEntity> getBlockEntityType() {
        return ModBlockEntities.VERTICAL_SYMETRICAL_SAIL_SHAFT_STRAIGHT_BE.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if (state.getValue(BlockStateProperties.FACING) == Direction.UP) {
            return SHAPE.get(Direction.NORTH);
        }
        else if (state.getValue(BlockStateProperties.FACING) == Direction.DOWN){
            return SHAPE.get(Direction.WEST);
        }

        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState blockState) {
        return Direction.get(Direction.AxisDirection.POSITIVE, blockState.getValue(AXIS));
    }

    @Override
    public float sable$getParallelDragScalar() {
        return 1.75f;
    }

    @Override
    public float sable$getLiftScalar() {
        return 0;
    }
}