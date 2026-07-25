package com.silent.createwingsplus.block;

import com.silent.createwingsplus.block.entity.SailShaftAngledEntity;
import com.silent.createwingsplus.block.entity.SymetricalSailShaftAngledEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractAngleShaftBlock extends DirectionalAxisKineticBlock implements IBE<SailShaftAngledEntity> {
    public AbstractAngleShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis axis = context.getHorizontalDirection().getAxis();
        Direction direction = Direction.getFacingAxis(context.getPlayer(), axis);
        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, direction);
    }

    public static boolean hasShaftTowards(BlockState state, Direction face) {
        return Arrays.asList(getDirectionsConnectedByState(state)).contains(face);
    }

    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return hasShaftTowards(state, face);
    }

    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return this.transform(originalState, new StructureTransform(new BlockPos(0, 0, 0), targetedFace.getAxis(), Rotation.CLOCKWISE_90, Mirror.NONE));
    }

    public static Direction[] getDirectionsConnectedByState(BlockState state) {
        Direction facing = (Direction)state.getValue(DirectionalKineticBlock.FACING);
        Direction secondDirection = Direction.NORTH;
        switch (facing) {
            case NORTH -> secondDirection = Direction.EAST;
            case EAST -> secondDirection = Direction.SOUTH;
            case SOUTH -> secondDirection = Direction.WEST;
            case WEST -> secondDirection = Direction.NORTH;
            case UP -> secondDirection = Direction.NORTH;
            case DOWN -> secondDirection = Direction.NORTH;
        }

        return new Direction[]{facing.getOpposite(), secondDirection};
    }

    public BlockState getBlockstateConnectingDirections(Direction direction1, Direction direction2) {
        boolean axisAlongFirst = direction1.getAxisDirection() == direction2.getAxisDirection();
        return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)DirectionalKineticBlock.FACING, (Comparable)direction1.getOpposite())).setValue((Property)DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, (Comparable)Boolean.valueOf(axisAlongFirst));
    }

    public static boolean isPositiveDirection(Direction direction) {
        return Direction.get(Direction.AxisDirection.POSITIVE, direction.getAxis()) == direction;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        Direction[] directions = getDirectionsConnectedByState(state);
        return this.getBlockstateConnectingDirections(rot.rotate(directions[0]), rot.rotate(directions[1]));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction[] directions = getDirectionsConnectedByState(state);
        return this.getBlockstateConnectingDirections(mirror.getRotation(directions[0]).rotate(directions[0]), mirror.getRotation(directions[1]).rotate(directions[1]));
    }

    public BlockState transform(BlockState state, StructureTransform transform) {
        Direction[] directions = getDirectionsConnectedByState(state);
        return this.getBlockstateConnectingDirections(transform.mirrorFacing(transform.rotateFacing(directions[0])), transform.mirrorFacing(transform.rotateFacing(directions[1])));
    }

    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return false;
    }

    public Class<SailShaftAngledEntity> getBlockEntityClass() {
        return SailShaftAngledEntity.class;
    }
}