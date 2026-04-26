package btw.lowercase.widgetplus.impl.property;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;

public interface ConditionalWidgetProperty {
    boolean get(final AbstractWidget widget);

    MapCodec<? extends ConditionalWidgetProperty> type();
}