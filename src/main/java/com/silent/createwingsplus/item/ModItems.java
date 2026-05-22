package com.silent.createwingsplus.item;

import com.silent.createwingsplus.WingsPlus;
import net.minecraft.references.Items;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WingsPlus.MOD_ID);

    //public static final DeferredItem<Item> ANGLEDSAIL = ITEMS.register("angledsail", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
