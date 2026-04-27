package btw.lowercase.widgetplus.impl.states;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record CompositeWidgetEntry(List<WidgetEntry> widgets) implements WidgetEntry {
    @Override
    public @NonNull WidgetState resolve(final AbstractWidget widget) {
        final List<WidgetState> states = new ArrayList<>();
        for (final WidgetEntry entry : this.widgets) {
            states.add(entry.resolve(widget));
        }

        return new WidgetState.Multiple(states);
    }

    public record Unbaked(List<WidgetEntry.Unbaked> widgets) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                WidgetEntries.CODEC.listOf().fieldOf("widgets").forGetter(Unbaked::widgets)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            if (!this.widgets.isEmpty()) {
                return this.widgets.size() == 1 ? this.widgets.getFirst().bake() : new CompositeWidgetEntry(this.widgets.stream().map(WidgetEntry.Unbaked::bake).toList());
            } else {
                return EmptyWidgetEntry.INSTANCE;
            }
        }
    }
}

