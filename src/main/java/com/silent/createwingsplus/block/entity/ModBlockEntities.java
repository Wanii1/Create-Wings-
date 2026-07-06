package com.silent.createwingsplus.block.entity;

import com.silent.createwingsplus.WingsPlus;
import com.silent.createwingsplus.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, WingsPlus.MOD_ID);

    public static final Supplier<BlockEntityType<SailShaftStraightEntity>> SAIL_SHAFT_STRAIGHT_BE = BLOCK_ENTITIES.register("sail_shaft_straight_be", () -> BlockEntityType.Builder.of(SailShaftStraightEntity::new, ModBlocks.SAIL_SHAFT_STRAIGHT.get()).build(null));
    public static final Supplier<BlockEntityType<SailShaftAngledEntity>> SAIL_SHAFT_ANGLED_BE = BLOCK_ENTITIES.register("sail_shaft_angled_be", () -> BlockEntityType.Builder.of(SailShaftAngledEntity::new, ModBlocks.SAIL_SHAFT_ANGLED.get()).build(null));
    public static final Supplier<BlockEntityType<SailGlueEntity>> SAIL_GLUE_BE = BLOCK_ENTITIES.register("sail_glue_be", () -> BlockEntityType.Builder.of(SailGlueEntity::new, ModBlocks.SAIL_GLUE.get()).build(null));
    public static final Supplier<BlockEntityType<SailTorsionSpringEntity>> SAIL_TORSION_SPRING_BE = BLOCK_ENTITIES.register("sail_torsion_spring_be", () -> BlockEntityType.Builder.of(SailTorsionSpringEntity::new, ModBlocks.SAIL_TORSION_SPRING.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
