package btw.lowercase.widgetplus.mixin.components;

import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractScrollArea.class)
public abstract class MixinAbstractScrollArea extends AbstractWidget {
    public MixinAbstractScrollArea(final int x, final int y, final int width, final int height, final Component message) {
        super(x, y, width, height, message);
    }

    // TODO
}
