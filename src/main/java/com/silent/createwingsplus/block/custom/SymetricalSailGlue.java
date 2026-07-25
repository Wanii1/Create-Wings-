package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.entity.ModBlockEntities;
import com.silent.createwingsplus.block.entity.SailGlueEntity;
import com.silent.createwingsplus.block.entity.SymetricalSailGlueEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.block.IBE;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
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


public class SymetricalSailGlue extends BearingBlock implements IBE<SymetricalSailGlueEntity>, BlockSubLevelLiftProvider {

    private static final VoxelShape SHAPE = Block.box(0.0,5.0,0.0,16.0,10.0,16.0);
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    public SymetricalSailGlue(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Axis axis = context.getHorizontalDirection().getAxis();
        Direction direction = Direction.getFacingAxis(context.getPlayer(), axis);
        return this.defaultBlockState().setValue(BlockStateProperties.AXIS, axis).setValue(BlockStateProperties.FACING, direction.getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS);
        builder.add(BlockStateProperties.FACING);
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

    public Class<SymetricalSailGlueEntity> getBlockEntityClass() {
        return SymetricalSailGlueEntity.class;
    }

    public BlockEntityType<? extends SymetricalSailGlueEntity> getBlockEntityType() {
        return (BlockEntityType) ModBlockEntities.SYMETRICAL_SAIL_GLUE_BE.get();
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
        return 1.75f;
    }

    @Override
    public float sable$getLiftScalar() {
        return 0;
    }
}