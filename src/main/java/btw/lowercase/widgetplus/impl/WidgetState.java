package btw.lowercase.widgetplus.impl;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public interface WidgetState {
    record Textured(Identifier texture, Optional<RenderPipeline> pipeline) implements WidgetState {
        public Textured(final Identifier texture) {
            this(texture, Optional.empty());
        }
    }

    record Default() implements WidgetState {
        public static final Default INSTANCE = new Default();
    }

    record Empty() implements WidgetState {
        public static final Empty INSTANCE = new Empty();
    }
}