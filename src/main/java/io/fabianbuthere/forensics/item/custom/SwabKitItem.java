package io.fabianbuthere.forensics.item.custom;

import io.fabianbuthere.forensics.item.ModItems;
import io.fabianbuthere.forensics.util.FingerprintHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import java.util.UUID;

public class SwabKitItem extends Item {
    public SwabKitItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        InteractionHand hand = pContext.getHand();

        ItemStack handItem = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            handItem.shrink(1);

            UUID fingerprint = FingerprintHelper.getFingerprint(level, pContext.getClickedPos());
            if (fingerprint == null) {
                ItemStack newItem = new ItemStack(Items.PAPER);
                player.setItemInHand(hand, newItem);
                return InteractionResult.SUCCESS;
            }

            ItemStack newItem = new ItemStack(ModItems.DEVELOPING_SWAB_KIT.get());
            CompoundTag tag = newItem.getOrCreateTag();
            CompoundTag position = new CompoundTag();
            position.putInt("x", pContext.getClickedPos().getX());
            position.putInt("y", pContext.getClickedPos().getY());
            position.putInt("z", pContext.getClickedPos().getZ());
            tag.putUUID("fingerprint", fingerprint);
            tag.put("position", position);
            CompoundTag display = new CompoundTag();
            ListTag lore = new ListTag();
            lore.add(net.minecraft.nbt.StringTag.valueOf("{\"text\":\"Contains fingerprint data for (%d, %d, %d).\"}".formatted(
                    pContext.getClickedPos().getX(),
                    pContext.getClickedPos().getY(),
                    pContext.getClickedPos().getZ()
            )));
            display.put("Lore", lore);
            tag.put("display", display);
            newItem.setTag(tag);

            player.setItemInHand(hand, newItem);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
