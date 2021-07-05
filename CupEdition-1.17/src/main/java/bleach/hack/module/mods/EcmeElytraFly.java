package bleach.hack.module.mods;

import com.google.common.eventbus.Subscribe;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import net.minecraft.util.math.MathHelper;


public class EcmeElytraFly extends Module {

    public EcmeElytraFly() {
        super("EcmeElytraFly", KEY_UNBOUND, Category.MOVEMENT, "Elytrafly bypass for endcrystal.me",
                new SettingSlider("Boost", 0, 0.15, 0.10, 2).withDesc("Boost speed"),
                new SettingSlider("MaxBoost", 0, 5, 2.8, 1).withDesc("Max boost speed"),
                new SettingSlider("Speed", 0, 5, 5.00, 2).withDesc("Max speed"));
    }

    @Subscribe
    public void onTick(EventTick event) {
        double currentVel = Math.abs(mc.player.getVelocity().x) + Math.abs(mc.player.getVelocity().y) + Math.abs(mc.player.getVelocity().z);
        float radianYaw = (float) Math.toRadians(mc.player.getYaw());
        float boost = (float) getSetting(0).asSlider().getValue();

        if (mc.player.isFallFlying() && currentVel <= getSetting(1).asSlider().getValue()) {
            if (mc.options.keyBack.isPressed()) {
                mc.player.addVelocity(MathHelper.sin(radianYaw) * boost, 0, MathHelper.cos(radianYaw) * -boost);
            } else if (mc.player.getPitch() > 0) {
                mc.player.addVelocity(MathHelper.sin(radianYaw) * -boost, 0, MathHelper.cos(radianYaw) * boost);
            }
        }

    }
}