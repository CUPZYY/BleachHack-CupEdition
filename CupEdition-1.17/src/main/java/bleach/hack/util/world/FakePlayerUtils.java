package bleach.hack.util.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

public class FakePlayerUtils extends OtherClientPlayerEntity
{
    public FakePlayerUtils()
    {
        this(new GameProfile(UUID.fromString("a96eea13-de45-4ba5-b763-a4f7e65adac8"), "CUPZYY"));
    }

    public FakePlayerUtils(GameProfile profile)
    {
        this(profile, MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ());
    }

    public FakePlayerUtils(GameProfile profile, double x, double y, double z)
    {
        super(MinecraftClient.getInstance().world, profile);
        setPos(x, y, z);
    }

    public void spawn()
    {
        MinecraftClient.getInstance().world.addEntity(this.getId(), this);
    }

    public void despawn()
    {
        MinecraftClient.getInstance().world.removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
    }
}
