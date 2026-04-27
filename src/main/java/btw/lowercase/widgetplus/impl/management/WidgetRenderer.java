package btw.lowercase.widgetplus.impl.management;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.states.primitive.*;
import btw.lowercase.widgetplus.impl.util.Bounds;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class WidgetRenderer {
    public static void renderDefinition(final WidgetDefinition.Type type, final AbstractWidget widget, final WidgetRenderContext renderContext, final Consumer<WidgetRenderContext> defaultRender) {
        renderState(WidgetPlus.getWidgetManager().getState(type, widget), renderContext, defaultRender);
    }

    public static void renderState(final WidgetState state, final WidgetRenderContext renderContext, final Consumer<WidgetRenderContext> defaultRender) {
        if (state instanceof WidgetState.Multiple(List<WidgetState> states)) {
            states.forEach(it -> renderState(it, renderContext, defaultRender));
        } else if (state instanceof WidgetState.Textured(Identifier texture, Optional<RenderPipeline> pipeline)) {
            renderContext.guiGraphics().blitSprite(pipeline.orElse(renderContext.pipeline()), texture, renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), renderContext.color());
        } else if (state instanceof WidgetState.Primitive(
                PrimitiveFunction function, Optional<RenderPipeline> pipeline, Optional<Bounds> bounds
        )) {
            pipeline.ifPresentOrElse(renderContext::setPipeline, () -> renderContext.setPipeline(RenderPipelines.GUI));
            bounds.ifPresent(renderContext::setBounds);
            renderPrimitive(function, renderContext);
        } else if (state instanceof WidgetState.Custom(WidgetState customState, Optional<Bounds> bounds)) {
            bounds.ifPresent(renderContext::setBounds);
            renderState(customState, renderContext, defaultRender);
        } else if (state instanceof WidgetState.Default(Optional<RenderPipeline> pipeline)) {
            pipeline.ifPresent(renderContext::setPipeline);
            defaultRender.accept(renderContext);
        }
    }

    public static void renderPrimitive(final PrimitiveFunction function, final WidgetRenderContext renderContext) {
        switch (function) {
            case Fill fill ->
                    renderContext.guiGraphics().fill(renderContext.pipeline(), renderContext.x(), renderContext.y(), renderContext.x() + renderContext.width(), renderContext.y() + renderContext.height(), fill.color());
            case FillGradient fillGradient ->
                    renderContext.guiGraphics().innerFill(renderContext.pipeline(), TextureSetup.noTexture(), renderContext.x(), renderContext.y(), renderContext.x() + renderContext.width(), renderContext.y() + renderContext.height(), fillGradient.startColor(), fillGradient.endColor());
            case Outline outline ->
                    outline(renderContext.guiGraphics(), renderContext.pipeline(), renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), outline.color());
            case OutlineGradient outlineGradient ->
                    outlineGradient(renderContext.guiGraphics(), renderContext.pipeline(), renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), outlineGradient.startColor(), outlineGradient.endColor());
            case null, default ->
                    throw new RuntimeException("TODO: Implement primitive rendering for type: " + function);
        }
    }

    private static void outline(final GuiGraphicsExtractor guiGraphics, final RenderPipeline pipeline, final int x, final int y, final int width, final int height, final int color) {
        guiGraphics.fill(pipeline, x, y, x + width, y + 1, color);
        guiGraphics.fill(pipeline, x, y + height - 1, x + width, y + height, color);
        guiGraphics.fill(pipeline, x, y + 1, x + 1, y + height - 1, color);
        guiGraphics.fill(pipeline, x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    private static void outlineGradient(final GuiGraphicsExtractor guiGraphics, final RenderPipeline pipeline, final int x, final int y, final int width, final int height, final int startColor, final int endColor) {
        guiGraphics.innerFill(pipeline, TextureSetup.noTexture(), x, y, x + width, y + 1, startColor, endColor);
        guiGraphics.innerFill(pipeline, TextureSetup.noTexture(), x, y + height - 1, x + width, y + height, startColor, endColor);
        guiGraphics.innerFill(pipeline, TextureSetup.noTexture(), x, y + 1, x + 1, y + height - 1, startColor, endColor);
        guiGraphics.innerFill(pipeline, TextureSetup.noTexture(), x + width - 1, y + 1, x + width, y + height - 1, startColor, endColor);
    }
}
