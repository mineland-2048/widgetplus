package btw.lowercase.widgetplus;

import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class WidgetPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WidgetPlusConfig.load();
        ReloadListener.registerReloadListener(new ReloadListener());
        registerTestPack();

    }

    private void registerTestPack() {
        WidgetPlus.logger.info("Registering test pack");
        FabricLoader.getInstance().getModContainer(WidgetPlus.MOD_ID).ifPresent(container -> {
            Identifier packId = Identifier.fromNamespaceAndPath(WidgetPlus.MOD_ID, "test_pack");
            boolean worked = ResourceLoader.registerBuiltinPack(packId, container, PackActivationType.NORMAL);

            WidgetPlus.logger.info("Result: " + (worked ? "yes" : "no"));
        });

    }
}
