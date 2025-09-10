package io.fabianbuthere.forensics.datagen;

import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Forensics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SWAB_KIT);
        simpleItem(ModItems.DEVELOPING_SWAB_KIT);
        simpleItem(ModItems.DEVELOPED_SWAB_KIT);
        simpleItem(ModItems.FAILED_SWAB_KIT);
        simpleItem(ModItems.FORENSIC_GLOVES);
    }

    @SuppressWarnings("removal")
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Forensics.MOD_ID, "item/" + item.getId().getPath()));
    }
}
