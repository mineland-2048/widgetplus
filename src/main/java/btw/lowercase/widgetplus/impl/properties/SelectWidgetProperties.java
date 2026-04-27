package btw.lowercase.widgetplus.impl.properties;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.properties.select.*;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class SelectWidgetProperties {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, SelectWidgetProperty.Type<?, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<SelectWidgetProperty.Type<?, ?>> CODEC = ID_MAPPER.codec(Identifier.CODEC);

    public static void bootstrap() {
        ID_MAPPER.put(WidgetPlus.id("message"), Message.TYPE);
        ID_MAPPER.put(WidgetPlus.id("graphics_preset"), VanillaGraphicsPreset.TYPE);
        ID_MAPPER.put(WidgetPlus.id("server/address"), ServerAddress.TYPE);
        ID_MAPPER.put(WidgetPlus.id("server/motd"), ServerMOTD.TYPE);
        ID_MAPPER.put(WidgetPlus.id("world/dimension"), Dimension.TYPE);
        ID_MAPPER.put(WidgetPlus.id("world/weather"), Weather.TYPE);
    }
}