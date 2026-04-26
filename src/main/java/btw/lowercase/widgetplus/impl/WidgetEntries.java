package btw.lowercase.widgetplus.impl;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.states.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class WidgetEntries {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends WidgetEntry.Unbaked>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<WidgetEntry.Unbaked> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(WidgetEntry.Unbaked::type, c -> c);

    public static void bootstrap() {
        ID_MAPPER.put(WidgetPlus.id("empty"), EmptyWidgetEntry.Unbaked.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("texture"), TextureWidgetEntry.Unbaked.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("range_dispatch"), RangeDispatchWidgetEntry.Unbaked.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("select"), SelectWidgetEntry.Unbaked.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("condition"), ConditionalWidgetEntry.Unbaked.MAP_CODEC);
    }
}
