package btw.lowercase.widgetplus.mixin.components;

import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.management.WidgetRenderContext;
import btw.lowercase.widgetplus.impl.management.WidgetRenderer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(AbstractButton.class)
public abstract class MixinAbstractButton extends AbstractWidget.WithInactiveMessage {
    public MixinAbstractButton(final int x, final int y, final int width, final int height, final Component message) {
        super(x, y, width, height, message);
    }

    @WrapOperation(method = "extractDefaultSprite", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"))
    private void widgetplus$blitButton(final GuiGraphicsExtractor instance, final RenderPipeline renderPipeline, final Identifier location, final int x, final int y, final int width, final int height, final int color, final Operation<Void> original) {
        final WidgetRenderContext widgetRenderContext = WidgetRenderContext.of(instance, renderPipeline, location, x, y, width, height, color);
        final Consumer<WidgetRenderContext> defaultRender = (renderContext) -> original.call(renderContext.guiGraphics(), renderContext.pipeline(), renderContext.location(), renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), renderContext.color());
        if (WidgetPlusConfig.instance().enabled) {
            WidgetRenderer.renderDefinition(WidgetDefinition.Type.BUTTON, this, widgetRenderContext, defaultRender);
        } else {
            defaultRender.accept(widgetRenderContext);
        }
    }
}