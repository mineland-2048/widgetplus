package btw.lowercase.widgetplus.impl.states;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.properties.SelectWidgetProperties;
import btw.lowercase.widgetplus.impl.property.SelectWidgetProperty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.ExtraCodecs;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record SelectWidgetEntry<T>(SelectWidgetProperty<T> property, EntrySelector<T> selector) implements WidgetEntry {
    @Override
    public @Nullable WidgetState resolve(final AbstractWidget widget) {
        final T value = this.property.get();
        return this.selector.get(value).resolve(widget);
    }

    public interface EntrySelector<T> {
        WidgetEntry get(@Nullable final T value);
    }

    public record SwitchCase<T>(List<T> values, WidgetEntry.Unbaked widget) {
        public static <T> Codec<SwitchCase<T>> codec(final Codec<T> valueCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(valueCodec)).fieldOf("when").forGetter(SwitchCase::values),
                    WidgetEntries.CODEC.fieldOf("widget").forGetter(SwitchCase::widget)
            ).apply(instance, SwitchCase::new));
        }
    }

    public record Unbaked(UnbakedSwitch<?, ?> unbakedSwitch,
                          WidgetEntry.Unbaked fallback) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                UnbakedSwitch.MAP_CODEC.forGetter(Unbaked::unbakedSwitch),
                WidgetEntries.CODEC.fieldOf("fallback").forGetter(Unbaked::fallback)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            return this.unbakedSwitch.bake(this.fallback.bake());
        }
    }

    public record UnbakedSwitch<P extends SelectWidgetProperty<T>, T>(P property, List<SwitchCase<T>> cases) {
        public static final MapCodec<UnbakedSwitch<?, ?>> MAP_CODEC = SelectWidgetProperties.CODEC.dispatchMap(
                "property",
                unbaked -> unbaked.property().type(),
                SelectWidgetProperty.Type::switchCodec
        );

        public WidgetEntry bake(final WidgetEntry fallback) {
            final Object2ObjectMap<T, WidgetEntry> bakedEntries = new Object2ObjectOpenHashMap<>();
            for (SwitchCase<T> switchCase : this.cases) {
                final WidgetEntry entry = switchCase.widget.bake();
                for (final T value : switchCase.values) {
                    bakedEntries.put(value, entry);
                }
            }

            bakedEntries.defaultReturnValue(fallback);
            return new SelectWidgetEntry<>(this.property, this.createModelGetter(bakedEntries));
        }

        private EntrySelector<T> createModelGetter(final Object2ObjectMap<T, WidgetEntry> originalEntries) {
            final WidgetEntry defaultEntry = originalEntries.defaultReturnValue();

            final Object2ObjectMap<T, WidgetEntry> remappedModels = new Object2ObjectOpenHashMap<>(originalEntries.size());
            remappedModels.defaultReturnValue(defaultEntry);
            originalEntries.forEach((value, model) -> {
                final DataResult<T> dataResult = this.property.valueCodec().encodeStart(JsonOps.INSTANCE, value).flatMap(element -> this.property.valueCodec().parse(JsonOps.INSTANCE, element));
                dataResult.ifSuccess(it -> remappedModels.put(it, model));
            });

            return (value) -> value == null ? defaultEntry : (WidgetEntry) remappedModels.get(value);
        }
    }
}