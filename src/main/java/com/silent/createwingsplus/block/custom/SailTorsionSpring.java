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


public class SailTorsionSpring extends DirectionalKineticBlock implements IBE<SailTorsionSpringEntity>, ExtraKinetics.ExtraKineticsBlock, IDirectionalAnalogOutput {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape SHAPE = Block.box(0.0,5.0,0.0,16.0,10.0,16.0);

    public SailTorsionSpring(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    public boolean hasShaftTowards(final LevelReader world, final BlockPos pos, final BlockState state, final Direction face) {
        return face.getOpposite() == state.getValue(FACING);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected VoxelShape getShape(final BlockState blockState, final BlockGetter blockGetter, final BlockPos blockPos, final CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected void neighborChanged(final BlockState blockState, final Level level, final BlockPos blockPos, final Block block, final BlockPos blockPos2, final boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        final boolean signal = level.hasNeighborSignal(blockPos);
        if (signal != blockState.getValue(POWERED)) {
            level.setBlock(blockPos, blockState.setValue(POWERED, signal), 2); // idk what this magic number does... copied from DiodeBlock
            this.withBlockEntityDo(level, blockPos, SailTorsionSpringEntity::onSignalChanged);
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(final BlockState blockState) {
        return blockState.getValue(FACING).getAxis().isHorizontal();
    }

    @Override
    public int getAnalogOutputSignalFrom(final BlockState blockState, final Level level, final BlockPos blockPos, final Direction dir) {
        final Direction facing = blockState.getValue(FACING);
        final SailTorsionSpringEntity be = this.getBlockEntity(level, blockPos);

        final float frac = Mth.clamp (be.getAngle() / be.angleInput.getValue(), -1, 1);
        if (Math.abs(be.getAngle()) < 0.99) {
            return 0;
        }
        final int value = (int) (((frac < 0 ? Math.floor(frac * 15) : Math.ceil(frac * 15)) *
                ((facing.getStepX() == 1 || facing.getStepZ() == 1) ? -1 : 1)));

        if (facing.getClockWise() == dir && value > 0) {
            return value;
        } else if (facing.getCounterClockWise() == dir && value < 0) {
            return -value;
        }
        return 0;
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

    @Override
    public IRotate getExtraKineticsRotationConfiguration() {
        return SailTorsionSpringEntity.Output.CONFIG;
    }
}