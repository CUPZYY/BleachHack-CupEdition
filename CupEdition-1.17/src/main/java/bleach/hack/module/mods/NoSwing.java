package bleach.hack.module.mods;

import bleach.hack.event.events.EventSendPacket;
import bleach.hack.event.events.EventSwingHand;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

public class NoSwing extends Module {

    public NoSwing() {
        super("NoSwing", KEY_UNBOUND, Category.MISC, "Makes you not swing your hand",
                new SettingToggle("Client", true).withDesc("Makes you not swing your hand clientside"),
                new SettingToggle("Server", true).withDesc("Makes you not send hand swing packets"));
    }

    @Subscribe
    public void onSwingHand(EventSwingHand event) {
        if (getSetting(0).asToggle().state) {
            event.setCancelled(true);
        }
    }

    @Subscribe
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof HandSwingC2SPacket && getSetting(1).asToggle().state) {
            event.setCancelled(true);
        }
    }
}