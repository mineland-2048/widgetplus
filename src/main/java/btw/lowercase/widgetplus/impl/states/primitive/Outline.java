package btw.lowercase.widgetplus.impl.states.primitive;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record Outline(int color) implements PrimitiveType {
    public static final MapCodec<Outline> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.STRING_ARGB_COLOR.fieldOf("color").forGetter(Outline::color)
    ).apply(instance, Outline::new));

    @Override
    public MapCodec<? extends PrimitiveType> type() {
        return MAP_CODEC;
    }
}