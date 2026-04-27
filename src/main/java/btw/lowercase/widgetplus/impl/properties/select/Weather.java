package btw.lowercase.widgetplus.impl.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record Weather() implements SelectWidgetProperty<Weather.Type> {
    public static final SelectWidgetProperty.Type<Weather, Weather.Type> TYPE = SelectWidgetProperty.Type.create(
            MapCodec.unit(new Weather()), Type.CODEC
    );

    @Override
    public @Nullable Type get(final AbstractWidget widget) {
        final Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (!level.isRaining() && !level.isThundering()) {
                return Type.CLEAR;
            }

            if (level.isRaining() && !level.isThundering()) {
                return Type.RAIN;
            }

            if (level.isThundering()) {
                return Type.THUNDER;
            }
        }

        return null;
    }

    @Override
    public Codec<Type> valueCodec() {
        return Type.CODEC;
    }

    @Override
    public SelectWidgetProperty.Type<? extends SelectWidgetProperty<Type>, Type> type() {
        return TYPE;
    }

    public enum Type implements StringRepresentable {
        CLEAR,
        RAIN,
        THUNDER;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        @Override
        public @NonNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}