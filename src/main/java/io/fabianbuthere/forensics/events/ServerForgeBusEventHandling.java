package io.fabianbuthere.forensics.events;

import io.fabianbuthere.forensics.Forensics;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Forensics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerForgeBusEventHandling {
    private static void addFingerprintToBlock(ServerPlayer player, BlockPos pos) {
        // TODO
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {

    }
}
