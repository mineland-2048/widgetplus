package btw.lowercase.widgetplus.mixin.components;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(EditBox.class)
public abstract class MixinEditBox extends AbstractWidget {
    public MixinEditBox(final int x, final int y, final int width, final int height, final Component message) {
        super(x, y, width, height, message);
    }

    @WrapOperation(method = "extractWidgetRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void widgetplus$blitEditBox(final GuiGraphicsExtractor instance, final RenderPipeline renderPipeline, final Identifier location, final int x, final int y, final int width, final int height, final Operation<Void> original) {
        if (WidgetPlusConfig.instance().enabled) {
            final WidgetState state = WidgetPlus.getWidgetManager().getState(WidgetDefinition.Type.EDIT_BOX, this);
            if (state instanceof WidgetState.Textured(Identifier texture, Optional<RenderPipeline> pipeline)) {
                original.call(instance, pipeline.orElse(renderPipeline), texture, x, y, width, height);
                return;
            }

            if (state instanceof WidgetState.Empty) {
                return;
            }
        }

        original.call(instance, renderPipeline, location, x, y, width, height);
    }
}
