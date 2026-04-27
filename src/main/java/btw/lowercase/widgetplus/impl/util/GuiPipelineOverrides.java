package btw.lowercase.widgetplus.impl.util;

import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class GuiPipelineOverrides {
    public static final Codec<GuiPipelineOverrides> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("vertex").forGetter(overrides -> overrides.vertexShader),
            Identifier.CODEC.optionalFieldOf("fragment").forGetter(overrides -> overrides.fragmentShader),
            Utils.COLOR_TARGET_STATE_CODEC.optionalFieldOf("color_target").forGetter(overrides -> overrides.colorTargetState)
    ).apply(instance, GuiPipelineOverrides::new));

    private final Optional<Identifier> vertexShader;
    private final Optional<Identifier> fragmentShader;
    private final Optional<ColorTargetState> colorTargetState;

    GuiPipelineOverrides(final Optional<Identifier> vertexShader, final Optional<Identifier> fragmentShader, final Optional<ColorTargetState> colorTargetState) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.colorTargetState = colorTargetState;
    }

    public void apply(final RenderPipeline.Builder builder) {
        this.vertexShader.ifPresent(builder::withVertexShader);
        this.fragmentShader.ifPresent(builder::withFragmentShader);
        this.colorTargetState.ifPresent(builder::withColorTargetState);
    }
}
