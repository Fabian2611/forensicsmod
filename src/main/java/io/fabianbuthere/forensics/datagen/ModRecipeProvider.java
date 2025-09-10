package io.fabianbuthere.forensics.datagen;

import io.fabianbuthere.forensics.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.FORENSIC_GLOVES.get())
            .pattern(" L ")
            .pattern("LDL")
            .pattern(" F ")
            .define('L', Items.LEATHER)
            .define('D', Items.WHITE_DYE)
            .define('F', Items.FEATHER)
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BLACK_GLOVES.get())
                .pattern(" L ")
                .pattern("LDL")
                .pattern(" F ")
                .define('L', Items.LEATHER)
                .define('D', Items.BLACK_DYE)
                .define('F', Items.FEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(pWriter);
    }
}
