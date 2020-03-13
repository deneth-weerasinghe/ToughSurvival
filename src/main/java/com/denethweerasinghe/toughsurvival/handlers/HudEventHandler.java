package com.denethweerasinghe.toughsurvival.handlers;

import com.denethweerasinghe.toughsurvival.playerdata.hydration.Hydration;
import com.denethweerasinghe.toughsurvival.playerdata.hydration.IHydration;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.IWetness;
import com.denethweerasinghe.toughsurvival.playerdata.wetness.Wetness;
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

    private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(ToughSurvival.MOD_ID, "textures/gui/hud_overlay.png");

    @SubscribeEvent
    public static void onRenderHud(RenderGameOverlayEvent.Post event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE && !event.isCanceled()) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = (PlayerEntity) mc.getRenderViewEntity();

            /* checks if the player is in creative or spectator mode. both of these modes are not
            based on survival so no point in drawing them
            */
            if (!(player.isCreative() || player.isSpectator()) && player.isAlive()){

                mc.getTextureManager().bindTexture(GUI_ELEMENTS);

                // hydration bar position
                int hydrPosX = mc.mainWindow.getScaledWidth() / 2 + 10;
                int hydrPosY = mc.mainWindow.getScaledHeight() - 49;

                // prevents hydration bar from overwriting air bar, which only appears under certain conditions
                if (player.areEyesInFluid(FluidTags.WATER) || player.getAir() < 300){
                    hydrPosY -= 10;
                }



                /* parameters not mapped out yet but from what I can tell from extensive testing:
                p_blit1 and p_blit2: x and y coords of point from which to start drawing
                p_blit3 and p_blit4: x and y coords of top left corner of crop from texture file
                p_blit5 and p_blit6: x and y coords of bottom right corner of crop from texture file
                 */

                // drawing hydration bar
                mc.ingameGUI.blit(hydrPosX, hydrPosY, 0, 0, 81, 9);
                IHydration hydrCap = Hydration.getFromPlayer(player);
                int hydrRatio = 81 * hydrCap.getHydration() / Hydration.MAX_HYDRATION;
                mc.ingameGUI.blit(hydrPosX, hydrPosY, 0, 9, hydrRatio, 9);

                // drawing wetness bar
                mc.ingameGUI.blit(5, 5, 0, 18, 25, 101);

                IWetness wetCap = Wetness.getFromPlayer(player);
                int wetness = wetCap.getWetness();
                int wetRatio =  97 * wetness / Wetness.MAX_WETNESS;
                wetRatio = 97 - wetRatio;

                mc.ingameGUI.blit(7 , 7 + wetRatio, 25, 20 + wetRatio, 21, 97 - wetRatio);
            }
        }
    }
}
