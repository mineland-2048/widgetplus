package btw.lowercase.widgetplus.impl.states.primitive;

import btw.lowercase.widgetplus.impl.util.GuiPipelineOverrides;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record Fill(int color, Optional<GuiPipelineOverrides> pipelineOverrides) implements PrimitiveType {
    public static final MapCodec<Fill> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("color").forGetter(Fill::color),
            GuiPipelineOverrides.CODEC.optionalFieldOf("pipeline_overrides").forGetter(Fill::pipelineOverrides)
    ).apply(instance, Fill::new));

    @Override
    public MapCodec<? extends PrimitiveType> type() {
        return MAP_CODEC;
    }
}
