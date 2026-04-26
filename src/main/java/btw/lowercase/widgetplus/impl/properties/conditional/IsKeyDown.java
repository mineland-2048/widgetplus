package btw.lowercase.widgetplus.impl.properties.conditional;

import btw.lowercase.widgetplus.impl.property.ConditionalWidgetProperty;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;

public record IsKeyDown(String key) implements ConditionalWidgetProperty {
    public static final MapCodec<IsKeyDown> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("key").forGetter(IsKeyDown::key)
    ).apply(instance, IsKeyDown::new));

    @Override
    public boolean get(final AbstractWidget widget) {
        final InputConstants.Key key = InputConstants.getKey(this.key);
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key.getValue());
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}