package com.silent.createwingsplus.block.entity;

import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticEffectHandler;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VerticalSymetricalSailShaftAngledEntity extends DirectionalShaftHalvesBlockEntity {
    public @Nullable Long network;
    public @Nullable BlockPos source;
    public boolean updateSpeed;

    protected KineticEffectHandler effects;

    public SequencedGearshiftBlockEntity.SequenceContext sequenceContext;

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public VerticalSymetricalSailShaftAngledEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VERTICAL_SYMETRICAL_SAIL_SHAFT_ANGLED_BE.get(), pos, state);
        effects = new KineticEffectHandler(this);
        updateSpeed = true;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1);
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (!canPropagateDiagonally(block, state))
            return neighbours;

        Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
        BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (axis.choose(offset.getX(), offset.getY(), offset.getZ()) != 0)
                        return;
                    if (offset.distSqr(BlockPos.ZERO) != 2)
                        return;
                    neighbours.add(worldPosition.offset(offset));
                });
        return neighbours;
    }

    @Override
    protected boolean isNoisy() {
        return false;
    }
}
