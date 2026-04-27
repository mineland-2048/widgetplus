package btw.lowercase.widgetplus.impl.states;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.Optional;

public record CustomWidgetEntry(WidgetEntry entry, Optional<Bounds> bounds) implements WidgetEntry {
    @Override
    public WidgetState resolve(final AbstractWidget widget) {
        return new WidgetState.Custom(this.entry.resolve(widget), this.bounds);
    }

    public record Unbaked(WidgetEntry.Unbaked widget, Optional<Bounds> bounds) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                WidgetEntries.CODEC.fieldOf("widget").forGetter(Unbaked::widget),
                Bounds.CODEC.optionalFieldOf("bounds").forGetter(Unbaked::bounds)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            return new CustomWidgetEntry(this.widget.bake(), this.bounds);
        }
    }

    public record Bounds(Optional<Integer> x, Optional<Integer> y, Optional<Integer> width, Optional<Integer> height) {
        public static Codec<Bounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("x").forGetter(Bounds::x),
                Codec.INT.optionalFieldOf("y").forGetter(Bounds::y),
                Codec.INT.optionalFieldOf("width").forGetter(Bounds::width),
                Codec.INT.optionalFieldOf("height").forGetter(Bounds::height)
        ).apply(instance, Bounds::new));
    }
}
