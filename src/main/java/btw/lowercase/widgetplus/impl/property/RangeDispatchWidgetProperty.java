package btw.lowercase.widgetplus.impl.property;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;

public interface RangeDispatchWidgetProperty {
    float get(final AbstractWidget widget);

    MapCodec<? extends RangeDispatchWidgetProperty> type();
}
