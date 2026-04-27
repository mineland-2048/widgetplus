package btw.lowercase.widgetplus.impl.management;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import java.util.List;
import java.util.Optional;

public final class WidgetRenderer {
    public static void render(final WidgetDefinition.Type type, final AbstractWidget widget, final BlitRenderContext renderContext, final Runnable defaultRender) {
        final WidgetState state = WidgetPlus.getWidgetManager().getState(type, widget);
        if (state instanceof WidgetState.Multiple(List<WidgetState> states)) {
            for (final WidgetState innerState : states) {
                render(innerState, renderContext, defaultRender);
            }
        } else {
            render(state, renderContext, defaultRender);
        }
    }

    public static void render(final WidgetState state, final BlitRenderContext renderContext, final Runnable defaultRender) {
        if (state instanceof WidgetState.Textured(Identifier texture, Optional<RenderPipeline> pipeline)) {
            renderContext.guiGraphicsExtractor.blitSprite(pipeline.orElse(renderContext.pipeline), texture, renderContext.x, renderContext.y, renderContext.width, renderContext.height, renderContext.color);
            return;
        }

        if (state instanceof WidgetState.Default) {
            defaultRender.run();
        }
    }

    public record BlitRenderContext(GuiGraphicsExtractor guiGraphicsExtractor,
                                    RenderPipeline pipeline,
                                    Identifier location,
                                    int x, int y,
                                    int width, int height,
                                    int color) {
        public BlitRenderContext(
                final GuiGraphicsExtractor guiGraphicsExtractor,
                final RenderPipeline pipeline,
                final Identifier location,
                final int x,
                final int y,
                final int width,
                final int height
        ) {
            this(guiGraphicsExtractor, pipeline, location, x, y, width, height, ARGB.white(1.0F));
        }
    }
}
