/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package bleach.hack.module.mods;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import bleach.hack.setting.other.SettingLists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.eventbus.Subscribe;


import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.setting.other.SettingRotate;
import bleach.hack.util.world.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * @author CUPZYY | https://github.com/CUPZYY
 */


public class NetherFreedom extends Module {


    private Set<BlockPos> renderBlocks = new HashSet<>();


    public NetherFreedom() {
        super("NetherFreedom", KEY_UNBOUND, Category.WORLD, "Breaks blocks around you",
                new SettingMode("Mode", "Normal", "SurvMulti", "Multi", "Instant").withDesc("Mining mode"),
                new SettingSlider("Multi", 1, 10, 10, 0).withDesc("How many blocks to mine at once if Multi/SurvMulti mode is on"),
                new SettingSlider("Height", 1, 6, 3.0, 1).withDesc("The high the roof is going to be (for nether freedom, this is 3)"),
                new SettingSlider("Range", 1, 6, 5.0, 1).withDesc("How far away should it break blocks"),
                new SettingSlider("Cooldown", 0, 4, 0, 0).withDesc("Cooldown between mining blocks"),
                new SettingMode("Sort", "Closest", "Furthest", "Hardness", "None").withDesc("Which order to mine blocks in"),
                new SettingToggle("Filter", true).withDesc("Filters certain blocks").withChildren(
                        new SettingMode("Mode", "Blacklist", "Whitelist").withDesc("How to handle the list"),
                        SettingLists.newBlockList("Edit Blocks" , "Edit Filtered Blocks", Blocks.OBSIDIAN, Blocks.BEDROCK).withDesc("Edit the filtered blocks")),
                new SettingRotate(false),
                new SettingToggle("NoParticles", false).withDesc("Removes block breaking particles"),
                new SettingToggle("PickOnly", true).withDesc("Only allows pickaxe for mining"));
    }

    @Override
    public void onDisable() {
        renderBlocks.clear();

        super.onDisable();
    }

    @Subscribe
    public void onTick(EventTick event) {
        renderBlocks.clear();

        double range = getSetting(2).asSlider().getValue() - 0.1;

        Map<BlockPos, Pair<Vec3d, Direction>> blocks = new LinkedHashMap<>();

        /* Add blocks around player */
        for (int x = (int) 5; x >= (int) -5; x--) {
            for (int y = (int) range; y >= (0); y--) {
                for (int z = (int) 5; z >= (int) -5; z--) {
                    BlockPos pos = new BlockPos(mc.player.getPos().add(x, y + 0.2, z));

                    if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR && !WorldUtils.isFluid(pos)) {
                        Pair<Vec3d, Direction> vec = getBlockAngle(pos);

                        if (vec != null) {
                            blocks.put(pos, vec);
                        }
                    }
                }
            }
        }

        if (blocks.isEmpty())
            return;

        if (getSetting(5).asMode().mode != 3) {
            Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());

            blocks = blocks.entrySet().stream()
                    .sorted((a, b) -> getSetting(5).asMode().mode <= 1 ?
                            Double.compare(
                                    eyePos.distanceTo((getSetting(5).asMode().mode == 0 ? a : b).getValue().getLeft()),
                                    eyePos.distanceTo((getSetting(5).asMode().mode == 0 ? b : a).getValue().getLeft()))
                            : Float.compare(
                            mc.world.getBlockState(a.getKey()).getHardness(mc.world, a.getKey()),
                            mc.world.getBlockState(b.getKey()).getHardness(mc.world, b.getKey())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
        }

        /* Move the block under the player to last so it doesn't mine itself down
         * without clearing everything above first */
        if (blocks.containsKey(mc.player.getBlockPos().down())) {
            Pair<Vec3d, Direction> v = blocks.get(mc.player.getBlockPos().down());
            blocks.remove(mc.player.getBlockPos().down());
            blocks.put(mc.player.getBlockPos().down(), v);
        }

        int broken = 0;
        for (Entry<BlockPos, Pair<Vec3d, Direction>> pos : blocks.entrySet()) {
            if (getSetting(6).asToggle().state) {
                boolean contains = getSetting(6).asToggle().getChild(1).asList(Block.class).contains(mc.world.getBlockState(pos.getKey()).getBlock());

                if ((getSetting(6).asToggle().getChild(0).asMode().mode == 0 && contains)
                        || (getSetting(6).asToggle().getChild(0).asMode().mode == 1 && !contains)) {
                    continue;
                }
            }

            float hardness = mc.world.getBlockState(pos.getKey()).calcBlockBreakingDelta(mc.player, mc.world, pos.getKey());

            if (getSetting(0).asMode().mode == 1 && hardness <= 1f && broken > 0) {
                return;
            }

            if (getSetting(7).asRotate().state) {
                Vec3d v = pos.getValue().getLeft();
                WorldUtils.facePosAuto(v.x, v.y, v.z, getSetting(7).asRotate()); }

            
            Item mainhandItem = mc.player.getMainHandStack().getItem();
            
            if(!(mainhandItem instanceof PickaxeItem) && getSetting(9).asToggle().state){
                return;
            }
            mc.interactionManager.updateBlockBreakingProgress(pos.getKey(), pos.getValue().getRight());
            renderBlocks.add(pos.getKey());
            mc.player.swingHand(Hand.MAIN_HAND);

            broken++;
            if (getSetting(0).asMode().mode == 0
                    || (getSetting(0).asMode().mode == 1 && hardness <= 1f)
                    || (getSetting(0).asMode().mode == 1 && broken >= getSetting(1).asSlider().getValueInt())
                    || (getSetting(0).asMode().mode == 2 && broken >= getSetting(1).asSlider().getValueInt())) {
                return;
            }
        }
    }


    public Pair<Vec3d, Direction> getBlockAngle(BlockPos pos) {
        for (Direction d: Direction.values()) {
            if (!mc.world.getBlockState(pos.offset(d)).isFullCube(mc.world, pos)) {
                Vec3d vec = WorldUtils.getLegitLookPos(pos, d, true, 5);

                if (vec != null) {
                    return Pair.of(vec, d);
                }
            }
        }

        return null;
    }

}
