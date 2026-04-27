package btw.lowercase.widgetplus.impl.properties;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.properties.select.Dimension;
import btw.lowercase.widgetplus.impl.properties.select.VanillaGraphicsPreset;
import btw.lowercase.widgetplus.impl.properties.select.Weather;
import btw.lowercase.widgetplus.impl.property.SelectWidgetProperty;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class SelectWidgetProperties {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, SelectWidgetProperty.Type<?, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<SelectWidgetProperty.Type<?, ?>> CODEC = ID_MAPPER.codec(Identifier.CODEC);

    public static void bootstrap() {
        ID_MAPPER.put(WidgetPlus.id("graphics_preset"), VanillaGraphicsPreset.TYPE);
        ID_MAPPER.put(WidgetPlus.id("world/dimension"), Dimension.TYPE);
        ID_MAPPER.put(WidgetPlus.id("world/weather"), Weather.TYPE);
    }
}