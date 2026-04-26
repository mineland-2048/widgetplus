package btw.lowercase.widgetplus.impl.properties;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.properties.range_dispatch.SliderValue;
import btw.lowercase.widgetplus.impl.property.RangeDispatchWidgetProperty;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class RangeDispatchWidgetProperties {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends RangeDispatchWidgetProperty>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final MapCodec<RangeDispatchWidgetProperty> MAP_CODEC = ID_MAPPER
            .codec(Identifier.CODEC)
            .dispatchMap("property", RangeDispatchWidgetProperty::type, c -> c);

    public static void bootstrap() {
        ID_MAPPER.put(WidgetPlus.id("slider/value"), SliderValue.MAP_CODEC);
    }
}
