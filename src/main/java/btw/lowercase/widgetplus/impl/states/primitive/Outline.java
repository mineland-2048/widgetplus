package btw.lowercase.widgetplus.impl.states.primitive;

import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record Outline(int color, Optional<GuiPipelineOverrides> pipelineOverrides) implements PrimitiveFunction {
    public static final MapCodec<Outline> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("color").forGetter(Outline::color),
            GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(Outline::pipelineOverrides)
    ).apply(instance, Outline::new));

    @Override
    public MapCodec<? extends PrimitiveFunction> type() {
        return MAP_CODEC;
    }
}