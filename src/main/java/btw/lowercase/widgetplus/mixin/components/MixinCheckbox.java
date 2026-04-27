package btw.lowercase.widgetplus.mixin.components;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Checkbox.class)
public abstract class MixinCheckbox extends AbstractButton {
    public MixinCheckbox(final int x, final int y, final int width, final int height, final Component message) {
        super(x, y, width, height, message);
    }

    @WrapOperation(method = "extractContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"))
    private void widgetplus$blitCheckbox(final GuiGraphicsExtractor instance, final RenderPipeline renderPipeline, final Identifier location, final int x, final int y, final int width, final int height, final int color, final Operation<Void> original) {
        if (WidgetPlusConfig.instance().enabled) {
            final WidgetState state = WidgetPlus.getWidgetManager().getState(WidgetDefinition.Type.CHECKBOX, this);
            if (state instanceof WidgetState.Textured(Identifier texture, Optional<RenderPipeline> pipeline)) {
                original.call(instance, pipeline.orElse(renderPipeline), texture, x, y, width, height, color);
                return;
            }

            if (state instanceof WidgetState.Empty) {
                return;
            }
        }

        original.call(instance, renderPipeline, location, x, y, width, height, color);
    }
}
