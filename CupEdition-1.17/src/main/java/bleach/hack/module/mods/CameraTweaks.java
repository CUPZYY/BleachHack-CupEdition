package bleach.hack.module.mods;

import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;

public class CameraTweaks extends Module {

	public CameraTweaks() {
		super("CameraTweaks", KEY_UNBOUND, Category.RENDER, "Improves the 3rd person camera",
				new SettingToggle("CameraClip", true).withDesc("Makes the camera clip into walls"),
				new SettingToggle("Distance", true).withDesc("Distance to the player").withChildren(
						new SettingSlider("Distance", 0.5, 20, 4, 1).withDesc("The desired camera distance")));
	}
}