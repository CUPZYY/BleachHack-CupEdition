package bleach.hack.module.mods;


import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.setting.other.SettingRotate;
import bleach.hack.util.BleachLogger;
import bleach.hack.util.world.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.BedBlock;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * @author CUPZYY | https://github.com/CUPZYY
 */


public class AutoBedtrap extends Module {
    BlockPos lookingCoords;
    Direction bed2direction;
    BlockPos bed2;
    int cap = 0;
    boolean bed;
    boolean working = false;

    public AutoBedtrap() {
        super("AutoBedtrap", KEY_UNBOUND, Category.WORLD, "Automatically places obsidian around bed",
                new SettingSlider("BPT", 1, 8, 2, 0).withDesc("Blocks per tick, how many blocks to place per tick"),
                new SettingToggle("Keep on", true).withDesc("Keeps the module on after placing the obsidian"),
                new SettingRotate(false).withDesc("Rotates when placing"));

    }

    public void onEnable() {
        super.onEnable();

        assert mc.crosshairTarget != null;
        if (!working) {
            lookingCoords = mc.crosshairTarget.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) mc.crosshairTarget).getBlockPos() : null;
            if (lookingCoords == null && (!getSetting(1).asToggle().state)) {
                BleachLogger.errorMessage("Not looking at a bed");
                setEnabled(false);
                return;
            }
            else if (lookingCoords == null) {
                return;
            }
            bed = mc.world.getBlockState(lookingCoords).getBlock().getName().toString().toUpperCase().contains("_BED");
        }
        if ((!bed || lookingCoords == null) && (!getSetting(1).asToggle().state)){
            BleachLogger.errorMessage("Not looking at a bed");
            setEnabled(false);
            return;
        }

        int obby = -1;
        for (int i = 0; i < 9; i++) {
            assert mc.player != null;
            if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                obby = i;
                break;
            }
        }

        if (obby == -1) {
            BleachLogger.errorMessage("No obsidian in hotbar!");
            setEnabled(false);
        }
    }
    public void onDisable() {
        super.onDisable();
        working = false;
        cap = 0;
        lookingCoords = null;
    }


    @Subscribe
    public void onTick(EventTick event) {
        assert mc.crosshairTarget != null;
        if (!working) {
            lookingCoords = mc.crosshairTarget.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) mc.crosshairTarget).getBlockPos() : null;
        }
        assert lookingCoords != null;
        assert mc.world != null;
        assert mc.world.getBlockState(lookingCoords).getBlock().getName().toString() != null;
        if ((!getSetting(1).asToggle().state) && ((lookingCoords == null) || (!bed))) {
            setEnabled(false);
            return;
        }
        try {
            if (!working) {
                bed = mc.world.getBlockState(lookingCoords).getBlock().getName().toString().toUpperCase().contains("_BED");
            }
        } catch (NullPointerException e) {
            return;
        }
        if (bed || working) {
            BlockPos bed1 = lookingCoords;
            bed2direction = BedBlock.getOppositePartDirection(mc.world.getBlockState(bed1));
            if (bed2direction == Direction.EAST) {
                bed2 = lookingCoords.east(1);
            } else if (bed2direction == Direction.NORTH) {
                bed2 = lookingCoords.north(1);
            } else if (bed2direction == Direction.SOUTH) {
                bed2 = lookingCoords.south(1);
            } else if (bed2direction == Direction.WEST) {
                bed2 = lookingCoords.west(1);
            }
            int obby = -1;
            for (int i = 0; i < 9; i++) {
                assert mc.player != null;
                if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                    obby = i;
                    break;
                }
            }

            if (obby == -1) {
                BleachLogger.errorMessage("Ran out of obsidian!");
                setEnabled(false);
                return;
            }

            placeTickAround(obby, bed1);
            placeTickAround(obby, bed2);
        }
    }


    public void placeTickAround(int obsidian, BlockPos block) {
        working = true;
        for (BlockPos b : new BlockPos[]{
                block.up(), block.west(),
                block.north(), block.south(),
                block.east(), block.down()}) {

            if (cap >= getSetting(0).asSlider().getValueInt()) {
                cap = 0;
                return;
            }

            for (int i = 0; i < 9; i++) {
                assert mc.player != null;
                if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                    obsidian = i;
                    break;
                }
            }
            if (!(mc.player.getInventory().getStack(obsidian).getItem() == Items.OBSIDIAN)) {
                BleachLogger.errorMessage("Ran out of obsidian!");
                setEnabled(false);
                return;
            }


            if (WorldUtils.canPlaceBlock(b)) {
                WorldUtils.placeBlock(b, obsidian, getSetting(2).asRotate(), false, false, true);
                cap++;

                if (cap >= getSetting(0).asSlider().getValueInt()) {
                    return;
                }
            }
        }
        working = false;
        cap = 0;
    }
}