package btw.lowercase.widgetplus.impl.management;

import btw.lowercase.widgetplus.impl.util.Bounds;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class WidgetRenderContext {
    private final GuiGraphicsExtractor guiGraphicsExtractor;
    private RenderPipeline pipeline;
    private Identifier location;
    private int x;
    private int y;
    private int width;
    private int height;
    private final int color;

    WidgetRenderContext(
            final GuiGraphicsExtractor guiGraphicsExtractor,
            final RenderPipeline pipeline,
            final Identifier location,
            final int x,
            final int y,
            final int width,
            final int height,
            final int color
    ) {
        this.guiGraphicsExtractor = guiGraphicsExtractor;
        this.pipeline = pipeline;
        this.location = location;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public static WidgetRenderContext of(
            final GuiGraphicsExtractor guiGraphicsExtractor,
            final RenderPipeline pipeline,
            final Identifier location,
            final int x,
            final int y,
            final int width,
            final int height,
            final int color
    ) {
        return new WidgetRenderContext(guiGraphicsExtractor, pipeline, location, x, y, width, height, color);
    }

    public static WidgetRenderContext of(
            final GuiGraphicsExtractor guiGraphicsExtractor,
            final RenderPipeline pipeline,
            final Identifier location,
            final int x,
            final int y,
            final int width,
            final int height
    ) {
        return of(guiGraphicsExtractor, pipeline, location, x, y, width, height, ARGB.white(1.0F));
    }

    public GuiGraphicsExtractor guiGraphics() {
        return this.guiGraphicsExtractor;
    }

    public RenderPipeline pipeline() {
        return this.pipeline;
    }

    public void setPipeline(final RenderPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Identifier location() {
        return this.location;
    }

    public void setLocation(final Identifier location) {
        this.location = location;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public void setBounds(final Bounds bounds) {
        this.x = bounds.getX(this.x);
        this.y = bounds.getY(this.y);
        this.width = bounds.width().orElse(this.width);
        this.height = bounds.height().orElse(this.height);
    }

    public int color() {
        return this.color;
    }
}
