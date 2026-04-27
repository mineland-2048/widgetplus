package btw.lowercase.widgetplus.impl.properties.range_dispatch;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record WorldClockTime(Optional<Holder<WorldClock>> clockHolder) implements RangeDispatchWidgetProperty {
    public static final MapCodec<WorldClockTime> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            WorldClock.CODEC.optionalFieldOf("clock").forGetter(WorldClockTime::clockHolder)
    ).apply(instance, WorldClockTime::new));

    @Override
    public float get(final AbstractWidget widget) {
        final Level level = Minecraft.getInstance().level;
        if (level != null) {
            return level.clockManager().getTotalTicks(this.clockHolder.orElse(level.registryAccess().getOrThrow(WorldClocks.OVERWORLD)));
        } else {
            return 0.0F;
        }
    }

    @Override
    public MapCodec<? extends RangeDispatchWidgetProperty> type() {
        return MAP_CODEC;
    }
}
