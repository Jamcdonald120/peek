package de.maxhenkel.peek.events;

import de.maxhenkel.peek.Peek;
import de.maxhenkel.peek.data.DataStore;
import de.maxhenkel.peek.tooltips.ContainerTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

import java.util.Optional;

public class TooltipImageEvents {

    private static final Minecraft mc = Minecraft.getInstance();

    private static final String ITEMS = "Items";

    private static HolderLookup.Provider getProvider() {
        return mc.getConnection().registryAccess();
    }

    public static Optional<TooltipComponent> getTooltipImage(ItemStack stack, Block block) {
        if (block instanceof ShulkerBoxBlock) {
            return getShulkerBoxTooltipImage(stack);
        } else if (block instanceof ChestBlock) {
            return getChestTooltipImage(stack);
        } else if (block instanceof BarrelBlock) {
            return getBarrelTooltipImage(stack);
        } else if (block instanceof DispenserBlock) {
            return getDispenserTooltipImage(stack);
        } else if (block instanceof HopperBlock) {
            return getHopperTooltipImage(stack);
        } else if (block instanceof EnderChestBlock) {
            return getEnderChestTooltipImage();
        }

        return Optional.empty();
    }

    private static Optional<TooltipComponent> getEnderChestTooltipImage() {

        if (!Peek.CONFIG.peekEnderChests.get()) {
            return Optional.empty();
        }

        if (DataStore.enderChestInventory == null) {
            return Optional.empty();
        }

        return Optional.of(new ContainerTooltip(9, 3, DataStore.enderChestInventory));
    }

    private static Optional<TooltipComponent> getShulkerBoxTooltipImage(ItemStack stack) {
        if (!Peek.CONFIG.peekShulkerBoxes.get()) {
            return Optional.empty();
        }

        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);

        if (!Peek.CONFIG.showEmptyContainers.get() && contents.stream().allMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }

        return Optional.of(new ContainerTooltip(9, 3, contents, ShulkerBoxBlockEntity.CONTAINER_SIZE));
    }

    private static Optional<TooltipComponent> getChestTooltipImage(ItemStack stack) {
        if (!Peek.CONFIG.peekChests.get()) {
            return Optional.empty();
        }
        return getDefaultChestSizeTooltipImage(stack);
    }

    private static Optional<TooltipComponent> getBarrelTooltipImage(ItemStack stack) {
        if (!Peek.CONFIG.peekBarrels.get()) {
            return Optional.empty();
        }
        return getDefaultChestSizeTooltipImage(stack);
    }

    private static Optional<TooltipComponent> getDispenserTooltipImage(ItemStack stack) {
        if (!Peek.CONFIG.peekDispensers.get()) {
            return Optional.empty();
        }

        CustomData customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        if (customData == null) {
            return Optional.empty();
        }

        CompoundTag blockEntityData = customData.getUnsafe();
        if (!blockEntityData.contains(ITEMS, Tag.TAG_LIST)) {
            return Optional.empty();
        }

        NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(blockEntityData, items, getProvider());

        if (!Peek.CONFIG.showEmptyContainers.get() && items.stream().allMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }

        return Optional.of(new ContainerTooltip(3, 3, items));
    }

    private static Optional<TooltipComponent> getHopperTooltipImage(ItemStack stack) {
        if (!Peek.CONFIG.peekHoppers.get()) {
            return Optional.empty();
        }

        CustomData customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        if (customData == null) {
            return Optional.empty();
        }

        CompoundTag blockEntityData = customData.getUnsafe();
        if (!blockEntityData.contains(ITEMS, Tag.TAG_LIST)) {
            return Optional.empty();
        }

        NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(blockEntityData, items, getProvider());

        if (!Peek.CONFIG.showEmptyContainers.get() && items.stream().allMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }

        return Optional.of(new ContainerTooltip(5, 1, items));
    }

    private static Optional<TooltipComponent> getDefaultChestSizeTooltipImage(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        if (customData == null) {
            return Optional.empty();
        }

        CompoundTag blockEntityData = customData.getUnsafe();
        if (!blockEntityData.contains(ITEMS, Tag.TAG_LIST)) {
            return Optional.empty();
        }

        NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(blockEntityData, items, getProvider());

        if (!Peek.CONFIG.showEmptyContainers.get() && items.stream().allMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }

        return Optional.of(new ContainerTooltip(9, 3, items));
    }

}
