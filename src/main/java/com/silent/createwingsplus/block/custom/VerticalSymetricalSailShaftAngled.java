package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.AbstractAngleShaftSymetricalBlock;
import com.silent.createwingsplus.block.AbstractAngleShaftVerticalSymetricalBlock;
import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SymetricalSailShaftAngledEntity;
import com.silent.createwingsplus.block.entity.VerticalSymetricalSailShaftAngledEntity;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.UP;


public class VerticalSymetricalSailShaftAngled extends AbstractAngleShaftVerticalSymetricalBlock implements IBE<VerticalSymetricalSailShaftAngledEntity>, BlockSubLevelLiftProvider {

    private static final VoxelShape box = Block.box(5.5, 0.0, 0.0, 10.5, 16.0, 16.0);
    private static final VoxelShaper SHAPE = VoxelShaper.forDirectional(box, Direction.NORTH);
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public VerticalSymetricalSailShaftAngled(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Axis axis = context.getNearestLookingDirection().getAxis();
        Direction vertical = context.getNearestLookingVerticalDirection();
        Axis horizontal = context.getHorizontalDirection().getAxis();
        Direction finalDirection = Direction.getFacingAxis(context.getPlayer(), horizontal);
        boolean up = false;

        if (vertical == Direction.UP){
            up = true;
        }

        if (finalDirection == null){
            finalDirection = Direction.NORTH;
        }

        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, finalDirection).setValue(UP, up);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(BlockStateProperties.FACING);
        builder.add(UP);
        builder.add(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if (state.getValue(BlockStateProperties.FACING) == Direction.UP || state.getValue(BlockStateProperties.FACING) == Direction.DOWN) {
            return SHAPE.get(Direction.NORTH);
        }

        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public Class<VerticalSymetricalSailShaftAngledEntity> getBlockEntityClass() {
        return VerticalSymetricalSailShaftAngledEntity.class;
    }

    @Override
    public BlockEntityType<? extends VerticalSymetricalSailShaftAngledEntity> getBlockEntityType() {
        return (BlockEntityType)ModBlockEntities.VERTICAL_SYMETRICAL_SAIL_SHAFT_ANGLED_BE.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        Direction blockDir = state.getValue(FACING);
        boolean up = state.getValue(UP);

        if (up == true){
            if (face == Direction.UP){
                return true;
            }
            else if (face == blockDir){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (face == Direction.DOWN){
                return true;
            }
            else if (face == blockDir){
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState state) {
        return Direction.DOWN;
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