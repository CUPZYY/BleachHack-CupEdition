package bleach.hack.module.mods;

import java.util.HashSet;
import java.util.Set;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.util.world.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Ghosthand extends Module {

	public Ghosthand() {
		super("Ghosthand", KEY_UNBOUND, Category.PLAYER, "Opens Containers Through Walls");
	}

	@Subscribe
	public void onTick(EventTick event) {
		if (!mc.options.keyUse.isPressed() || mc.player.isSneaking())
			return;

		// Return if we are looking at any block entities
		BlockPos lookingPos = new BlockPos(mc.player.raycast(4.25, mc.getTickDelta(), false).getPos());
		for (BlockEntity b : WorldUtils.getBlockEntities()) {
			if (lookingPos.equals(b.getPos())) {
				return;
			}
		}

		Set<BlockPos> posList = new HashSet<>();

		Vec3d nextPos = new Vec3d(0, 0, 0.1)
				.rotateX(-(float) Math.toRadians(mc.player.getPitch()))
				.rotateY(-(float) Math.toRadians(mc.player.getYaw()));

		for (int i = 1; i < 50; i++) {
			BlockPos curPos = new BlockPos(mc.player.getCameraPosVec(mc.getTickDelta()).add(nextPos.multiply(i)));
			if (!posList.contains(curPos)) {
				posList.add(curPos);

				for (BlockEntity b : WorldUtils.getBlockEntities()) {
					if (b.getPos().equals(curPos)) {
						mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND,
								new BlockHitResult(mc.player.getPos(), Direction.UP, curPos, true));
						return;
					}
				}
			}
		}
	}

}