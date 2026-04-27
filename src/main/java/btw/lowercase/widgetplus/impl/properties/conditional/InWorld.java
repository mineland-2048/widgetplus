package btw.lowercase.widgetplus.impl.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;

public record InWorld() implements ConditionalWidgetProperty {
    public static final MapCodec<InWorld> MAP_CODEC = MapCodec.unit(new InWorld());

    @Override
    public boolean get(final AbstractWidget widget) {
        return Minecraft.getInstance().level != null;
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}
