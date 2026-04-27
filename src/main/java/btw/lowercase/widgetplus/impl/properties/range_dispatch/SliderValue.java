package btw.lowercase.widgetplus.impl.properties.range_dispatch;

import btw.lowercase.widgetplus.mixin.components.AbstractSliderButtonAccessor;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;

public record SliderValue() implements RangeDispatchWidgetProperty {
    public static final MapCodec<SliderValue> MAP_CODEC = MapCodec.unit(new SliderValue());

    @Override
    public float get(final AbstractWidget widget) {
        if (widget instanceof AbstractSliderButton abstractSliderButton) {
            return (float) ((AbstractSliderButtonAccessor) abstractSliderButton).widgetplus$getValue();
        } else {
            return 0.0F;
        }
    }

    @Override
    public MapCodec<? extends RangeDispatchWidgetProperty> type() {
        return MAP_CODEC;
    }
}
