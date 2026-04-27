package btw.lowercase.widgetplus.impl.states.primitive;

import com.mojang.serialization.MapCodec;

public interface PrimitiveFunction {
    MapCodec<? extends PrimitiveFunction> type();
}