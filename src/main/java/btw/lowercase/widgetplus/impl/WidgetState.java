package btw.lowercase.widgetplus.impl;

import btw.lowercase.widgetplus.impl.states.primitive.PrimitiveFunction;
import btw.lowercase.widgetplus.impl.util.Bounds;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public interface WidgetState {
    record Multiple(List<WidgetState> states) implements WidgetState {
    }

    record Textured(Identifier texture, Optional<RenderPipeline> pipeline) implements WidgetState {
    }

    record Primitive(PrimitiveFunction function, Optional<RenderPipeline> pipeline,
                     Optional<Bounds> bounds) implements WidgetState {
    }

    record Custom(WidgetState state, Optional<Bounds> bounds) implements WidgetState {
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