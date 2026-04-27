package btw.lowercase.widgetplus.impl.properties.conditional;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;

public record IsKeyDown(InputConstants.Key key) implements ConditionalWidgetProperty {
    private static final Codec<InputConstants.Key> KEY_CODEC = Codec.STRING.comapFlatMap(key -> {
        try {
            return DataResult.success(InputConstants.getKey(key));
        } catch (final Throwable throwable) {
            return DataResult.error(() -> "Invalid key: " + key);
        }
    }, InputConstants.Key::getName);

    public static final MapCodec<IsKeyDown> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            KEY_CODEC.fieldOf("key").forGetter(IsKeyDown::key)
    ).apply(instance, IsKeyDown::new));

    @Override
    public boolean get(final AbstractWidget widget) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), this.key.getValue());
    }

    @Override
    public MapCodec<? extends ConditionalWidgetProperty> type() {
        return MAP_CODEC;
    }
}