package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SymetricalSailGlueEntity;
import com.silent.createwingsplus.block.entity.VerticalSymetricalSailGlueEntity;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.block.IBE;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static com.silent.createwingsplus.block.entity.VerticalSymetricalSailShaftAngledEntity.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.UP;


public class VerticalSymetricalSailGlue extends BearingBlock implements IBE<VerticalSymetricalSailGlueEntity>, BlockSubLevelLiftProvider {

    private static final VoxelShape box = Block.box(5.5, 0.0, 0.0, 10.5, 16.0, 16.0);
    private static final VoxelShaper SHAPE = VoxelShaper.forDirectional(box, Direction.NORTH);
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public VerticalSymetricalSailGlue(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Axis axis = context.getNearestLookingDirection().getAxis();
        Axis vertical = context.getNearestLookingDirection().getAxis();
        Axis horizontal = context.getHorizontalDirection().getAxis();
        Direction finalDirection = Direction.getFacingAxis(context.getPlayer(), axis);
        boolean up = false;

        if (vertical == Axis.Y){
            if (context.getNearestLookingDirection() == Direction.DOWN){
                if (horizontal == Axis.X){
                    finalDirection = Direction.DOWN;
                }
                else {
                    finalDirection = Direction.DOWN;
                    up = true;
                }
            }
            else if (context.getNearestLookingDirection() == Direction.UP){
                if (horizontal == Axis.X){
                    finalDirection = Direction.UP;
                }
                else {
                    finalDirection = Direction.UP;
                    up = true;
                }
            }
        }

        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, finalDirection.getOpposite()).setValue(BlockStateProperties.UP, up);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(BlockStateProperties.FACING);
        builder.add(UP);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!player.mayBuild()) {
            return ItemInteractionResult.FAIL;
        } else if (player.isShiftKeyDown()) {
            return ItemInteractionResult.FAIL;
        } else if (stack.isEmpty()) {
            if (level.isClientSide) {
                return ItemInteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(level, pos, (be) -> {
                    if (be.isRunning()) {
                        be.disassemble();
                    } else {
                        be.assembleNextTick = true;
                    }
                });
                return ItemInteractionResult.SUCCESS;
            }
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    public Class<VerticalSymetricalSailGlueEntity> getBlockEntityClass() {
        return VerticalSymetricalSailGlueEntity.class;
    }

    public BlockEntityType<? extends VerticalSymetricalSailGlueEntity> getBlockEntityType() {
        return (BlockEntityType) ModBlockEntities.VERTICAL_SYMETRICAL_SAIL_GLUE_BE.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if (state.getValue(BlockStateProperties.FACING) == Direction.UP || state.getValue(BlockStateProperties.FACING) == Direction.DOWN) {
            if(state.getValue(BlockStateProperties.UP)){
                return SHAPE.get(Direction.NORTH);
            }
            else {
                return SHAPE.get(Direction.WEST);
            }
        }

        return SHAPE.get(state.getValue(FACING));
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