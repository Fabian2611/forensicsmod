package io.fabianbuthere.forensics.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldFingerprintData extends SavedData {
    private static final String DATA_NAME = "forensics_fingerprint_data";

    private final Map<BlockPos, UUID> fingerprints = new HashMap<>();
    private final Map<BlockPos, UUID> footprints = new HashMap<>();

    public WorldFingerprintData() {
        super();
    }

    public static WorldFingerprintData get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(WorldFingerprintData::load, WorldFingerprintData::new, DATA_NAME);
    }

    public static WorldFingerprintData load(CompoundTag tag) {
        WorldFingerprintData data = new WorldFingerprintData();

        // Load fingerprints
        ListTag fingerprintList = tag.getList("fingerprints", Tag.TAG_COMPOUND);
        for (Tag fingerprintTag : fingerprintList) {
            CompoundTag compound = (CompoundTag) fingerprintTag;
            BlockPos pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
            UUID uuid = compound.getUUID("uuid");
            data.fingerprints.put(pos, uuid);
        }

        // Load footprints
        ListTag footprintList = tag.getList("footprints", Tag.TAG_COMPOUND);
        for (Tag footprintTag : footprintList) {
            CompoundTag compound = (CompoundTag) footprintTag;
            BlockPos pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
            UUID uuid = compound.getUUID("uuid");
            data.footprints.put(pos, uuid);
        }

        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        // Save fingerprints
        ListTag fingerprintList = new ListTag();
        for (Map.Entry<BlockPos, UUID> entry : fingerprints.entrySet()) {
            CompoundTag compound = new CompoundTag();
            BlockPos pos = entry.getKey();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            compound.putUUID("uuid", entry.getValue());
            fingerprintList.add(compound);
        }
        tag.put("fingerprints", fingerprintList);

        // Save footprints
        ListTag footprintList = new ListTag();
        for (Map.Entry<BlockPos, UUID> entry : footprints.entrySet()) {
            CompoundTag compound = new CompoundTag();
            BlockPos pos = entry.getKey();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            compound.putUUID("uuid", entry.getValue());
            footprintList.add(compound);
        }
        tag.put("footprints", footprintList);

        return tag;
    }

    public void addFingerprint(BlockPos pos, UUID playerUUID) {
        fingerprints.put(pos, playerUUID);
        setDirty();
    }

    private UUID uuidToFootprintUUID(UUID playerUUID) {
        long msb = playerUUID.getMostSignificantBits();
        long lsb = playerUUID.getLeastSignificantBits();
        msb = ~msb;
        lsb = ~lsb;
        return new UUID(msb, lsb);
    }

    public void addFootprint(BlockPos pos, UUID playerUUID) {
        footprints.put(pos, uuidToFootprintUUID(playerUUID));
        setDirty();
    }

    public UUID getFingerprint(BlockPos pos) {
        return fingerprints.get(pos);
    }

    public UUID getFootprint(BlockPos pos) {
        return footprints.get(pos);
    }

    public boolean hasFingerprint(BlockPos pos) {
        return fingerprints.containsKey(pos);
    }

    public boolean hasFootprint(BlockPos pos) {
        return footprints.containsKey(pos);
    }

    public void removeFingerprint(BlockPos pos) {
        fingerprints.remove(pos);
        setDirty();
    }

    public void removeFootprint(BlockPos pos) {
        footprints.remove(pos);
        setDirty();
    }
}
