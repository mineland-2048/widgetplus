package btw.lowercase.widgetplus.impl.management;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.WidgetDefinition;
import btw.lowercase.widgetplus.impl.WidgetState;
import btw.lowercase.widgetplus.impl.entries.WidgetEntry;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetManager {
    // The identifier is the type here.
    // -mineland 2026-04-26
    private final HashMap<Identifier, WidgetDefinitionCollection> entries = new HashMap<>();

    public void clear() {
        this.entries.clear();
    }

    private WidgetDefinitionCollection getOrSetCollection(final Identifier type) {
        WidgetDefinitionCollection collection;
        if (!this.entries.containsKey(type)) {
            collection = new WidgetDefinitionCollection();
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

    public WidgetState getState(final WidgetDefinition.Type type, final AbstractWidget widget, final int hash_offset) {
        return this.getState(typeIdentifier(type), widget, hash_offset);
    }

    public WidgetState getState(final WidgetDefinition.Type type, final AbstractWidget widget) {
        return this.getState(type, widget, 0);
    }

    public Identifier typeIdentifier(final WidgetDefinition.Type type) {
        return WidgetPlus.id(type.getSerializedName());
    }

    public WidgetState getState(final Identifier type, final AbstractWidget widget, final int hash_offset) {
        final WidgetDefinitionCollection collection = this.entries.getOrDefault(type, null);
        if (collection != null) {
            return collection.getState(widget, hash_offset);
        } else {
            return WidgetState.Default.INSTANCE;
        }
    }

    public WidgetDefinition get(final Identifier type, final int hash_value) {
        final WidgetDefinitionCollection collection = this.entries.getOrDefault(type, null);
        return collection != null ? collection.getDefinition(hash_value) : null;
    }

    public WidgetDefinition get(final Identifier type, final Identifier id) {
        final WidgetDefinitionCollection collection = this.entries.getOrDefault(type, null);
        return collection != null ? collection.getDefinition(id) : null;
    }

    // A collection of WidgetDefinitions based off their widget type.
    private static class WidgetDefinitionCollection {
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

        public WidgetDefinitionCollection() {
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
                final int hashValue = widgetDefinition.target().hash().get();

                ArrayList<Identifier> list;
                if (!this.hashValueLookup.containsKey(hashValue)) {
                    list = new ArrayList<>();
                    this.hashValueLookup.put(hashValue, list);
                } else {
                    list = this.hashValueLookup.get(hashValue);
                }

                list.add(id);
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
            final ArrayList<Identifier> lookup = new ArrayList<>(this.hashValueLookup.getOrDefault(hash_value, this.identifiersOrder));
            lookup.addAll(this.identifiersOrder);
            for (final Identifier identifier : lookup) {
                final WidgetDefinition result = this.getDefinition(identifier);
                if (result != null) {
                    return result;
                }
            }

            return null;
        }

        public WidgetDefinition getDefinition(final Identifier id) {
            return this.definitions.getOrDefault(id, null);
        }

        public WidgetState getState(@NotNull final AbstractWidget widget, final int hash_offset) {
            final int hash_value = widget.hashCode() + hash_offset;

            final ArrayList<Identifier> lookup = new ArrayList<>(this.hashValueLookup.getOrDefault(hash_value, this.identifiersOrder));
            lookup.addAll(this.identifiersOrder);
            for (final Identifier identifier : lookup) {
                final WidgetDefinition definition = this.getDefinition(identifier);
                if (definition == null) {
                    continue;
                }

                WidgetEntry baked = this.bakedEntries.getOrDefault(definition, null);
                if (baked == null) {
                    baked = definition.widget().bake();
                    this.bakedEntries.put(definition, baked);
                }

                final WidgetState state = baked.resolve(widget);
                if (state instanceof WidgetState.Fallback) {
                    continue;
                }

                return state;
            }

            return WidgetState.Default.INSTANCE;
        }
    }
}
