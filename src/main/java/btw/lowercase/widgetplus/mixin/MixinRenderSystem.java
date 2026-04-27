package btw.lowercase.widgetplus.mixin;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import btw.lowercase.widgetplus.impl.management.WidgetPlusDynamicUniforms;
import btw.lowercase.widgetplus.impl.util.ElapsedTimes;
import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicUniforms;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {
    @Unique
    private static GpuBufferSlice widgetplus$dynamicUniforms;

    @Inject(method = "initRenderer", at = @At("TAIL"))
    private static void widgetplus$initUniforms(final GpuDevice device, final CallbackInfo ci) {
        WidgetPlus.initUniform();
    }

    @Inject(method = "flipFrame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DynamicUniforms;reset()V", shift = At.Shift.AFTER))
    private static void widgetplus$resetUniforms(final TracyFrameCapture tracyFrameCapture, final CallbackInfo ci) {
        final WidgetPlusDynamicUniforms dynamicUniforms = WidgetPlus.dynamicUniforms();
        if (WidgetPlusConfig.instance().enabled && dynamicUniforms != null) {
            dynamicUniforms.reset();
        }
    }

    @Inject(method = "getDynamicUniforms", at = @At("HEAD"))
    private static void widgetplus$setupUbo(final CallbackInfoReturnable<DynamicUniforms> cir) {
        final WidgetPlusDynamicUniforms dynamicUniforms = WidgetPlus.dynamicUniforms();
        if (WidgetPlusConfig.instance().enabled && dynamicUniforms != null) {
            final Minecraft minecraft = Minecraft.getInstance();
            final double mouseX = minecraft.mouseHandler.xpos();
            final double mouseY = minecraft.mouseHandler.ypos();

            final ElapsedTimes elapsedTimes = ((ElapsedTimes) minecraft);
            widgetplus$dynamicUniforms = dynamicUniforms.write(
                    new Vector2d(mouseX, mouseY),
                    elapsedTimes.widgetplus$getElapsedPauseTime(),
                    elapsedTimes.widgetplus$getElapsedOpenScreenTime()
            );
        }
    }

    @Inject(method = "bindDefaultUniforms", at = @At("TAIL"))
    private static void widgetplus$bindCustomUniform(final RenderPass renderPass, final CallbackInfo ci) {
        if (WidgetPlusConfig.instance().enabled && widgetplus$dynamicUniforms != null) {
            renderPass.setUniform("WPData", widgetplus$dynamicUniforms);
        }
    }
}
