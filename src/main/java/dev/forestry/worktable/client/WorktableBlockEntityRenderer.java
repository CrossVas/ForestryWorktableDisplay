package dev.forestry.worktable.client;

import com.mojang.blaze3d.vertex.PoseStack;
import forestry.worktable.recipes.MemorizedRecipe;
import forestry.worktable.tiles.WorktableTile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Objects;

public class WorktableBlockEntityRenderer implements BlockEntityRenderer<WorktableTile> {

    public WorktableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(WorktableTile blockEntity, float v, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int light, int i1) {
        List<MemorizedRecipe> memorizedRecipes = new ObjectArrayList<>();
        // Gather memorized recipes
        for (int j = 0; j < 9; j++) {
            MemorizedRecipe memorizedRecipe = blockEntity.getMemory().getRecipe(j);
            if (memorizedRecipe != null) {
                memorizedRecipes.add(memorizedRecipe);
            }
        }
        // Display memorized recipes
        if (!memorizedRecipes.isEmpty()) {
            float blockScale = 0.25f;
            float itemScale = 0.125f;
            final double spacing = 0.189;
            final double offset = 0.31;
            matrixStack.translate(0, 1.0625, 0);

            for (int i = 0; i < memorizedRecipes.size(); i++) {
                MemorizedRecipe recipe = memorizedRecipes.get(i);
                ItemStack item = recipe.getSelectedRecipe().getResultItem(blockEntity.getLevel().registryAccess());
                if (item.isEmpty()) return;

                // Calculate the row and column for each item starting from top-left
                int row = i / 3;
                int col = i % 3;

                boolean isItem = !(item.getItem() instanceof BlockItem);

                matrixStack.pushPose();
                matrixStack.translate(1 - offset - col * spacing, isItem ? - 0.06 : 0, 1 - offset - row * spacing);
                float scale = isItem ? itemScale : blockScale;
                matrixStack.scale(scale, scale, scale);
                if (isItem) { // rotate item model
                    matrixStack.mulPose((new Quaternionf()).rotationX(90.0F * ((float) Math.PI / 180F)));
                }
                BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(item, blockEntity.getLevel(), null, 0);
                int lightAbove = LevelRenderer.getLightColor(Objects.requireNonNull(blockEntity.getLevel()), blockEntity.getBlockPos().above());
                Minecraft.getInstance().getItemRenderer().render(item, ItemDisplayContext.FIXED,
                        false, matrixStack, multiBufferSource, lightAbove, OverlayTexture.NO_OVERLAY, bakedmodel);
                matrixStack.popPose();
            }
        }
    }
}
