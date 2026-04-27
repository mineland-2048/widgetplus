package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.NonNull;

public class EmptyWidgetEntry implements WidgetEntry {
    public static final EmptyWidgetEntry INSTANCE = new EmptyWidgetEntry();

    @Override
    public @NonNull WidgetState resolve(final AbstractWidget widget) {
        return WidgetState.Empty.INSTANCE;
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