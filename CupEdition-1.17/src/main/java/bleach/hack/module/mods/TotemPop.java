package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.util.BleachLogger;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;
// Author: HerraVp and CUPZYY


public class TotemPop extends Module {

    private HashMap<String, Integer> pops = new HashMap<>();

    public boolean impact_toggle_state;

    public TotemPop() {
        super("PopCounter", KEY_UNBOUND, Category.COMBAT, "Counts totem pops",
                new SettingToggle("Self", true).withDesc("Counts yourself too when you pop"),
                new SettingToggle("Public Chat", true).withDesc("Sends it the totem pops to the chat instead of client only"));
    }

    public void
    onDisable()
    {
        super.onDisable();
        pops.clear();
    }

    @Subscribe
    public void
    onReadPacket(EventReadPacket event)
    {
        if(event.getPacket() instanceof EntityStatusS2CPacket)
        {
            EntityStatusS2CPacket pack = (EntityStatusS2CPacket) event.getPacket();

            if(pack.getStatus() == 35)
            {
                handlePop(pack.getEntity(mc.world));
            }
        }
    }

    @Subscribe
    public void
    onTick(EventTick tick)
    {
        if(mc.world == null)
            return;

        mc.world.getPlayers().forEach(player -> {
            if(player.getHealth() <= 0)
            {
                if(pops.containsKey(player.getEntityName()))
                {
                    if((!getSetting(0).asToggle().state) && (player == mc.player))
                        return;
                    if (!getSetting(1).asToggle().state) {
                        BleachLogger.infoMessage("\u00A7f" + player.getEntityName() + " \u00A79died after popping " + "\u00A7f" + pops.get(player.getEntityName()) + " \u00A79totems");
                    }
                    else{
                        assert mc.player != null;
                        mc.player.sendChatMessage(player.getEntityName() + " died after popping " + pops.get(player.getEntityName()) + " totems");
                    }
                    pops.remove(player.getEntityName(), pops.get(player.getEntityName()));
                }
            }
        });
    }

    private void
    handlePop(Entity entity){
        if(pops == null)
            pops = new HashMap<>();

        if((!getSetting(0).asToggle().state) && (entity == mc.player))
            return;

        if(pops.get(entity.getEntityName()) == null)
        {
            pops.put(entity.getEntityName(), 1);
            if (!getSetting(1).asToggle().state) {
                BleachLogger.infoMessage("\u00A7f" + entity.getEntityName() + " \u00A79popped "+ "\u00A7f" + "1 \u00A79totem");
            }
            else{
                assert mc.player != null;
                mc.player.sendChatMessage(entity.getEntityName() + " popped " + "1 totem");
            }
        }
        else if(!(pops.get(entity.getEntityName()) == null))
        {
            int popc = pops.get(entity.getEntityName());
            popc += 1;
            pops.put(entity.getEntityName(), popc);
            if (!getSetting(1).asToggle().state) {
                BleachLogger.infoMessage("\u00A7f" + entity.getEntityName() + " \u00A79popped "+ "\u00A7f" + popc + " \u00A79totems");
            }
            else{
                assert mc.player != null;
                mc.player.sendChatMessage(entity.getEntityName() + " popped " + popc + " totems");
            }
        }
    }

}