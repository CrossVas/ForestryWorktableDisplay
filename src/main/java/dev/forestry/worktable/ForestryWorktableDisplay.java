package dev.forestry.worktable;

import dev.forestry.worktable.client.WorktableBlockEntityRenderer;
import forestry.worktable.features.WorktableTiles;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ForestryWorktableDisplay.MODID)
public class ForestryWorktableDisplay {

    public static final String MODID = "forestryworktabledisplay";

    public ForestryWorktableDisplay() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);
    }

    public void clientSetup(final FMLClientSetupEvent e) {
        BlockEntityRenderers.register(WorktableTiles.WORKTABLE.tileType(), WorktableBlockEntityRenderer::new);
    }
}
