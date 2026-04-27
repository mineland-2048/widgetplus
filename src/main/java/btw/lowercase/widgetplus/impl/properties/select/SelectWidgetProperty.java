package btw.lowercase.widgetplus.impl.properties.select;

import btw.lowercase.widgetplus.impl.entries.SelectWidgetEntry;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface SelectWidgetProperty<T> {
    @Nullable T get(final AbstractWidget widget);

    Codec<T> valueCodec();

    SelectWidgetProperty.Type<? extends SelectWidgetProperty<T>, T> type();

    record Type<P extends SelectWidgetProperty<T>, T>(MapCodec<SelectWidgetEntry.UnbakedSwitch<P, T>> switchCodec) {
        public static <P extends SelectWidgetProperty<T>, T> SelectWidgetProperty.Type<P, T> create(final MapCodec<P> propertyMapCodec, final Codec<T> valueCodec) {
            return new SelectWidgetProperty.Type<>(RecordCodecBuilder.mapCodec(instance -> instance.group(
                    propertyMapCodec.forGetter(SelectWidgetEntry.UnbakedSwitch::property),
                    createCasesFieldCodec(valueCodec).forGetter(SelectWidgetEntry.UnbakedSwitch::cases)
            ).apply(instance, SelectWidgetEntry.UnbakedSwitch::new)));
        }

        public static <T> MapCodec<List<SelectWidgetEntry.SwitchCase<T>>> createCasesFieldCodec(final Codec<T> valueCodec) {
            return SelectWidgetEntry.SwitchCase.codec(valueCodec).listOf().validate(SelectWidgetProperty.Type::validateCases).fieldOf("cases");
        }

        private static <T> DataResult<List<SelectWidgetEntry.SwitchCase<T>>> validateCases(final List<SelectWidgetEntry.SwitchCase<T>> cases) {
            if (cases.isEmpty()) {
                return DataResult.error(() -> "Empty case list");
            } else {
                final Multiset<T> counts = HashMultiset.create();
                for (final SelectWidgetEntry.SwitchCase<T> caze : cases) {
                    counts.addAll(caze.values());
                }

                return counts.size() != counts.entrySet().size() ? DataResult.error(() -> {
                    final Stream<String> var10000 = counts.entrySet().stream().filter(entry -> entry.getCount() > 1).map(entry -> entry.getElement().toString());
                    return "Duplicate case conditions: " + var10000.collect(Collectors.joining(", "));
                }) : DataResult.success(cases);
            }
        }
    }
}
