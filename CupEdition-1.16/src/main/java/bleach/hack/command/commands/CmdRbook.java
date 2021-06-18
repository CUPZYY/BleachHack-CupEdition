/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package bleach.hack.command.commands;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.math.NumberUtils;

import bleach.hack.command.Command;
import bleach.hack.command.CommandCategory;
import bleach.hack.util.BleachLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

public class CmdRbook extends Command {

	public CmdRbook() {
		super("rbook", "Generates a random book.", "rbook <pages> <start char> <end char> <chrs/page>", CommandCategory.MISC,
				"randombook", "book");
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		ItemStack item = mc.player.inventory.getMainHandStack();

		if (item.getItem() != Items.WRITABLE_BOOK) {
			BleachLogger.errorMessage("Not Holding A Writable Book!");
			return;
		}

		int pages = args.length >= 1 && NumberUtils.isCreatable(args[0]) ? NumberUtils.createNumber(args[0]).intValue() : 100;
		int startChar = args.length >= 2 && NumberUtils.isCreatable(args[1]) ? NumberUtils.createNumber(args[1]).intValue() : 0;
		int endChar = args.length >= 3 && NumberUtils.isCreatable(args[2]) ? NumberUtils.createNumber(args[2]).intValue() : 0x10FFFF;
		int pageChars = args.length >= 4 && NumberUtils.isCreatable(args[3]) ? NumberUtils.createNumber(args[3]).intValue() : 210;

		IntStream chars = new Random().ints(startChar, endChar + 1);
		String text = chars.limit(pageChars * 100).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());

		ListTag textSplit = new ListTag();

		for (int t = 0; t < pages; t++)
			textSplit.add(StringTag.of(text.substring(t * pageChars, (t + 1) * pageChars)));

		item.getOrCreateTag().put("pages", textSplit);
		mc.player.networkHandler.sendPacket(new BookUpdateC2SPacket(item, false, mc.player.inventory.selectedSlot));

		BleachLogger.infoMessage("Written book (" + pages + " pages, " + pageChars + " chars/page)");
	}

}
