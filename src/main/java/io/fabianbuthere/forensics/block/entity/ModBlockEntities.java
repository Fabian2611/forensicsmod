package io.fabianbuthere.forensics.block.entity;

import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Forensics.MOD_ID);

    public static final RegistryObject<BlockEntityType<DevelopingStationBlockEntity>> DEVELOPING_STATION_BE =
            BLOCK_ENTITIES.register("developing_station_be", () ->
                    BlockEntityType.Builder.of(DevelopingStationBlockEntity::new,
                            ModBlocks.DEVELOPING_STATION.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
