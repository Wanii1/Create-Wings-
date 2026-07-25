package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.IDirectionalAnalogOutput;
import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SailTorsionSpringEntity;
import com.silent.createwingsplus.util.ExtraKinetics;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class SailTorsionSpring extends DirectionalKineticBlock implements IBE<SailTorsionSpringEntity> {
    private static final VoxelShape SHAPE = Block.box(0.0,5.0,0.0,16.0,10.0,16.0);

    public SailTorsionSpring(final Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public boolean hasShaftTowards(final LevelReader world, final BlockPos pos, final BlockState state, final Direction face) {
        return face.getOpposite() == state.getValue(FACING);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        Direction.Axis axis = context.getHorizontalDirection().getAxis();
        Direction direction = Direction.getFacingAxis(context.getPlayer(), axis);
        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, direction);
    }

    @Override
    protected VoxelShape getShape(final BlockState blockState, final BlockGetter blockGetter, final BlockPos blockPos, final CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public Direction.Axis getRotationAxis(final BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<SailTorsionSpringEntity> getBlockEntityClass() {
        return SailTorsionSpringEntity.class;
    }

    @Override
    public BlockEntityType<? extends SailTorsionSpringEntity> getBlockEntityType() {
        return ModBlockEntities.SAIL_TORSION_SPRING_BE.get();
    }
}