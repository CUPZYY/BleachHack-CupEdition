/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package bleach.hack.gui.clickgui.window;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.UI;
import org.apache.commons.lang3.tuple.Triple;

import bleach.hack.gui.window.Window;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

public abstract class ClickGuiWindow extends Window {

	protected MinecraftClient mc = MinecraftClient.getInstance();

	public int mouseX;
	public int mouseY;

	public boolean hiding;

	public int keyDown = -1;
	public boolean lmDown = false;
	public boolean rmDown = false;
	public boolean lmHeld = false;
	public int mwScroll = 0;

	public ClickGuiWindow(int x1, int y1, int x2, int y2, String title, ItemStack icon) {
		super(x1, y1, x2, y2, title, icon);
	}

	public boolean shouldClose(int mouseX, int mouseY) {
		return false;
	}

	protected void drawBar(MatrixStack matrix, int mouseX, int mouseY, TextRenderer textRend) {
		/* background */
		// Upper line
		boolean round = ModuleManager.getModule("ClickGUI").getSetting(3).asToggle().state;
		boolean rainbow = ModuleManager.getModule("ClickGUI").getSetting(4).asToggle().state;
		int rainbowSpeed = ModuleManager.getModule("ClickGUI").getSetting(4).asToggle().getChild(0).asSlider().getValueInt();
		int rgb = ModuleManager.getModule("ClickGUI").getSetting(5).asColor().getRGB();
		int colorFF = 0xff000000 | rgb;
		int color26 = 0x26000000 | rgb;
		int rainbowFF = UI.getRainbow(0.5f, 1, (80) - (rainbowSpeed + 29), 0);
		int rainbow26 = (UI.getRainbow(0.5f, 1, (80) - (rainbowSpeed + 29), 0) & 0x00FFFFFF) | 0x26000000;


		switch (ModuleManager.getModule("ClickGui").getSetting(6).asMode().mode) {
			case 0:
				if (round) {
					DrawableHelper.fill(matrix, x1, y1 + 1, x1 + 1, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x1, y1, x1 + 1, y2, !rainbow ? colorFF : rainbowFF);
				}
				// Line below category title
				DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, !hiding ? y1 + 13 : y1 + 12, !rainbow ? colorFF : rainbowFF);
				// Fullfill | DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, y1 + 1, 0xff55FF55);
				// lines on the sides
				DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, y1 + 1, !rainbow ? colorFF : rainbowFF);
				if (round) {
					DrawableHelper.fill(matrix, x2 - 1, y1 + 1, x2, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x2 - 1, y1, x2, y2, !rainbow ? colorFF : rainbowFF);
				}
				// Bottom line
				DrawableHelper.fill(matrix, x1 + 1, y2 - 1, x2 - 1, y2, !rainbow ? colorFF : rainbowFF);
				break;
			case 1:
				if (round) {
					DrawableHelper.fill(matrix, x1, y1 + 1, x1 + 1, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x1, y1, x1 + 1, y2, !rainbow ? colorFF : rainbowFF);
				}
				// Line below category title
				DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, !hiding ? y1 + 13 : y1 + 12, !rainbow ? colorFF : rainbowFF);
				// DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, y1 + 1, 0xff55FF55);
				DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, y2 - 1, !rainbow ? color26 : rainbow26);
				// lines on the sides
				DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, y1 + 1, !rainbow ? colorFF : rainbowFF);
				if (round) {
					DrawableHelper.fill(matrix, x2 - 1, y1 + 1, x2, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x2 - 1, y1, x2, y2, !rainbow ? colorFF : rainbowFF);
				}
				DrawableHelper.fill(matrix, x1 + 1, y2 - 1, x2 - 1, y2, !rainbow ? colorFF : rainbowFF);
				break;
			case 2:
				if (round) {
					DrawableHelper.fill(matrix, x1, y1 + 1, x1 + 1, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x1, y1, x1 + 1, y2, !rainbow ? colorFF : rainbowFF);
				}
				// Line below category title
				// Fullfill | DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, y1 + 1, 0xff55FF55);
				// lines on the sides
				DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, y1 + 1, !rainbow ? colorFF : rainbowFF);
				if (round) {
					DrawableHelper.fill(matrix, x2 - 1, y1 + 1, x2, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x2 - 1, y1, x2, y2, !rainbow ? colorFF : rainbowFF);
				}
				// Bottom line
				DrawableHelper.fill(matrix, x1 + 1, y2 - 1, x2 - 1, y2, !rainbow ? colorFF : rainbowFF);
				break;
			case 3:
				if (round) {
					DrawableHelper.fill(matrix, x1, y1 + 1, x1 + 1, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x1, y1, x1 + 1, y2, !rainbow ? colorFF : rainbowFF);
				}
				// Line below category title
				// DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, !hiding ? y1 + 13 : y1 + 12, 0xff55FF55);
				// DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, y1 + 1, 0xff55FF55);
				DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, y2 - 1, !rainbow ? color26 : rainbow26);
				// lines on the sides
				DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, y1 + 1, !rainbow ? colorFF : rainbowFF);
				if (round) {
					DrawableHelper.fill(matrix, x2 - 1, y1 + 1, x2, y2 - 1, !rainbow ? colorFF : rainbowFF);
				} else {
					DrawableHelper.fill(matrix, x2 - 1, y1, x2, y2, !rainbow ? colorFF : rainbowFF);
				}
				DrawableHelper.fill(matrix, x1 + 1, y2 - 1, x2 - 1, y2, !rainbow ? colorFF : rainbowFF);
				break;
		}
		/* +/- text */
		textRend.draw(matrix, hiding ? "+" : "_", x2 - 10, y1 + (hiding ? 4 : 2), 0x000000);
		textRend.draw(matrix, hiding ? "+" : "_", x2 - 11, y1 + (hiding ? 3 : 1), 0xffffff);
	}

	public void render(MatrixStack matrix, int mouseX, int mouseY) {
		super.render(matrix, mouseX, mouseY);

		if (rmDown && mouseOver(x1, y1, x1 + (x2 - x1), y1 + 13)) {
			mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			hiding = !hiding;
		}
	}
	public Triple<Integer, Integer, String> getTooltip() {
		return null;
	}

	public boolean mouseOver(int minX, int minY, int maxX, int maxY) {
		return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY < maxY;
	}


	public void updateKeys(int mouseX, int mouseY, int keyDown, boolean lmDown, boolean rmDown, boolean lmHeld, int mwScroll) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.keyDown = keyDown;
		this.lmDown = lmDown;
		this.rmDown = rmDown;
		this.lmHeld = lmHeld;
		this.mwScroll = mwScroll;
	}
}
