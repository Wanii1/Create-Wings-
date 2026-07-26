package com.silent.createwingsplus.block.custom;

import com.simibubi.create.content.contraptions.bearing.SailBlock;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.UP;

public class VerticalAngledSymetricalSail extends SailBlock implements BlockSubLevelLiftProvider {
    private static final VoxelShape SHAPE1 = Block.box(6.0,0.0,0.0,10.0,8.0,16.0); //0.0,5.0,0.0,7.5,10.0,16.0
    private static final VoxelShape SHAPE2 = Block.box(6.0,0.0,0.0,10.0,16.0,8.0); //8.5,5.0,0.0,16.0,10.0,8.0
    private static final VoxelShape SHAPE3 = Block.box(6.0,8.0,0.0,10.0,16.0,16.0);
    private static final VoxelShape SHAPEDOWN = Shapes.join(SHAPE1, SHAPE2, BooleanOp.OR);
    private static final VoxelShape SHAPEUP = Shapes.join(SHAPE3, SHAPE2, BooleanOp.OR);
    private static final VoxelShaper CSHAPE1 = VoxelShaper.forDirectional(SHAPEDOWN, Direction.NORTH);
    private static final VoxelShaper CSHAPE2 = VoxelShaper.forDirectional(SHAPEUP, Direction.NORTH);
    public VerticalAngledSymetricalSail(Properties properties) {
        super(properties, false, DyeColor.WHITE);
    }


    @Override
    public void applyDye(BlockState state, Level world, BlockPos pos, Vec3 hit, @Nullable DyeColor color) {
        return;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        DyeColor color = DyeColor.getColor(stack);
        if (color != null) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis axis = context.getNearestLookingDirection().getAxis();
        Direction vertical = context.getNearestLookingVerticalDirection();
        Direction.Axis horizontal = context.getHorizontalDirection().getAxis();
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
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState blockState) {
        return Direction.get(Direction.AxisDirection.POSITIVE, blockState.getValue(AXIS));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if (state.getValue(BlockStateProperties.UP)){
            if (state.getValue(BlockStateProperties.FACING) == Direction.UP || state.getValue(BlockStateProperties.FACING) == Direction.DOWN) {
                return CSHAPE2.get(Direction.NORTH);
            }

            return CSHAPE2.get(state.getValue(FACING));
        }
        else {
            if (state.getValue(BlockStateProperties.FACING) == Direction.UP || state.getValue(BlockStateProperties.FACING) == Direction.DOWN) {
                return CSHAPE1.get(Direction.NORTH);
            }

            return CSHAPE1.get(state.getValue(FACING));
        }
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