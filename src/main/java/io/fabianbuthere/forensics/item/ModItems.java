package io.fabianbuthere.forensics.item;

import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.item.custom.SwabKitItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Forensics.MOD_ID);

    public static final RegistryObject<Item> SWAB_KIT = ITEMS.register("swab_kit",
            () -> new SwabKitItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DEVELOPING_SWAB_KIT = ITEMS.register("developing_swab_kit",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DEVELOPED_SWAB_KIT = ITEMS.register("developed_swab_kit",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> FAILED_SWAB_KIT = ITEMS.register("failed_swab_kit",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> FORENSIC_GLOVES = ITEMS.register("forensic_gloves",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
