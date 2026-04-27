package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.properties.ConditionalWidgetProperties;
import btw.lowercase.widgetplus.impl.properties.conditional.ConditionalWidgetProperty;
import btw.lowercase.widgetplus.impl.properties.conditional.WidgetPropertyTest;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;

public record ConditionalWidgetEntry(WidgetPropertyTest property, WidgetEntry onTrue,
                                     WidgetEntry onFalse) implements WidgetEntry {
    @Override
    public WidgetState resolve(final AbstractWidget widget) {
        return this.property.get(widget) ? this.onTrue.resolve(widget) : this.onFalse.resolve(widget);
    }

    public record Unbaked(ConditionalWidgetProperty property, WidgetEntry.Unbaked onTrue,
                          WidgetEntry.Unbaked onFalse) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ConditionalWidgetProperties.MAP_CODEC.forGetter(Unbaked::property),
                WidgetEntries.CODEC.fieldOf("on_true").forGetter(Unbaked::onTrue),
                WidgetEntries.CODEC.fieldOf("on_false").forGetter(Unbaked::onFalse)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            return new ConditionalWidgetEntry(this.property::get, this.onTrue.bake(), this.onFalse.bake());
        }
    }
}
