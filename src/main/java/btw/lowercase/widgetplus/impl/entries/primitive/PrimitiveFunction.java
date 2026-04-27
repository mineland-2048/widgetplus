package btw.lowercase.widgetplus.impl.entries.primitive;

import com.mojang.serialization.MapCodec;

public interface PrimitiveFunction {
    MapCodec<? extends PrimitiveFunction> type();
}