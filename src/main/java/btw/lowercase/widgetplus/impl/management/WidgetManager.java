package btw.lowercase.widgetplus.impl.management;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.states.WidgetEntry;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetManager {
    // A collection of WidgetDefinitions based off their widget type.
    private static class WidgetDefinitionCollection {

        // Should we get rid of this? dont know if we'll need to access it.
        private final Identifier type;

        // Holds the raw definitions by their file path.
        // NOTE: Should we make this have a resource pack prefix to deal with conflicts?
        // ex. minecraft:button, minecraft:multiplayer/disconnect_button
        private final HashMap<Identifier, WidgetDefinition> definitions;

        // HashMap for the hash() lookup.
        // Its a list in case there are multiple definitions.
        private final HashMap<Integer, ArrayList<Identifier>> hashValueLookup;

        // As a definition is registered, the order of lookups will be as is in here.
        private final ArrayList<Identifier> identifiersOrder;

        // Baked definitions here
        private final HashMap<WidgetDefinition, WidgetEntry> bakedEntries;

        public WidgetDefinitionCollection(final Identifier type) {
            this.type = type;
            this.hashValueLookup = new HashMap<>();
            this.definitions = new HashMap<>();
            this.identifiersOrder = new ArrayList<>();
            this.bakedEntries = new HashMap<>();
        }

        // TODO: Deal with duplicates eventualy
        // -mineland 2026-04-26
        public void register(final Identifier id, final WidgetDefinition widgetDefinition) {
            this.definitions.put(id, widgetDefinition);
            if (widgetDefinition.target().hash().isPresent()) {
                int hashValue = widgetDefinition.target().hash().get();
                if (!hashValueLookup.containsKey(hashValue)) {
                    hashValueLookup.put(hashValue, new ArrayList<>());
                }

                ArrayList<Identifier> idList = hashValueLookup.get(hashValue);
                idList.add(id);
            }

            // I dont like this. Maybe a set but i dont know how those work on java
            // -mineland 2026-04-26
            if (!this.identifiersOrder.contains(id)) {
                this.identifiersOrder.add(id);
            }
        }

        // Returns the definition from either the lookup or the first one it matches.
        public WidgetDefinition getDefinition(final int hash_value) {
            // Look over all the hash values first, then the rest of the orders second.
            final ArrayList<Identifier> lookup = new ArrayList<>(hashValueLookup.getOrDefault(hash_value, identifiersOrder));
            lookup.addAll(identifiersOrder);
            for (final Identifier identifier : lookup) {
                final WidgetDefinition result = getDefinition(identifier);
                if (result != null) {
                    return result;
                }
            }

            return null;
        }

        public WidgetDefinition getDefinition(final Identifier id) {
            return definitions.getOrDefault(id, null);
        }

        // This also bakes the models because the render thread is picky on resource loading.
        public WidgetState getState(@NotNull final AbstractWidget widget, final int hashOffset) {
            final int hash_value = widget.hashCode() + hashOffset;
            final ArrayList<Identifier> lookup = new ArrayList<>(hashValueLookup.getOrDefault(hash_value, identifiersOrder));
            lookup.addAll(identifiersOrder);

            for (final Identifier identifier : lookup) {
                final WidgetDefinition definition = getDefinition(identifier);
                if (definition == null) {
                    continue;
                }

                WidgetEntry baked = this.bakedEntries.getOrDefault(definition, null);
                if (baked == null) {
                    baked = definition.widget().bake();
                    this.bakedEntries.put(definition, baked);
                }

                return baked.resolve(widget);
            }
            return null;
        }
    }

    // The identifier is the type here.
    // -mineland 2026-04-26
    private final HashMap<Identifier, WidgetDefinitionCollection> entries = new HashMap<>();

    private WidgetDefinitionCollection getOrSetCollection(final Identifier type) {
        WidgetDefinitionCollection collection;
        if (!this.entries.containsKey(type)) {
            collection = new WidgetDefinitionCollection(type);
            this.entries.put(type, collection);
        } else {
            collection = this.entries.get(type);
        }

        return collection;
    }

    public void register(final Identifier type, final Identifier id, final WidgetDefinition widgetDefinition) {
        this.getOrSetCollection(type).register(id, widgetDefinition);
    }

    public void register(final WidgetDefinition.Type type, final Identifier id, final WidgetDefinition widgetDefinition) {
        this.register(typeIdentifier(type), id, widgetDefinition);
    }

    public WidgetState getState(final Identifier type, final AbstractWidget widget) {
        return this.getState(type, widget, 0);
    }

    public WidgetState getState(final WidgetDefinition.Type type, final AbstractWidget widget, int hashOffset) {
        return this.getState(typeIdentifier(type), widget, hashOffset);
    }

    public WidgetState getState(final WidgetDefinition.Type type, final AbstractWidget widget) {
        return this.getState(type, widget, 0);
    }

    public Identifier typeIdentifier(final WidgetDefinition.Type type) {
        return WidgetPlus.id(type.getSerializedName());
    }

    public WidgetState getState(final Identifier type, final AbstractWidget widget, int hashOffset) {
        final WidgetDefinitionCollection collection = this.entries.getOrDefault(type, null);
        if (collection == null) {
            return null;
        }

        return collection.getState(widget, hashOffset);
    }

    public WidgetDefinition get(final Identifier type, final int hash_value) {
        final WidgetDefinitionCollection collection = entries.getOrDefault(type, null);
        return collection != null ? collection.getDefinition(hash_value) : null;
    }

    public WidgetDefinition get(final Identifier type, final Identifier id) {
        final WidgetDefinitionCollection collection = entries.getOrDefault(type, null);
        return collection != null ? collection.getDefinition(id) : null;

    }

    public void clear() {
        this.entries.clear();
    }
}
