package btw.lowercase.widgetplus.mixin;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetLocations;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractButton.class)
public abstract class MixinAbstractButton extends AbstractWidget.WithInactiveMessage {
    public MixinAbstractButton(final int x, final int y, final int width, final int height, final Component message) {
        super(x, y, width, height, message);
    }

    @WrapOperation(method = "extractDefaultSprite", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"))
    private void widgetplus$blitButton(final GuiGraphicsExtractor instance, final RenderPipeline renderPipeline, final Identifier location, final int x, final int y, final int width, final int height, final int color, final Operation<Void> original) {
        if (WidgetPlusConfig.instance().enabled) {
            final WidgetDefinition entry = WidgetPlus.getWidgetManager().getWidgetByHashOrId(this.hashCode(), WidgetLocations.BUTTON);
            final WidgetState state = entry.widget().bake().resolve(this);
            if (state != null) {
                RenderPipeline pipeline = renderPipeline;
                if (state.pipelineOverrides().isPresent()) {
                    final RenderPipeline.Builder builder = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET);
                    builder.withLocation("pipeline/dynamic_button_" + this.hashCode());
                    state.pipelineOverrides().get().apply(builder);
                    pipeline = builder.build();
                }

                original.call(instance, pipeline, state.texture(), x, y, width, height, color);
            } else {
                return; // Empty
            }
        }

        original.call(instance, renderPipeline, location, x, y, width, height, color);
    }
}