package btw.lowercase.widgetplus.impl.base;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.property.SelectWidgetProperty;
import btw.lowercase.widgetplus.impl.states.WidgetEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public class SelectWidget {
    public record UnbakedSwitch<P extends SelectWidgetProperty<T>, T>(P property,
                                                                      List<SelectWidget.SwitchCase<T>> cases) {
    }

    public record SwitchCase<T>(List<T> values, WidgetEntry.Unbaked widget) {
        public static <T> Codec<SelectWidget.SwitchCase<T>> codec(final Codec<T> valueCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(valueCodec)).fieldOf("when").forGetter(SelectWidget.SwitchCase::values),
                    WidgetEntries.CODEC.fieldOf("widget").forGetter(SelectWidget.SwitchCase::widget)
            ).apply(instance, SelectWidget.SwitchCase::new));
        }
    }
}
