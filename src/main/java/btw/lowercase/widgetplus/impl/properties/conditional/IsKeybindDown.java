package btw.lowercase.widgetplus.impl.properties.conditional;

import btw.lowercase.widgetplus.impl.property.ConditionalWidgetProperty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.AbstractWidget;

public record IsKeybindDown(String keybind) implements ConditionalWidgetProperty {
    public static final MapCodec<IsKeybindDown> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("keybind").forGetter(IsKeybindDown::keybind)
    ).apply(instance, IsKeybindDown::new));

    @Override
    public boolean get(final AbstractWidget widget) {
        final KeyMapping keyMapping = KeyMapping.get(this.keybind);
        return keyMapping != null && keyMapping.isDown();
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}
