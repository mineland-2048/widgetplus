package btw.lowercase.widgetplus.impl.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;

public record Disabled() implements ConditionalWidgetProperty {
    public static final MapCodec<Disabled> MAP_CODEC = MapCodec.unit(new Disabled());

    @Override
    public boolean get(final AbstractWidget widget) {
        return !widget.isActive();
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}
