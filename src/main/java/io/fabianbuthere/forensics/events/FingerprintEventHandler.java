package io.fabianbuthere.forensics.events;

import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.data.WorldFingerprintData;
import io.fabianbuthere.forensics.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Forensics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FingerprintEventHandler {
    private static boolean isPlayerProtectedFromFingerprinting(Player player) {
        if (player.isSpectator()) return true;
        ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
        return offhand.is(ModItems.FORENSIC_GLOVES.get()) || player.getMainHandItem().is(ModItems.SWAB_KIT.get());
    }

    private static boolean isPlayerProtectedFromFootprinting(Player player) {
        if (player.isSpectator()) return true;
        return !player.getItemBySlot(EquipmentSlot.FEET).isEmpty();
    }

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            if (isPlayerProtectedFromFingerprinting(player)) return;
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            data.addFingerprint(event.getPos(), player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            if (isPlayerProtectedFromFingerprinting(player)) return;
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            data.addFingerprint(event.getPos(), player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.level() instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            data.removeFingerprint(event.getPos());
            data.removeFootprint(event.getPos());
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player && player.level() instanceof ServerLevel serverLevel) {
            if (isPlayerProtectedFromFootprinting(player)) return;

            BlockPos pos = player.blockPosition().below();
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            data.addFootprint(pos, player.getUUID());
        }
    }
}
