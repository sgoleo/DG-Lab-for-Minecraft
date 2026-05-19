package online.kbpf.dg_lab.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public interface ClientPlayerEntityAccessor {
    @Accessor("healthInitialized")
    boolean getHealthInitialized();

    @Accessor("healthInitialized")
    void setHealthInitialized(boolean value);
}
