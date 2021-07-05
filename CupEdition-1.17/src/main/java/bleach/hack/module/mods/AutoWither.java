package bleach.hack.module.mods;


import bleach.hack.event.events.EventTick;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingColor;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.setting.other.SettingRotate;
import bleach.hack.util.BleachLogger;
import bleach.hack.util.render.RenderUtils;
import bleach.hack.util.render.color.QuadColor;
import bleach.hack.util.world.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.lwjgl.opengl.GL11;

/**
 * @author CUPZYY | https://github.com/CUPZYY
 */


public class AutoWither extends Module {
    int cap = 0;
    boolean working = true;
    BlockPos sand1;
    BlockPos sand2;
    BlockPos sand3;
    BlockPos sand4;
    BlockPos head1;
    BlockPos head2;
    BlockPos head3;


    public AutoWither() {
        super("AutoWither", KEY_UNBOUND, Category.WORLD, "Automatically spawns a wither in front of you",
                new SettingSlider("Range", 1, 4, 4, 0).withDesc("How far away the wither shall be spawned"),
                new SettingSlider("BPT", 1, 8, 2, 0).withDesc("Blocks per tick, how many blocks to place per tick"),
                new SettingSlider("Box", 0.1, 4, 2, 1).withDesc("The thickness of the box lines"),
                new SettingSlider("Fill", 0, 1, 0.3, 2).withDesc("The opacity of the fill"),
                new SettingMode("Render", "Box+Fill", "Box", "Fill").withDesc("The rendering method"),
                new SettingColor("Color", 1f, 0f, 0f, false),
                new SettingRotate(false).withDesc("Rotates when placing"));

    }

    public void onEnable() {
        super.onEnable();
        int range = getSetting(0).asSlider().getValueInt();
        assert mc.player != null;
        assert mc.world != null;
        Direction facing = mc.player.getHorizontalFacing();
        if (facing == Direction.NORTH) {
            for (; ; range -= 1) {
                BlockPos pos = mc.player.getBlockPos().north(range);
                sand1 = pos;
                sand2 = pos.up();
                sand3 = pos.up().east();
                sand4 = pos.up().west();
                head1 = pos.up(2);
                head2 = pos.up(2).east();
                head3 = pos.up(2).west();
                if ((mc.world.getBlockState(sand1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand3).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand4).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head3).getMaterial().isReplaceable())) break;
            }
            if (range == 0) {
                BleachLogger.errorMessage("No space available");
                setEnabled(false);
                return;
            }

        }
        else if (facing == Direction.SOUTH) {
            for (; ; range -= 1) {
                BlockPos pos = mc.player.getBlockPos().south(range);
                sand1 = pos;
                sand2 = pos.up();
                sand3 = pos.up().east();
                sand4 = pos.up().west();
                head1 = pos.up(2);
                head2 = pos.up(2).east();
                head3 = pos.up(2).west();
                if ((mc.world.getBlockState(sand1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand3).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand4).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head3).getMaterial().isReplaceable())) break;
            }
            if (range == 0) {
                BleachLogger.errorMessage("No space available");
                setEnabled(false);
                return;
            }

        }
        else if (facing == Direction.EAST) {
            for (; ; range -= 1) {
                BlockPos pos = mc.player.getBlockPos().east(range);
                sand1 = pos;
                sand2 = pos.up();
                sand3 = pos.up().north();
                sand4 = pos.up().south();
                head1 = pos.up(2);
                head2 = pos.up(2).north();
                head3 = pos.up(2).south();
                if ((mc.world.getBlockState(sand1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand3).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand4).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head3).getMaterial().isReplaceable())) break;
            }
            if (range == 0) {
                BleachLogger.errorMessage("No space available");
                setEnabled(false);
                return;
            }

        }
        else if (facing == Direction.WEST) {
            for (; ; range -= 1) {
                BlockPos pos = mc.player.getBlockPos().west(range);
                sand1 = pos;
                sand2 = pos.up();
                sand3 = pos.up().north();
                sand4 = pos.up().south();
                head1 = pos.up(2);
                head2 = pos.up(2).north();
                head3 = pos.up(2).south();
                if ((mc.world.getBlockState(sand1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand3).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(sand4).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head1).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head2).getMaterial().isReplaceable() &&
                        mc.world.getBlockState(head3).getMaterial().isReplaceable())) break;
            }
            if (range == 0) {
                BleachLogger.errorMessage("No space available");
                setEnabled(false);
                return;
            }

        }
    }
    public void onDisable(){
        super.onDisable();
        cap = 0;
        working = false;
        sand1 = null;
        sand2 = null;
        sand3 = null;
        sand4 = null;
        head1 = null;
        head2 = null;
        head3 = null;
    }



    @Subscribe
    public void onTick(EventTick event) {

        int sand = -1;
        for (int i = 0; i < 9; i++) {
            assert mc.player != null;
            if (mc.player.getInventory().getStack(i).getItem() == Items.SOUL_SAND) {
                sand = i;
                break;
            }
        }
        int head = -1;
        for (int i = 0; i < 9; i++) {
            assert mc.player != null;
            if (mc.player.getInventory().getStack(i).getItem() == Items.WITHER_SKELETON_SKULL) {
                head = i;
                break;
            }
        }

        if (head == -1) {
            BleachLogger.errorMessage("Ran out of soul sand!");
            setEnabled(false);
            return;
        }
        placeTick();

        if (!working) {
            setEnabled(false);
            return;
        }
    }



    public void placeTick() {
        working = true;
        for (BlockPos b : new BlockPos[]{
                sand1, sand2,
                sand3, sand4}) {
            if (cap >= getSetting(1).asSlider().getValueInt()) {
                cap = 0;
                return;
            }
            int item = -1;
            for (int i = 0; i < 9; i++) {
                assert mc.player != null;
                if (mc.player.getInventory().getStack(i).getItem() == Items.SOUL_SAND) {
                    item = i;
                    break;
                }
            }


            if (WorldUtils.canPlaceBlock(b)) {
                WorldUtils.placeBlock(b, item, getSetting(6).asRotate(), false, false, true);
                cap++;

                if (cap >= getSetting(1).asSlider().getValueInt()) {
                    return;
                }
            }
        }
        for (BlockPos b : new BlockPos[]{
                head1, head2,
                head3}) {
            if (cap >= getSetting(1).asSlider().getValueInt()) {
                cap = 0;
                return;
            }
            int item = -1;
            for (int i = 0; i < 9; i++) {
                assert mc.player != null;
                if (mc.player.getInventory().getStack(i).getItem() == Items.WITHER_SKELETON_SKULL) {
                    item = i;
                    break;
                }
            }


            if (WorldUtils.canPlaceBlock(b)) {
                WorldUtils.placeBlock(b, item, getSetting(6).asRotate(), false, false, true);
                cap++;

                if (cap >= getSetting(1).asSlider().getValueInt()) {
                    return;
                }
            }
        }
        working = false;
        cap = 0;
    }

    @Subscribe
    public void onRender(EventWorldRender event) {
        for (BlockPos b : new BlockPos[]{
                sand1, sand2,
                sand3, sand4,
                head1, head2,
                head3}) {

            if (b == null) return;
            assert mc.world != null;

            this.drawFilledBlockBox(b);
        }
    }

    public void drawFilledBlockBox(BlockPos blockPos) {
        float[] rgb = getSetting(5).asColor().getRGBFloat();
        BlockState state = mc.world.getBlockState(blockPos);
        VoxelShape voxelShape = state.getOutlineShape(mc.world, blockPos);
        int mode = getSetting(4).asMode().mode;
        if (voxelShape.isEmpty()) {
            voxelShape = VoxelShapes.cuboid(0, 0, 0, 1, 1, 1);
        }

        if (mode == 0 || mode == 2) {
            float fillAlpha = getSetting(3).asSlider().getValueFloat();

            for (Box box : voxelShape.getBoundingBoxes()) {
                RenderUtils.drawBoxFill(box.offset(blockPos), QuadColor.single(rgb[0], rgb[1], rgb[2], fillAlpha));
            }
        }
        if (mode == 0 || mode == 1) {
            float outlineWidth = getSetting(2).asSlider().getValueFloat();

            for (Box box : voxelShape.getBoundingBoxes()) {
                RenderUtils.drawBoxOutline(box.offset(blockPos), QuadColor.single(rgb[0], rgb[1], rgb[2], 1f), outlineWidth);
            }
        }
    }
}