package io.fabianbuthere.forensics.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Set;

public class FilterSlot extends SlotItemHandler {
    private final Set<Item> allowedItems;

    public FilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Set<Item> allowedItems) {
        super(itemHandler, index, xPosition, yPosition);
        this.allowedItems = allowedItems;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return allowedItems.contains(stack.getItem());
    }
}
