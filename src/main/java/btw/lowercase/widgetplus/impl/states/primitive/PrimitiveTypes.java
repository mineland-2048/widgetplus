package btw.lowercase.widgetplus.impl.states.primitive;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;

public class PrimitiveTypes {
    private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends PrimitiveFunction>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<PrimitiveFunction> CODEC = ID_MAPPER.codec(Codec.STRING).dispatch(PrimitiveFunction::type, c -> c);

    public static void bootstrap() {
        ID_MAPPER.put("fill", Fill.MAP_CODEC);
        ID_MAPPER.put("fill_gradient", FillGradient.MAP_CODEC);
        ID_MAPPER.put("outline", Outline.MAP_CODEC);
        ID_MAPPER.put("outline_gradient", OutlineGradient.MAP_CODEC);
    }
}