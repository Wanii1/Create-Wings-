package com.silent.createwingsplus.block;

import com.silent.createwingsplus.WingsPlus;
import com.silent.createwingsplus.block.custom.*;
import com.silent.createwingsplus.item.ModItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.bearing.BlankSailBlockItem;
import com.simibubi.create.content.contraptions.bearing.SailBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.silent.createwingsplus.WingsPlus.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WingsPlus.MOD_ID);

    public static final BlockEntry<AngledSail> ANGLED_SAIL =
            REGISTRATE.block("angled_sail", p -> AngledSail.withCanvas(p, DyeColor.WHITE))
                    .simpleItem().register();

    public static final DyedBlockList<AngledSail> DYED_ANGLED_SAILS = new DyedBlockList<>(colour -> {
        if (colour == DyeColor.WHITE) {
            return ANGLED_SAIL;
        }
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_angled_sail", p -> AngledSail.withCanvas(p, colour))
                .blockstate((c, p) -> p.directionalBlock(c.get(), p.models()
                        .withExistingParent(colourName + "_angled_sail", p.modLoc("block/angled_sail"))
                        .texture("0", p.modLoc("block/angled_sail_" + colourName)))).simpleItem().register();
    });

    public static final BlockEntry<SailShaftStraight> SAIL_SHAFT_STRAIGHT =
            REGISTRATE.block("sail_shaft_straight", p -> SailShaftStraight.withCanvas(p, DyeColor.WHITE))
                    .simpleItem().register();

    public static final DyedBlockList<SailShaftStraight> DYED_SAIL_SHAFT_STRAIGHTS = new DyedBlockList<>(colour -> {
        if (colour == DyeColor.WHITE) {
            return SAIL_SHAFT_STRAIGHT;
        }
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_sail_shaft_straight", p -> SailShaftStraight.withCanvas(p, colour))
                .blockstate((c, p) -> p.directionalBlock(c.get(), p.models()
                        .withExistingParent(colourName + "_angled_sail", p.modLoc("block/angled_sail"))
                        .texture("0", p.modLoc("block/canvas_" + colourName + "_shaft_straight")))).simpleItem().register();
    });

    public static BlockEntry<SailShaftAngled> SAIL_SHAFT_ANGLED;
    public static BlockEntry<SailGlue> SAIL_GLUE;
    public static BlockEntry<SailTorsionSpring> SAIL_TORSION_SPRING;
    public static BlockEntry<AngledSymetricalSail> ANGLED_SYMETRICAL_SAIL;
    public static BlockEntry<SymetricalSailShaftStraight> SYMETRICAL_SAIL_SHAFT_STRAIGHT;
    public static BlockEntry<SymetricalSailShaftAngled> SYMETRICAL_SAIL_SHAFT_ANGLED;
    public static BlockEntry<SymetricalSailGlue> SYMETRICAL_SAIL_GLUE;
    public static BlockEntry<VerticalSymetricalSailShaftStraight> VERTICAL_SYMETRICAL_SAIL_SHAFT_STRAIGHT;
    public static BlockEntry<VerticalSymetricalSailShaftAngled> VERTICAL_SYMETRICAL_SAIL_SHAFT_ANGLED;
    public static BlockEntry<VerticalSymetricalSailGlue> VERTICAL_SYMETRICAL_SAIL_GLUE;
    public static BlockEntry<VerticalAngledSymetricalSail> VERTICAL_ANGLED_SYMETRICAL_SAIL;

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        //ANGLED_SAIL = REGISTRATE.block("angled_sail", AngledSail::new).simpleItem().register();



        //SAIL_SHAFT_STRAIGHT = REGISTRATE.block("sail_shaft_straight", SailShaftStraight::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        SAIL_SHAFT_ANGLED = REGISTRATE.block("sail_shaft_angled", SailShaftAngled::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        SAIL_GLUE = REGISTRATE.block("sail_glue", SailGlue::new).simpleItem().register();
        SAIL_TORSION_SPRING = REGISTRATE.block("sail_torsion_spring", SailTorsionSpring::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        ANGLED_SYMETRICAL_SAIL = REGISTRATE.block("angled_symetrical_sail", AngledSymetricalSail::new).simpleItem().register();
        SYMETRICAL_SAIL_SHAFT_STRAIGHT = REGISTRATE.block("symetrical_sail_shaft_straight", SymetricalSailShaftStraight::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        SYMETRICAL_SAIL_SHAFT_ANGLED = REGISTRATE.block("symetrical_sail_shaft_angled", SymetricalSailShaftAngled::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        SYMETRICAL_SAIL_GLUE = REGISTRATE.block("symetrical_sail_glue", SymetricalSailGlue::new).simpleItem().register();
        VERTICAL_SYMETRICAL_SAIL_SHAFT_STRAIGHT = REGISTRATE.block("vertical_symetrical_sail_shaft_straight", VerticalSymetricalSailShaftStraight::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        VERTICAL_SYMETRICAL_SAIL_SHAFT_ANGLED = REGISTRATE.block("vertical_symetrical_sail_shaft_angled", VerticalSymetricalSailShaftAngled::new).blockstate(BlockStateGen.axisBlockProvider(false)).simpleItem().register();
        VERTICAL_SYMETRICAL_SAIL_GLUE = REGISTRATE.block("vertical_symetrical_sail_glue", VerticalSymetricalSailGlue::new).simpleItem().register();
        VERTICAL_ANGLED_SYMETRICAL_SAIL = REGISTRATE.block("vertical_angled_symetrical_sail", VerticalAngledSymetricalSail::new).simpleItem().register();
        BLOCKS.register(eventBus);
    }


}
