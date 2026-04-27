package btw.lowercase.widgetplus.impl;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public interface WidgetState {
    record Textured(Identifier texture, Optional<RenderPipeline> pipeline) implements WidgetState {
    }

    record Multiple(List<WidgetState> states) implements WidgetState {
    }

    record Empty() implements WidgetState {
        public static final Empty INSTANCE = new Empty();
    }

    record Default() implements WidgetState {
        public static final Default INSTANCE = new Default();
    }
}