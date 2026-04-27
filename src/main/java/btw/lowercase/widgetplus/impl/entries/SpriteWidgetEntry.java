package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record SpriteWidgetEntry(Identifier sprite,
                                Optional<RenderPipeline> pipeline) implements WidgetEntry {
    @Override
    public WidgetState resolve(final AbstractWidget widget) {
        return new WidgetState.Sprite(this.sprite, this.pipeline);
    }

    public record Unbaked(Identifier sprite,
                          Optional<GuiPipelineOverrides> pipelineOverrides) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("sprite").forGetter(Unbaked::sprite),
                GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(Unbaked::pipelineOverrides)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            final RenderPipeline.Builder builder = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET);
            builder.withLocation("pipeline/dynamic_widget_" + this.sprite.hashCode());
            this.pipelineOverrides.ifPresent(overrides -> overrides.apply(builder));
            return new SpriteWidgetEntry(this.sprite, Optional.of(builder.build()));
        }
    }
}