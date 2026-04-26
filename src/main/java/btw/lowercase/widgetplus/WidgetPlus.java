package btw.lowercase.widgetplus;

import btw.lowercase.widgetplus.impl.management.WidgetManager;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WidgetPlus {
    // TODO: Move these two out of here into the build script.
    // -mineland 2026-04-26
    public static final String MOD_ID = "widgetplus";
    public static final String VERSION = "1.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(final String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private static final WidgetManager widgetManager = new WidgetManager();

    public static WidgetManager getWidgetManager() {
        return widgetManager;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
