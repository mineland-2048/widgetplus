package btw.lowercase.widgetplus.impl.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.multiplayer.ServerData;
import org.jspecify.annotations.NonNull;

public record ServerAddress() implements SelectWidgetProperty<String> {
    public static final SelectWidgetProperty.Type<ServerAddress, String> TYPE = SelectWidgetProperty.Type.create(
            MapCodec.unit(new ServerAddress()), Codec.STRING
    );

    @Override
    public @NonNull String get(final AbstractWidget widget) {
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isSingleplayer()) {
            return "singleplayer";
        } else {
            final ServerData serverData = minecraft.getCurrentServer();
            return serverData != null ? serverData.ip : "";
        }
    }

    @Override
    public Codec<String> valueCodec() {
        return Codec.STRING;
    }

    @Override
    public Type<? extends SelectWidgetProperty<String>, String> type() {
        return TYPE;
    }
}