package btw.lowercase.widgetplus.impl.states.primitive;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record Fill(int color) implements PrimitiveType {
    public static final MapCodec<Fill> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("color").forGetter(Fill::color)
    ).apply(instance, Fill::new));

    @Override
    public MapCodec<? extends PrimitiveType> type() {
        return MAP_CODEC;
    }
}
