package btw.lowercase.widgetplus.impl.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;

public record Hovered() implements ConditionalWidgetProperty {
    public static final MapCodec<Hovered> MAP_CODEC = MapCodec.unit(new Hovered());

    @Override
    public boolean get(final AbstractWidget widget) {
        return widget.isHovered();
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}