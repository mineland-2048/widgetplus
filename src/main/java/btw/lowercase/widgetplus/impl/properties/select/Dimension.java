package btw.lowercase.widgetplus.impl.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public record Dimension() implements SelectWidgetProperty<ResourceKey<Level>> {
    public static final Codec<ResourceKey<Level>> VALUE_CODEC = Level.RESOURCE_KEY_CODEC;
    public static final SelectWidgetProperty.Type<Dimension, ResourceKey<Level>> TYPE = SelectWidgetProperty.Type.create(
            MapCodec.unit(new Dimension()), VALUE_CODEC
    );

    @Override
    public @Nullable ResourceKey<Level> get(final AbstractWidget widget) {
        final Level level = Minecraft.getInstance().level;
        if (level != null) {
            return level.dimension();
        } else {
            return null;
        }
    }

    @Override
    public Codec<ResourceKey<Level>> valueCodec() {
        return VALUE_CODEC;
    }

    @Override
    public Type<? extends SelectWidgetProperty<ResourceKey<Level>>, ResourceKey<Level>> type() {
        return TYPE;
    }
}
