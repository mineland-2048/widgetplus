package btw.lowercase.widgetplus;

import btw.lowercase.widgetplus.config.WidgetPlusConfig;
import btw.lowercase.widgetplus.impl.management.WidgetPlusReloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public final class WidgetPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WidgetPlusConfig.load();
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(WidgetPlus.id("widgets"), new WidgetPlusReloadListener());
        this.registerTestPack();
    }

    private void registerTestPack() {
        WidgetPlus.getLogger().info("Registering test pack");
        FabricLoader.getInstance().getModContainer(WidgetPlus.MOD_ID).ifPresent(container -> {
            final Identifier packId = Identifier.fromNamespaceAndPath(WidgetPlus.MOD_ID, "test_pack");
            final boolean worked = ResourceLoader.registerBuiltinPack(packId, container, PackActivationType.NORMAL);
            WidgetPlus.getLogger().info("Result: " + (worked ? "yes" : "no"));
        });
    }
}
