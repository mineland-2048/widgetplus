package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.NonNull;

public class FallbackWidgetEntry implements WidgetEntry {
    public static final FallbackWidgetEntry INSTANCE = new FallbackWidgetEntry();

    @Override
    public @NonNull WidgetState resolve(final AbstractWidget widget) {
        return WidgetState.Fallback.INSTANCE;
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
