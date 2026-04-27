package btw.lowercase.widgetplus.impl.entries.primitive;

import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record FillGradient(int startColor, int endColor,
                           Optional<GuiPipelineOverrides> pipelineOverrides) implements PrimitiveFunction {
    public static final MapCodec<FillGradient> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("start_color").forGetter(FillGradient::startColor),
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("end_color").forGetter(FillGradient::endColor),
            GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(FillGradient::pipelineOverrides)
    ).apply(instance, FillGradient::new));

    @Override
    public MapCodec<? extends PrimitiveFunction> type() {
        return MAP_CODEC;
    }
}