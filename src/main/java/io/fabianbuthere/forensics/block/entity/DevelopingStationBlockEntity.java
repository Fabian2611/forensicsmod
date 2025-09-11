package io.fabianbuthere.forensics.block.entity;

import io.fabianbuthere.forensics.item.ModItems;
import io.fabianbuthere.forensics.screen.DevelopingStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class DevelopingStationBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> DEVELOPING_SLOT_ITEMS.contains(stack.getItem());
                case 1 -> CHEMICAL_SLOT_ITEMS.contains(stack.getItem());
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private static final Set<Item> DEVELOPING_SLOT_ITEMS = Set.of(
            ModItems.DEVELOPING_SWAB_KIT.get(),
            ModItems.DEVELOPING_FOOTPRINT_MOULD.get()
    );

    private static final Set<Item> CHEMICAL_SLOT_ITEMS = Set.of(
            Items.GLOWSTONE_DUST,
            Items.WATER_BUCKET
    );

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 300;

    public DevelopingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DEVELOPING_STATION_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> DevelopingStationBlockEntity.this.progress;
                    case 1 -> DevelopingStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> DevelopingStationBlockEntity.this.progress = pValue;
                    case 1 -> DevelopingStationBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.forensics.developing_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DevelopingStationMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("developing_station.progress", progress);

        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("developing_station.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private ItemStack getSwabResult() {
        ItemStack result = new ItemStack(ModItems.DEVELOPED_SWAB_KIT.get(), 1);

        CompoundTag swabTag = this.itemHandler.getStackInSlot(0).getTag();
        CompoundTag resultTag = result.getOrCreateTag();

        if (swabTag != null && swabTag.contains("fingerprint") && swabTag.contains("position")) {
            resultTag.putUUID("fingerprint", swabTag.getUUID("fingerprint"));
            resultTag.put("position", swabTag.getCompound("position"));

            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();
            lore.add(StringTag.valueOf("{\"text\":\"Fingerprint at (%s, %s, %s) is @%s.\",\"color\":\"dark_purple\",\"italic\":false}".formatted(
                    swabTag.getCompound("position").getInt("x"),
                    swabTag.getCompound("position").getInt("y"),
                    swabTag.getCompound("position").getInt("z"),
                    swabTag.getUUID("fingerprint").toString()
            )));
            display.put("Lore", lore);
            resultTag.put("display", display);

            result.setTag(resultTag);
        } else {
            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();
            lore.add(StringTag.valueOf("{\"text\":\"Contains no valid fingerprint data!\",\"color\":\"red\",\"italic\":false,\"bold\":true}"));
            display.put("Lore", lore);
            resultTag.put("display", display);

            result.setTag(resultTag);
        }

        return result;
    }

    private ItemStack getMouldResult() {
        ItemStack result = new ItemStack(ModItems.DEVELOPED_FOOTPRINT_MOULD.get(), 1);

        CompoundTag mouldTag = this.itemHandler.getStackInSlot(0).getTag();
        CompoundTag resultTag = result.getOrCreateTag();

        if (mouldTag != null && mouldTag.contains("footprint") && mouldTag.contains("position")) {
            resultTag.putUUID("footprint", mouldTag.getUUID("footprint"));
            resultTag.put("position", mouldTag.getCompound("position"));

            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();
            lore.add(StringTag.valueOf("{\"text\":\"Footprint at (%s, %s, %s) is @%s.\",\"color\":\"dark_purple\",\"italic\":false}".formatted(
                    mouldTag.getCompound("position").getInt("x"),
                    mouldTag.getCompound("position").getInt("y"),
                    mouldTag.getCompound("position").getInt("z"),
                    mouldTag.getUUID("footprint").toString()
            )));
            display.put("Lore", lore);
            resultTag.put("display", display);

            result.setTag(resultTag);
        } else {
            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();
            lore.add(StringTag.valueOf("{\"text\":\"Contains no valid footprint data!\",\"color\":\"red\",\"italic\":false,\"bold\":true}"));
            display.put("Lore", lore);
            resultTag.put("display", display);

            result.setTag(resultTag);
        }

        return result;
    }

    private void craftItem() {
        ItemStack result = this.itemHandler.getStackInSlot(0).is(ModItems.DEVELOPING_SWAB_KIT.get()) ? getSwabResult() : getMouldResult();

        this.itemHandler.extractItem(0, 1, false);
        this.itemHandler.extractItem(1, 1, false);

        this.itemHandler.setStackInSlot(2, result);
    }

    private boolean hasRecipe() {
        boolean hasDevelopingKit = this.itemHandler.getStackInSlot(0).is(ModItems.DEVELOPING_SWAB_KIT.get()) ||
                this.itemHandler.getStackInSlot(0).is(ModItems.DEVELOPING_FOOTPRINT_MOULD.get());

        boolean hasChemical = this.itemHandler.getStackInSlot(1).is(Items.GLOWSTONE_DUST) ||
                this.itemHandler.getStackInSlot(1).is(Items.WATER_BUCKET);

        return hasDevelopingKit && hasChemical && canInsertAmountIntoOutputSlot(1) &&
                (canInsertItemIntoOutputSlot(new ItemStack(ModItems.SWAB_KIT.get())) || canInsertItemIntoOutputSlot(new ItemStack(ModItems.FOOTPRINT_MOULD.get())));
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack result) {
        return this.itemHandler.getStackInSlot(2).isEmpty() || this.itemHandler.getStackInSlot(2).getItem() == result.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(2).getCount() + count <= this.itemHandler.getStackInSlot(2).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    public static Set<Item> getDevelopingSlotItems() {
        return DEVELOPING_SLOT_ITEMS;
    }

    public static Set<Item> getChemicalSlotItems() {
        return CHEMICAL_SLOT_ITEMS;
    }
}
