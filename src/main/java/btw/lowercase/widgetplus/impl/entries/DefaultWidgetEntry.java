package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.RenderPipelines;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record DefaultWidgetEntry(Optional<RenderPipeline> pipeline) implements WidgetEntry {
    @Override
    public @NonNull WidgetState resolve(final AbstractWidget widget) {
        return new WidgetState.Default(this.pipeline);
    }

    public record Unbaked(Optional<GuiPipelineOverrides> pipelineOverrides) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(Unbaked::pipelineOverrides)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            final RenderPipeline.Builder builder = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET);
            builder.withLocation("pipeline/dynamic_widget_" + this.hashCode());
            this.pipelineOverrides.ifPresent(overrides -> overrides.apply(builder));
            return new DefaultWidgetEntry(Optional.of(builder.build()));
        }
    }
}
