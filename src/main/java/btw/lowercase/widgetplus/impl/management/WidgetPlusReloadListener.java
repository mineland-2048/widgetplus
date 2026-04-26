package btw.lowercase.widgetplus.impl.management;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import org.jspecify.annotations.NonNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class WidgetPlusReloadListener implements PreparableReloadListener {
    @Override
    public @NonNull CompletableFuture<Void> reload(final @NonNull SharedState sharedState, final @NonNull Executor taskExecutor, final PreparationBarrier preparationBarrier, final @NonNull Executor reloadExecutor) {
        return CompletableFuture.runAsync(() -> {
            final WidgetManager widgetManager = WidgetPlus.getWidgetManager();
            widgetManager.clear();

            int total = 0;
            final AtomicInteger successful = new AtomicInteger();
            final AtomicInteger error = new AtomicInteger();
            for (Map.Entry<Identifier, Resource> entry : sharedState.resourceManager().listResources("widgets", Identifier -> Identifier.getPath().endsWith(".json")).entrySet()) {
                {
                    total += 1;

                    final Identifier id = entry.getKey();
                    final Resource resource = entry.getValue();
                    try (final InputStream stream = resource.open()) {
                        final JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                        WidgetDefinition.CODEC.parse(JsonOps.INSTANCE, json).ifSuccess(widgetDefinition -> {
                            widgetManager.register(widgetDefinition.target().type(), id, widgetDefinition);
                            successful.getAndIncrement();
                        }).ifError(widgetDefinitionError -> {
                            WidgetPlus.getLogger().error(widgetDefinitionError.message());
                            error.getAndIncrement();
                        });

                    } catch (Exception e) {
                        WidgetPlus.getLogger().error("Couldn't parse '{}': \n{}", id, e.getCause());
                        error.getAndIncrement();
                    }
                }
            }

            WidgetPlus.getLogger().info("Finished loading {} widgets", total);
            WidgetPlus.getLogger().info("{} Parsed jsons, {} Errors.", successful, error);
        }).thenCompose(preparationBarrier::wait);
    }
}
