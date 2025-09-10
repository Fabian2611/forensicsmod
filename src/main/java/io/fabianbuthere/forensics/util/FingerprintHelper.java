package io.fabianbuthere.forensics.util;

import io.fabianbuthere.forensics.data.WorldFingerprintData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class FingerprintHelper {

    public static void addFingerprint(Level level, BlockPos pos, UUID playerUUID) {
        if (level instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            data.addFingerprint(pos, playerUUID);
        }
    }

    public static void addFootprint(Level level, BlockPos pos, UUID playerUUID) {
        if (level instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            data.addFootprint(pos, playerUUID);
        }
    }

    public static UUID getFingerprint(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            return data.getFingerprint(pos);
        }
        return null;
    }

    public static UUID getFootprint(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            return data.getFootprint(pos);
        }
        return null;
    }

    public static boolean hasFingerprint(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            return data.hasFingerprint(pos);
        }
        return false;
    }

    public static boolean hasFootprint(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            WorldFingerprintData data = WorldFingerprintData.get(serverLevel);
            return data.hasFootprint(pos);
        }
        return false;
    }

    public static String getPlayerName(Level level, UUID playerUUID) {
        if (level instanceof ServerLevel serverLevel) {
            Player player = serverLevel.getPlayerByUUID(playerUUID);
            return player != null ? player.getName().getString() : "Unknown Player";
        }
        return "Unknown Player";
    }
}
