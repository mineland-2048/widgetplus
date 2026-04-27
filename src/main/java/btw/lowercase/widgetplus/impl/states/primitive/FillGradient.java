package btw.lowercase.widgetplus.impl.states.primitive;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record FillGradient(int startColor, int endColor) implements PrimitiveType {
    public static final MapCodec<FillGradient> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("start_color").forGetter(FillGradient::startColor),
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("end_color").forGetter(FillGradient::endColor)
    ).apply(instance, FillGradient::new));

    @Override
    public MapCodec<? extends PrimitiveType> type() {
        return MAP_CODEC;
    }
}