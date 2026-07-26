package com.silent.createwingsplus.block.custom;

import com.silent.createwingsplus.block.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.bearing.SailBlock;
import com.simibubi.create.foundation.utility.BlockHelper;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AngledSymetricalSail extends SailBlock implements BlockSubLevelLiftProvider {
    private static final VoxelShape SHAPE1 = Block.box(0.0,6.0,0.0,8.0,10.0,16.0);
    private static final VoxelShape SHAPE2 = Block.box(8.0,6.0,0.0,16.0,10.0,8.0);
    private static final VoxelShape SHAPE = Shapes.join(SHAPE1, SHAPE2, BooleanOp.OR);
    private static final VoxelShaper CSHAPE = VoxelShaper.forDirectional(SHAPE, Direction.NORTH);
    public AngledSymetricalSail(Properties properties) {
        super(properties, false, DyeColor.WHITE);
    }


    @Override
    public void applyDye(BlockState state, Level world, BlockPos pos, Vec3 hit, @Nullable DyeColor color) {
        BlockState newState = (color == null ? AllBlocks.SAIL_FRAME : ModBlocks.DYED_ANGLED_SAILS.get(color)).getDefaultState();
        newState = BlockHelper.copyProperties(state, newState);

        if (state !=  newState){
            world.setBlockAndUpdate(pos, newState);
        }
        return;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        
        if (frame) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        DyeColor color = DyeColor.getColor(stack);
        if (color != null){
            if (!level.isClientSide)
                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0f, 1.1f - level.random.nextFloat() * .2f);
            applyDye(state, level, pos, hitResult.getLocation(), color);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState state) {
        return Direction.DOWN;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if (state.getValue(BlockStateProperties.FACING) == Direction.UP || state.getValue(BlockStateProperties.FACING) == Direction.DOWN) {
            return CSHAPE.get(Direction.NORTH);
        }

        return CSHAPE.get(state.getValue(FACING));
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