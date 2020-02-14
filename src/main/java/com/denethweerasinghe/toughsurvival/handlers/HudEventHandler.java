package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.IHydration;
import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class HudEventHandler {

    private static final ResourceLocation hydrationBar = new ResourceLocation(ToughSurvival.MOD_ID, "textures/gui/hud_overlay.png");

    @SubscribeEvent
    public static void onRenderHud(RenderGameOverlayEvent.Post event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE && !event.isCanceled()) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = (PlayerEntity) mc.getRenderViewEntity();

            /* checks if the player is in creative or spectator mode. both of these modes are not
            based on survival so no point in drawing them
            */
            if (!(player.isCreative() || player.isSpectator())){

                int elementPosX = mc.mainWindow.getScaledWidth() / 2 + 10;
                int elementPosY = mc.mainWindow.getScaledHeight() - 49;

                // prevents hydration bar from overwriting air bar, which only appears under certain conditions
                if (player.areEyesInFluid(FluidTags.WATER) || player.getAir() < 300){
                    elementPosY -= 10;
                }

                mc.getTextureManager().bindTexture(hydrationBar);

                /* parameters not mapped out yet but from what I can tell from extensive testing:
                p_blit1 and p_blit2: x and y coords of point from which to start drawing
                p_blit3 and p_blit4: x and y coords of top left corner of crop from texture file
                p_blit5 and p_blit6: x and y coords of bottom right corner of crop from texture file
                 */
                mc.ingameGUI.blit(elementPosX, elementPosY, 0, 0, 81, 9);

                if (player.isAlive()) {
                    IHydration cap = Hydration.getFromPlayer(player);
                    int ratio = 81 * cap.getHydration() / Hydration.MAX_HYDRATION;
                    mc.ingameGUI.blit(elementPosX, elementPosY, 0, 9, ratio, 9);
                }
            }
        }
    }
}
