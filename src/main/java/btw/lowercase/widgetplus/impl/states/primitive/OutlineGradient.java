package btw.lowercase.widgetplus.impl.states.primitive;

import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record OutlineGradient(int startColor, int endColor,
                              Optional<GuiPipelineOverrides> pipelineOverrides) implements PrimitiveType {
    public static final MapCodec<OutlineGradient> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("start_color").forGetter(OutlineGradient::startColor),
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("end_color").forGetter(OutlineGradient::endColor),
            GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(OutlineGradient::pipelineOverrides)
    ).apply(instance, OutlineGradient::new));

    @Override
    public MapCodec<? extends PrimitiveType> type() {
        return MAP_CODEC;
    }
}