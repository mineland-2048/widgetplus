package btw.lowercase.widgetplus.impl.states;

import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record TextureWidgetEntry(Identifier texture,
                                 Optional<RenderPipeline> pipeline) implements WidgetEntry {
    @Override
    public WidgetState resolve(final AbstractWidget widget) {
        return new WidgetState.Textured(this.texture, this.pipeline);
    }

    public record Unbaked(Identifier texture,
                          Optional<GuiPipelineOverrides> pipelineOverrides) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture),
                GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(Unbaked::pipelineOverrides)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.registerAndLoad(this.texture, new SimpleTexture(this.texture));

            final RenderPipeline.Builder builder = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET);
            builder.withLocation("pipeline/dynamic_widget_" + this.texture.hashCode());
            this.pipelineOverrides.ifPresent(overrides -> overrides.apply(builder));

            return new TextureWidgetEntry(this.texture, Optional.of(builder.build()));
        }
    }
}