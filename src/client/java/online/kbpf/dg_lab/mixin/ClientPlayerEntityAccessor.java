package online.kbpf.dg_lab.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public interface ClientPlayerEntityAccessor {
    @Accessor("flashOnSetHealth")
    boolean getHealthInitialized();

    @Accessor("flashOnSetHealth")
    void setHealthInitialized(boolean value);
}
