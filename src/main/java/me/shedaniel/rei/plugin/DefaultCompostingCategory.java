/*
 * Roughly Enough Items by Danielshe.
 * Licensed under the MIT License.
 */

package me.shedaniel.rei.plugin;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import me.shedaniel.rei.api.*;
import me.shedaniel.rei.gui.renderables.RecipeRenderer;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class DefaultCompostingCategory implements RecipeCategory<DefaultCompostingDisplay> {
    
    @Override
    public Identifier getIdentifier() {
        return DefaultPlugin.COMPOSTING;
    }
    
    @Override
    public Renderer getIcon() {
        return Renderable.fromItemStack(new ItemStack(Blocks.COMPOSTER));
    }
    
    @Override
    public String getCategoryName() {
        return I18n.translate("category.rei.composting");
    }
    
    @Override
    public RecipeRenderer getSimpleRenderer(DefaultCompostingDisplay recipe) {
        return new RecipeRenderer() {
            @Override
            public int getHeight() {
                return 10 + MinecraftClient.getInstance().textRenderer.fontHeight;
            }
            
            @Override
            public void render(int x, int y, double mouseX, double mouseY, float delta) {
                MinecraftClient.getInstance().textRenderer.draw(I18n.translate("text.rei.composting.page", recipe.getPage() + 1), x + 5, y + 6, -1);
            }
        };
    }
    
    @Override
    public List<Widget> setupDisplay(Supplier<DefaultCompostingDisplay> recipeDisplaySupplier, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        Point startingPoint = new Point(bounds.x + bounds.width - 55, bounds.y + 110);
        widgets.add(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float partialTicks) {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(DefaultPlugin.getDisplayTexture());
                this.blit(startingPoint.x, startingPoint.y, 28, 221, 55, 26);
            }
        });
        List<ItemConvertible> stacks = new LinkedList<>(recipeDisplaySupplier.get().getItemsByOrder());
        int i = 0;
        for(int y = 0; y < 6; y++)
            for(int x = 0; x < 8; x++) {
                widgets.add(new SlotWidget((int) bounds.getCenterX() - 72 + x * 18, bounds.y + y * 18, stacks.size() > i ? Arrays.asList(stacks.get(i).asItem().getStackForRender()) : Lists.newArrayList(), true, true, true) {
                    @Override
                    protected List<String> getExtraToolTips(ItemStack stack) {
                        final List<String>[] thing = new List[]{null};
                        recipeDisplaySupplier.get().getInputMap().forEach((itemProvider, aFloat) -> {
                            if (itemProvider.asItem().equals(stack.getItem()))
                                thing[0] = Arrays.asList(I18n.translate("text.rei.composting.chance", MathHelper.fastFloor(aFloat * 100)));
                        });
                        if (thing[0] != null)
                            return thing[0];
                        return super.getExtraToolTips(stack);
                    }
                });
                i++;
            }
        widgets.add(new SlotWidget((int) startingPoint.x + 34, startingPoint.y + 5, recipeDisplaySupplier.get().getOutput(), false, true, true));
        return widgets;
    }
    
    @Override
    public DisplaySettings getDisplaySettings() {
        return new DisplaySettings() {
            @Override
            public int getDisplayHeight(RecipeCategory iRecipeCategory) {
                return 140;
            }
            
            @Override
            public int getDisplayWidth(RecipeCategory iRecipeCategory, RecipeDisplay display) {
                return 150;
            }
            
            @Override
            public int getMaximumRecipePerPage(RecipeCategory iRecipeCategory) {
                return -1;
            }
            
            @Override
            public int getFixedRecipesPerPage() {
                return 1;
            }
        };
    }
    
}