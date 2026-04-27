package btw.lowercase.widgetplus.impl;

import btw.lowercase.widgetplus.impl.entries.WidgetEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record WidgetDefinition(Target target, WidgetEntry.Unbaked widget) {
    public static final Codec<WidgetDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Target.MAP_CODEC.fieldOf("target").forGetter(WidgetDefinition::target),
            WidgetEntries.CODEC.fieldOf("widget").forGetter(WidgetDefinition::widget)
    ).apply(instance, WidgetDefinition::new));

    public record Target(Type type, Optional<Integer> hash) {
        public static final MapCodec<Target> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Type.CODEC.fieldOf("type").forGetter(Target::type),
                Codec.INT.optionalFieldOf("hash_code").forGetter(Target::hash)
        ).apply(instance, Target::new));
    }

    public enum Type implements StringRepresentable {
        BUTTON,
        SLIDER,
        SLIDER_HANDLE,
        SCROLLBAR,
        SCROLLBAR_KNOB,
        EDIT_BOX,
        CHECKBOX;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        @Override
        public @NonNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
