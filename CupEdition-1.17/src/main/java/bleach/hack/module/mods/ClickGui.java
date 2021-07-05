/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.setting.base.SettingColor;
import bleach.hack.setting.base.SettingMode;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;

import bleach.hack.gui.clickgui.ModuleClickGuiScreen;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;

public class ClickGui extends Module {

	public static final ModuleClickGuiScreen clickGui = new ModuleClickGuiScreen();

	public ClickGui() {
		super("ClickGui", GLFW.GLFW_KEY_RIGHT_SHIFT, Category.RENDER, "Draws the clickgui",
				new SettingSlider("Length", 70, 85, 85, 0).withDesc("The length of each window"),
				new SettingToggle("Search bar", true).withDesc("Shows a search bar"),
				new SettingToggle("Help", true).withDesc("Shows the help text"),
				new SettingToggle("Round", true).withDesc("Rounded corners"),
				new SettingToggle("Rainbow", false).withDesc("Rainbow gui").withChildren(
						new SettingSlider("Speed", 1, 50, 15, 0).withDesc("How fast the colors are changing")
				),
				new SettingColor("Color", 0.333f, 1f, 0.333f, false),
				new SettingMode("Theme", "Wire", "SalHackSkid", "Clear", "Full"),
				new SettingToggle("Reset", false).withDesc("Resets the gui"));
	}

	@Override
	public void onEnable() {
		super.onEnable();

		mc.openScreen(clickGui);
	}

	@Subscribe
	public void onTick(EventTick event) {
		if (getSetting(7).asToggle().state){
			ModuleClickGuiScreen.resetGUI();
			getSetting(7).asToggle().state = false;
		}
	}


	@Override
	public void onDisable() {
		if (mc.currentScreen instanceof ModuleClickGuiScreen) {
			mc.openScreen(null);
		}

		super.onDisable();
	}
}
