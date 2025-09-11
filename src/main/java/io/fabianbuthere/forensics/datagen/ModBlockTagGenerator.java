package io.fabianbuthere.forensics.datagen;

import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Forensics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.DEVELOPING_STATION.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.DEVELOPING_STATION.get());
    }
}
