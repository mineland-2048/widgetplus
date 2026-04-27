package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.Nullable;

public interface WidgetEntry {
    @Nullable WidgetState resolve(final AbstractWidget widget);

    interface Unbaked {
        MapCodec<? extends WidgetEntry.Unbaked> type();

        WidgetEntry bake();
    }
}