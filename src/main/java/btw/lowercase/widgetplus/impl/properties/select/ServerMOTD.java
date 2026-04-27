package btw.lowercase.widgetplus.impl.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jspecify.annotations.NonNull;

public record ServerMOTD() implements SelectWidgetProperty<Component> {
    public static final SelectWidgetProperty.Type<ServerMOTD, Component> TYPE = SelectWidgetProperty.Type.create(
            MapCodec.unit(new ServerMOTD()), ComponentSerialization.CODEC
    );

    @Override
    public @NonNull Component get(final AbstractWidget widget) {
        final Minecraft minecraft = Minecraft.getInstance();
        final ServerData serverData = minecraft.getCurrentServer();
        return !minecraft.isSingleplayer() && serverData != null ? serverData.motd : Component.empty();
    }

    @Override
    public Codec<Component> valueCodec() {
        return ComponentSerialization.CODEC;
    }

    @Override
    public Type<? extends SelectWidgetProperty<Component>, Component> type() {
        return TYPE;
    }
}
