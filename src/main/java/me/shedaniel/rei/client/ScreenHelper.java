/*
 * Roughly Enough Items by Danielshe.
 * Licensed under the MIT License.
 */

package me.shedaniel.rei.client;

import com.google.common.collect.Lists;
import me.shedaniel.cloth.hooks.ClothClientHooks;
import me.shedaniel.rei.RoughlyEnoughItemsCore;
import me.shedaniel.rei.gui.ContainerScreenOverlay;
import me.shedaniel.rei.gui.widget.SearchFieldWidget;
import me.shedaniel.rei.listeners.ContainerScreenHooks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.util.Window;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ScreenHelper implements ClientModInitializer {
    
    public static SearchFieldWidget searchField;
    public static List<ItemStack> inventoryStacks = Lists.newArrayList();
    private static boolean overlayVisible = true;
    private static ContainerScreenOverlay overlay;
    private static AbstractContainerScreen lastContainerScreen = null;
    
    public static boolean isOverlayVisible() {
        return overlayVisible;
    }
    
    public static void toggleOverlayVisible() {
        overlayVisible = !overlayVisible;
    }
    
    public static Optional<ContainerScreenOverlay> getOptionalOverlay() {
        return Optional.ofNullable(overlay);
    }
    
    public static ContainerScreenOverlay getLastOverlay(boolean reset, boolean setPage) {
        if (overlay == null || reset) {
            overlay = new ContainerScreenOverlay();
            overlay.init(setPage);
        }
        return overlay;
    }
    
    public static ContainerScreenOverlay getLastOverlay() {
        return getLastOverlay(false, false);
    }
    
    public static AbstractContainerScreen getLastContainerScreen() {
        return lastContainerScreen;
    }
    
    public static void setLastContainerScreen(AbstractContainerScreen lastContainerScreen) {
        ScreenHelper.lastContainerScreen = lastContainerScreen;
    }
    
    public static ContainerScreenHooks getLastContainerScreenHooks() {
        return (ContainerScreenHooks) lastContainerScreen;
    }
    
    public static void drawHoveringWidget(int x, int y, TriConsumer<Integer, Integer, Float> consumer, int width, int height, float delta) {
        Window window = MinecraftClient.getInstance().window;
        drawHoveringWidget(new Dimension(window.getScaledWidth(), window.getScaledHeight()), x, y, consumer, width, height, delta);
    }
    
    public static void drawHoveringWidget(Dimension dimension, int x, int y, TriConsumer<Integer, Integer, Float> consumer, int width, int height, float delta) {
        int actualX = Math.max(x + 12, 6);
        int actualY = Math.min(y - height / 2, dimension.height - height - 6);
        if (actualX + width > dimension.width)
            actualX -= 24 + width;
        if (actualY < 6)
            actualY += 24;
        consumer.accept(actualX, actualY, delta);
    }
    
    public static boolean isDarkModeEnabled() {
        return RoughlyEnoughItemsCore.getConfigManager().getConfig().darkTheme;
    }
    
    @Override
    public void onInitializeClient() {
        ClothClientHooks.SCREEN_INIT_PRE.register((minecraftClient, screen, screenHooks) -> {
            if (lastContainerScreen != screen && screen instanceof AbstractContainerScreen)
                lastContainerScreen = (AbstractContainerScreen<?>) screen;
            return ActionResult.PASS;
        });
    }
    
}
