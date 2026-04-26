package btw.lowercase.widgetplus.impl.properties;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.properties.conditional.Disabled;
import btw.lowercase.widgetplus.impl.properties.conditional.Focused;
import btw.lowercase.widgetplus.impl.properties.conditional.Hovered;
import btw.lowercase.widgetplus.impl.properties.conditional.IsKeybindDown;
import btw.lowercase.widgetplus.impl.property.ConditionalWidgetProperty;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class ConditionalWidgetProperties {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ConditionalWidgetProperty>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper<>();
    public static final MapCodec<ConditionalWidgetProperty> MAP_CODEC = ID_MAPPER
            .codec(Identifier.CODEC)
            .dispatchMap("property", ConditionalWidgetProperty::type, c -> c);

    public static void bootstrap() {
        ID_MAPPER.put(WidgetPlus.id("hovered"), Hovered.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("focused"), Focused.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("disabled"), Disabled.MAP_CODEC);
        ID_MAPPER.put(WidgetPlus.id("keybind_down"), IsKeybindDown.MAP_CODEC);
    }
}