package btw.lowercase.widgetplus.mixin;

import btw.lowercase.widgetplus.impl.WidgetEntries;
import btw.lowercase.widgetplus.impl.properties.ConditionalWidgetProperties;
import btw.lowercase.widgetplus.impl.properties.RangeDispatchWidgetProperties;
import btw.lowercase.widgetplus.impl.properties.SelectWidgetProperties;
import net.minecraft.client.ClientBootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientBootstrap.class)
public abstract class MixinClientBootstrap {
    @Inject(method = "bootstrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/properties/numeric/RangeSelectItemModelProperties;bootstrap()V", shift = At.Shift.AFTER))
    private static void widgetplus$bootstrap(final CallbackInfo ci) {
        WidgetEntries.bootstrap();
        SelectWidgetProperties.bootstrap();
        ConditionalWidgetProperties.bootstrap();
        RangeDispatchWidgetProperties.bootstrap();
    }
}
