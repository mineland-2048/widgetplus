package btw.lowercase.widgetplus.impl.properties.conditional;

import com.mojang.serialization.MapCodec;

public interface ConditionalWidgetProperty extends WidgetPropertyTest {
    MapCodec<? extends ConditionalWidgetProperty> type();
}