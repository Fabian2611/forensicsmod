package io.fabianbuthere.forensics.util;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class FilterSlot extends Slot {
    private final Set<Item> allowedItems;

    public FilterSlot(Container container, int slot, int x, int y, Set<Item> allowedItems) {
        super(container, slot, x, y);
        this.allowedItems = allowedItems;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return allowedItems.contains(stack.getItem());
    }
}
