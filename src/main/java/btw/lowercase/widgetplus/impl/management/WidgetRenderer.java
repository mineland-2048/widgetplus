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
            for (final WidgetState innerState : states) {
                renderState(innerState, renderContext, defaultRender);
            }
        } else if (state instanceof WidgetState.Textured(Identifier texture, Optional<RenderPipeline> pipeline)) {
            renderContext.guiGraphics().blitSprite(pipeline.orElse(renderContext.pipeline()), texture, renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), renderContext.color());
        } else if (state instanceof WidgetState.Primitive(PrimitiveType function, Optional<RenderPipeline> pipeline, Optional<Bounds> bounds)) {
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

    public static void renderPrimitive(final PrimitiveType function, final WidgetRenderContext renderContext) {
        if (function instanceof Fill fill) {
            renderContext.guiGraphics().fill(renderContext.pipeline(), renderContext.x(), renderContext.y(), renderContext.x() + renderContext.width(), renderContext.y() + renderContext.height(), fill.color());
        } else if (function instanceof FillGradient fillGradient) {
            renderContext.guiGraphics().innerFill(renderContext.pipeline(), TextureSetup.noTexture(), renderContext.x(), renderContext.y(), renderContext.x() + renderContext.width(), renderContext.y() + renderContext.height(), fillGradient.startColor(), fillGradient.endColor());
        } else if (function instanceof Outline outline) {
            outline(renderContext.guiGraphics(), renderContext.pipeline(), renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), outline.color());
        } else if (function instanceof OutlineGradient outlineGradient) {
            outlineGradient(renderContext.guiGraphics(), renderContext.pipeline(), renderContext.x(), renderContext.y(), renderContext.width(), renderContext.height(), outlineGradient.startColor(), outlineGradient.endColor());
        }
    }

    private static void outline(final GuiGraphicsExtractor guiGraphicsExtractor, final RenderPipeline pipeline, final int x, final int y, final int width, final int height, final int color) {
        guiGraphicsExtractor.fill(pipeline, x, y, x + width, y + 1, color);
        guiGraphicsExtractor.fill(pipeline, x, y + height - 1, x + width, y + height, color);
        guiGraphicsExtractor.fill(pipeline, x, y + 1, x + 1, y + height - 1, color);
        guiGraphicsExtractor.fill(pipeline, x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    private static void outlineGradient(final GuiGraphicsExtractor guiGraphicsExtractor, final RenderPipeline pipeline, final int x, final int y, final int width, final int height, final int startColor, final int endColor) {
        guiGraphicsExtractor.innerFill(pipeline, TextureSetup.noTexture(), x, y, x + width, y + 1, startColor, endColor);
        guiGraphicsExtractor.innerFill(pipeline, TextureSetup.noTexture(), x, y + height - 1, x + width, y + height, startColor, endColor);
        guiGraphicsExtractor.innerFill(pipeline, TextureSetup.noTexture(), x, y + 1, x + 1, y + height - 1, startColor, endColor);
        guiGraphicsExtractor.innerFill(pipeline, TextureSetup.noTexture(), x + width - 1, y + 1, x + width, y + height - 1, startColor, endColor);
    }
}
