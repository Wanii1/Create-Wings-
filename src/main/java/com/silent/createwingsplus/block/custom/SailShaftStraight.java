package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SailShaftStraightEntity;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class SailShaftStraight extends KineticBlock implements IBE<SailShaftStraightEntity> {

    public SailShaftStraight(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        // onBlockAdded is useless for init, as sometimes the BE gets re-instantiated

        // however, if a block change occurs that does not change kinetic connections,
        // we can prevent a major re-propagation here

        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof KineticBlockEntity kineticBlockEntity) {
            kineticBlockEntity.preventSpeedUpdate = 0;

            if (oldState.getBlock() != state.getBlock())
                return;
            if (state.hasBlockEntity() != oldState.hasBlockEntity())
                return;
            if (!areStatesKineticallyEquivalent(oldState, state))
                return;

            kineticBlockEntity.preventSpeedUpdate = 2;
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        IBE.onRemove(pState, pLevel, pPos, pNewState);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return getRotationAxis(state) == Axis.Y;
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return getRotationAxis(state);
    }

    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        if (oldState.getBlock() != newState.getBlock())
            return false;
        return getRotationAxis(newState) == getRotationAxis(oldState);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos, int flags,
                                              int count) {
        if (worldIn.isClientSide())
            return;

        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (!(blockEntity instanceof KineticBlockEntity kbe))
            return;

        if (kbe.preventSpeedUpdate > 0)
            return;

        // Remove previous information when block is added
        kbe.warnOfMovement();
        kbe.clearKineticInformation();
        kbe.updateSpeed = true;
    }

    public float getParticleTargetRadius() {
        return .65f;
    }

    public float getParticleInitialRadius() {
        return .75f;
    }

    @Override
    public Class<SailShaftStraightEntity> getBlockEntityClass() {
        return SailShaftStraightEntity.class;
    }

    @Override
    public BlockEntityType<? extends SailShaftStraightEntity> getBlockEntityType() {
        return ModBlockEntities.SAIL_SHAFT_STRAIGHT_BE.get();
    }
}
