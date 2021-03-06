/*
 * Roughly Enough Items by Danielshe.
 * Licensed under the MIT License.
 */

package me.shedaniel.rei.client;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DefaultedList;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ItemRegistryImpl implements ItemRegistry {
    
    private final CopyOnWriteArrayList<ItemStack> itemList = Lists.newCopyOnWriteArrayList();
    
    @Override
    public List<ItemStack> getItemList() {
        return Collections.unmodifiableList(itemList);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getModifiableItemList() {
        return itemList;
    }
    
    @Override
    public ItemStack[] getAllStacksFromItem(Item item) {
        DefaultedList<ItemStack> list = DefaultedList.create();
        list.add(item.getStackForRender());
        item.appendStacks(item.getGroup(), list);
        TreeSet<ItemStack> stackSet = list.stream().collect(Collectors.toCollection(() -> new TreeSet<ItemStack>((p1, p2) -> ItemStack.areEqualIgnoreDamage(p1, p2) ? 0 : 1)));
        return Lists.newArrayList(stackSet).toArray(new ItemStack[0]);
    }
    
    @Override
    public void registerItemStack(Item afterItem, ItemStack stack) {
        if (!stack.isEmpty() && !alreadyContain(stack))
            if (afterItem == null || afterItem.equals(Items.AIR))
                itemList.add(stack);
            else {
                int last = itemList.size();
                for(int i = 0; i < itemList.size(); i++)
                    if (itemList.get(i).getItem().equals(afterItem))
                        last = i + 1;
                itemList.add(last, stack);
            }
    }
    
}
