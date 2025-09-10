package io.fabianbuthere.forensics.item;

import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Forensics.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FORENSICS_TAB = CREATIVE_MODE_TABS.register("forensics_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(Items.BRUSH))
            .title(Component.translatable("creativetab.forensics_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.SWAB_KIT.get());
                pOutput.accept(ModItems.FORENSIC_GLOVES.get());

                pOutput.accept(ModBlocks.DEVELOPING_STATION.get().asItem());
            })
        .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
