/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package bleach.hack;

import bleach.hack.util.BleachLogger;
import com.google.common.eventbus.EventBus;
import com.google.gson.JsonElement;

import bleach.hack.command.CommandManager;
import bleach.hack.gui.title.BleachTitleScreen;
import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ClickGui;
import bleach.hack.util.FriendManager;
import bleach.hack.util.file.BleachFileHelper;
import bleach.hack.util.file.BleachFileMang;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import bleach.hack.command.CommandSuggestor;

public class BleachHack implements ModInitializer {

	private static BleachHack instance = null;
	public static BleachLogger logger;

	public static final String VERSION = "1.3-DEV";
	public static final int INTVERSION = 30;

	public static final EventBus eventBus = new EventBus();

	public static FriendManager friendMang;

	//private BleachFileMang bleachFileManager;

	public static BleachHack getInstance() {
		return instance;
	}

	public BleachHack() {
		if (instance != null) {
			throw new RuntimeException("A BleachHack-CupEdition instance already exists.");
		}
	}

	@Override
	public void onInitialize() {
		long initStartTime = System.currentTimeMillis();

		if (instance != null) {
			throw new RuntimeException("BleachHack-CupEdition has already been initialized.");
		}

		instance = this;

		//TODO base-rewrite
		//this.eventBus = new EventBus();
		//this.bleachFileManager = new BleachFileMang();
		instance = this;

		//TODO base-rewrite
		//this.eventBus = new EventBus();
		//this.bleachFileManager = new BleachFileMang();
		BleachFileMang.init();
		ModuleManager.loadModules(this.getClass().getClassLoader().getResourceAsStream("bleachhack.modules.json"));
		BleachFileHelper.readModules();

		ClickGui.clickGui.initWindows();
		BleachFileHelper.readClickGui();
		BleachFileHelper.readFriends();
		BleachFileHelper.readUI();

		CommandManager.readPrefix();
		CommandManager.loadCommands(this.getClass().getClassLoader().getResourceAsStream("bleachhack.commands.json"));
		CommandSuggestor.start();

		JsonElement mainMenu = BleachFileHelper.readMiscSetting("customTitleScreen");
		if (mainMenu != null && !mainMenu.getAsBoolean()) {
			BleachTitleScreen.customTitleScreen = false;
		}

		BleachLogger.logger.log(Level.INFO, "Loaded BleachHack-CupEdition in %d ms.", System.currentTimeMillis() - initStartTime);
	}
}
