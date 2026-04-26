package btw.lowercase.widgetplus;

import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ReloadListener implements ResourceManagerReloadListener {

    private void onReload(ResourceManager resourceManager) {
        WidgetManager widgetManager = WidgetPlus.getWidgetManager();
        widgetManager.clear();

        int total = 0, succesful = 0, error = 0;
        for (Map.Entry<Identifier, Resource> entry : resourceManager.listResources("widgets", Identifier -> Identifier.getPath().endsWith(".json")).entrySet()) {
            {
                total += 1;
                Identifier id = entry.getKey();
                Resource resource = entry.getValue();

                try (InputStream stream = resource.open()) {
                    JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                    var parsed = WidgetDefinition.CODEC.parse(JsonOps.INSTANCE, json);

                    parsed.ifSuccess(widgetDefinition -> widgetManager.register(widgetDefinition.target().type(), id, widgetDefinition))
                            .ifError(widgetDefinitionError -> WidgetPlus.logger.error(widgetDefinitionError.message()));

                    if (parsed.isSuccess()) succesful++;
                    else error++;

//                    WidgetPlus.logger.info("Parsed %s (?)".formatted(id.toString()));
                } catch (Exception e) {
                    WidgetPlus.logger.error("Couldn't parse '" + id + "': \n" + e.getCause());
                    error++;
                }

            }
        }

        WidgetPlus.logger.info("Finished loading %d widgets".formatted(total));
        WidgetPlus.logger.info("%d Parsed jsons, %d Errors.".formatted(succesful, error));
    }
    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        onReload(resourceManager);
    }


    // copy pasted over lol.
    public static void registerReloadListener(ResourceManagerReloadListener listener) {
        ResourceManagerReloadListener idListener = new ResourceManagerReloadListener() {
            @Override
            public @NotNull CompletableFuture<Void> reload(@NotNull SharedState sharedState, @NotNull Executor executor, @NotNull PreparationBarrier preparationBarrier, @NotNull Executor executor2) {
                return mainListener.reload(sharedState,executor,preparationBarrier,executor2);
            }

            @Override
            public void prepareSharedState(@NotNull SharedState sharedState) {
                ResourceManagerReloadListener.super.prepareSharedState(sharedState);
            }

            @Override
            public @NotNull String getName() {
                return ResourceManagerReloadListener.super.getName();
            }

            @Override
            public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {

            }

            private final ResourceManagerReloadListener mainListener = listener;


        };


        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Identifier.fromNamespaceAndPath(WidgetPlus.MOD_ID, "widgets"), idListener);
    }

}
