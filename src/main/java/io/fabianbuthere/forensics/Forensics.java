package io.fabianbuthere.forensics;

import com.mojang.logging.LogUtils;
import io.fabianbuthere.forensics.block.ModBlocks;
import io.fabianbuthere.forensics.item.ModCreativeModeTabs;
import io.fabianbuthere.forensics.item.ModItems;
import io.fabianbuthere.forensics.screen.DevelopingStationScreen;
import io.fabianbuthere.forensics.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Forensics.MOD_ID)
public class Forensics
{
    public static final String MOD_ID = "forensics";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Forensics(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);

        modEventBus.addListener(Forensics::onClientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("removal")
    public Forensics()
    {
        this(FMLJavaModLoadingContext.get());
    }

    private static void onClientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.DEVELOPING_STATION_MENU.get(), DevelopingStationScreen::new);
        });
    }
}
