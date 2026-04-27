package btw.lowercase.widgetplus.impl.states;

import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.Nullable;

public class DefaultWidgetEntry implements WidgetEntry {
    public static final DefaultWidgetEntry INSTANCE = new DefaultWidgetEntry();

    @Override
    public @Nullable WidgetState resolve(final AbstractWidget widget) {
        return WidgetState.Default.INSTANCE;
    }

    public record Unbaked() implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            return INSTANCE;
        }
    }
}
