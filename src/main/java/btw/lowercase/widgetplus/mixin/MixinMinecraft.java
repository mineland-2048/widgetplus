package btw.lowercase.widgetplus.mixin;

import btw.lowercase.widgetplus.impl.util.ScreenTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements ScreenTime {
    @Unique
    private long widgetplus$openScreenTimestamp = 0;

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void widgetplus$updateTimestamp(final Screen screen, final CallbackInfo ci) {
        this.widgetplus$openScreenTimestamp = screen != null ? System.currentTimeMillis() : 0;
    }

    @Override
    public int widgetplus$getElapsedOpenScreenTime() {
        return this.widgetplus$openScreenTimestamp != 0 ? Math.toIntExact(System.currentTimeMillis() - this.widgetplus$openScreenTimestamp) : 0;
    }
}