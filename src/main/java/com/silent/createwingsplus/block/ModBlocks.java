package com.silent.createwingsplus.block;

import com.silent.createwingsplus.WingsPlus;
import com.silent.createwingsplus.block.custom.*;
import com.silent.createwingsplus.item.ModItems;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WingsPlus.MOD_ID);

    public static BlockEntry<AngledSail> ANGLED_SAIL;
    public static BlockEntry<SailShaftStraight> SAIL_SHAFT_STRAIGHT;
    public static BlockEntry<SailShaftAngled> SAIL_SHAFT_ANGLED;
    public static BlockEntry<SailGlue> SAIL_GLUE;
    public static BlockEntry<SailTorsionSpring> SAIL_TORSION_SPRING;

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        ANGLED_SAIL = WingsPlus.REGISTRATE.block("angled_sail", AngledSail::new).simpleItem().register();
        SAIL_SHAFT_STRAIGHT = WingsPlus.REGISTRATE.block("sail_shaft_straight", SailShaftStraight::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        SAIL_SHAFT_ANGLED = WingsPlus.REGISTRATE.block("sail_shaft_angled", SailShaftAngled::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        SAIL_GLUE = WingsPlus.REGISTRATE.block("sail_glue", SailGlue::new).simpleItem().register();
        SAIL_TORSION_SPRING = WingsPlus.REGISTRATE.block("sail_torsion_spring", SailTorsionSpring::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        BLOCKS.register(eventBus);
    }


}
