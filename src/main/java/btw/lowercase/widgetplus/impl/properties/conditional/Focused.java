package btw.lowercase.widgetplus.impl.properties.conditional;

import btw.lowercase.widgetplus.impl.property.ConditionalWidgetProperty;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;

public record Focused() implements ConditionalWidgetProperty {
    public static final MapCodec<Focused> MAP_CODEC = MapCodec.unit(new Focused());

    @Override
    public boolean get(final AbstractWidget widget) {
        return widget.isFocused();
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}
