package btw.lowercase.widgetplus.impl.properties.conditional;

import btw.lowercase.widgetplus.impl.property.ConditionalWidgetProperty;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;

public record HoveredOrFocused() implements ConditionalWidgetProperty {
    public static final MapCodec<HoveredOrFocused> MAP_CODEC = MapCodec.unit(new HoveredOrFocused());

    @Override
    public boolean get(final AbstractWidget widget) {
        return widget.isHoveredOrFocused();
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}
