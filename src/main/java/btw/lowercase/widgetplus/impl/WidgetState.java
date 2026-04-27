package btw.lowercase.widgetplus.impl;

import btw.lowercase.widgetplus.impl.states.CustomWidgetEntry;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public interface WidgetState {
    record Textured(Identifier texture, Optional<RenderPipeline> pipeline) implements WidgetState {
    }

    record Multiple(List<WidgetState> states) implements WidgetState {
    }

    record Custom(WidgetState state, Optional<CustomWidgetEntry.Bounds> bounds) implements WidgetState {
    }

    record Empty() implements WidgetState {
        public static final Empty INSTANCE = new Empty();
    }

    record Fallback() implements WidgetState {
        public static final Fallback INSTANCE = new Fallback();
    }

    record Default(Optional<RenderPipeline> pipeline) implements WidgetState {
        public static final Default INSTANCE = new Default(Optional.empty());
    }
}