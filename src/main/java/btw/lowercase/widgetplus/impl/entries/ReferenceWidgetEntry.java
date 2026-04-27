package btw.lowercase.widgetplus.impl.entries;

import btw.lowercase.widgetplus.impl.WidgetState;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record ReferenceWidgetEntry(Identifier id) implements WidgetEntry {
    @Override
    public @NonNull WidgetState resolve(final AbstractWidget widget) {
        return new WidgetState.Reference(this.id);
    }

    public record Unbaked(Identifier id) implements WidgetEntry.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(Unbaked::id)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public WidgetEntry bake() {
            return new ReferenceWidgetEntry(this.id);
        }
    }
}
