package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.properties.RangeDispatchWidgetProperties;
import btw.lowercase.widgetplus.impl.properties.range_dispatch.RangeDispatchWidgetProperty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public record RangeDispatchWidgetEntry(RangeDispatchWidgetProperty property, float scale, float[] thresholds,
                                       WidgetEntry[] entries, WidgetEntry fallback) implements WidgetEntry {
    @Override
    public @Nullable WidgetState resolve(final AbstractWidget widget) {
        final float value = this.property.get(widget) * this.scale;

        WidgetEntry entry;
        if (Float.isNaN(value)) {
            entry = this.fallback;
        } else {
            final int index = lastIndexLessOrEqual(this.thresholds, value);
            entry = index == -1 ? this.fallback : this.entries[index];
        }

        return entry.resolve(widget);
    }

    private static int lastIndexLessOrEqual(float[] haystack, float needle) {
        if (haystack.length < 16) {
            for (int i = 0; i < haystack.length; i++) {
                if (haystack[i] > needle) {
                    return i - 1;
                }
            }

            return haystack.length - 1;
        } else {
            int index = Arrays.binarySearch(haystack, needle);
            if (index < 0) {
                int insertionPoint = ~index;
                return insertionPoint - 1;
            } else {
                return index;
            }
        }
    }

    public record Unbaked(RangeDispatchWidgetProperty property,
                          float scale,
                          List<Entry> entries,
                          WidgetEntry.Unbaked fallback) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                RangeDispatchWidgetProperties.MAP_CODEC.forGetter(Unbaked::property),
                Codec.FLOAT.optionalFieldOf("scale", 1.0F).forGetter(Unbaked::scale),
                Entry.CODEC.listOf().fieldOf("entries").forGetter(Unbaked::entries),
                WidgetEntries.CODEC.fieldOf("fallback").forGetter(Unbaked::fallback)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            final float[] thresholds = new float[this.entries.size()];
            final WidgetEntry[] entries = new WidgetEntry[this.entries.size()];

            final List<Entry> mutableEntries = new ArrayList<>(this.entries);
            mutableEntries.sort(Entry.BY_THRESHOLD);
            for (int i = 0; i < mutableEntries.size(); i++) {
                final Entry entry = mutableEntries.get(i);
                thresholds[i] = entry.threshold;
                entries[i] = entry.widget.bake();
            }

            return new RangeDispatchWidgetEntry(this.property, this.scale, thresholds, entries, this.fallback.bake());
        }
    }

    public record Entry(float threshold, WidgetEntry.Unbaked widget) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.FLOAT.fieldOf("threshold").forGetter(Entry::threshold),
                        WidgetEntries.CODEC.fieldOf("widget").forGetter(Entry::widget)
                ).apply(instance, Entry::new)
        );

        public static final Comparator<Entry> BY_THRESHOLD = Comparator.comparingDouble(Entry::threshold);
    }
}
