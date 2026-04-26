package btw.lowercase.widgetplus.impl;

import btw.lowercase.widgetplus.WidgetPlus;
import btw.lowercase.widgetplus.impl.states.EmptyWidgetEntry;
import btw.lowercase.widgetplus.impl.states.TextureWidgetEntry;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

// TODO
public class WidgetManager {

    // A collection of WidgetDefinitions based off their widget type.
    private class WidgetDefinitionCollection {
        private Identifier type;

        // Holds the raw definitions by their file path.
        // NOTE: Should we make this have a resource pack prefix to deal with conflicts?
        // ex. minecraft:button, minecraft:multiplayer/disconnect_button
        private HashMap<Identifier, WidgetDefinition> definitions;

        // hash map for the hash() lookup.
        // Its a list in case there are multiple definitions.
        private HashMap<Integer, ArrayList<Identifier>> hashValueLookup;

        // As a def is registered, the order of lookups will be as is in here.
        private ArrayList<Identifier> identifiersOrder;

        public WidgetDefinitionCollection(Identifier type) {
            this.hashValueLookup = new HashMap<>();
            this.definitions = new HashMap<>();
            this.identifiersOrder = new ArrayList<>();
            this.type = type;
        }

        public void register(Identifier id, int hash_value, WidgetDefinition widgetDefinition) {
            this.register(id, widgetDefinition);
            ArrayList<Identifier> arr = hashValueLookup.getOrDefault(hash_value, new ArrayList<>());
            arr.add(id);
            hashValueLookup.putIfAbsent(hash_value, arr);
        }

        // TODO: Deal with duplicates eventualy
        // -mineland 2026-04-26
        public void register(Identifier id, WidgetDefinition widgetDefinition) {
            definitions.put(id, widgetDefinition);

            // I dont like this. Maybe a set but i dont know how those work on java
            // -mineland 2026-04-26
            if (!identifiersOrder.contains(id)) {
                identifiersOrder.add(id);
            }
        }


        // Returns the definition from either the lookup or the first one it matches.
        // TODO: Make these have properties
        // -mineland 2026-04-26
        public WidgetDefinition get(int hash_value) {
            // Look over all the hash values first, then the rest of the orders second.
            ArrayList<Identifier> lookup = new ArrayList<>(hashValueLookup.getOrDefault(hash_value, identifiersOrder));
            lookup.addAll(identifiersOrder);

            for (int i = 0; i < lookup.size(); i++) {
                WidgetDefinition result = get(lookup.get(i));
                if (result != null) {
                    return result;
                }
            }
            return null;
        }


        public WidgetDefinition get(Identifier id) {
            WidgetDefinition definition = definitions.getOrDefault(id, null);
            // TODO: Do the actual condition checks.
//            if (definition == null) {
//                return null;
//            }
            return definition;
        }


        public WidgetState getState(@NotNull AbstractWidget widget, int hashOffset) {
            ArrayList<Identifier> lookup = new ArrayList<>(hashValueLookup.getOrDefault(widget.hashCode(), identifiersOrder));
            lookup.addAll(identifiersOrder);

            for (int i = 0; i < lookup.size(); i++) {
                WidgetDefinition result = get(lookup.get(i));
                if (result == null) {
                    continue;
                }

                // Should this be cached somewhere? I feel like it should.
                var baked = result.widget().bake().resolve(widget);
                if (baked == null) {
                    continue;
                }
                return baked;
            }
            return null;
        }


        public WidgetState getState(AbstractWidget widget) {
            return getState(widget, 0);
        }
    }

    // The identifier is the type here.
    // -mineland 2026-04-26
    private final HashMap<Identifier, WidgetDefinitionCollection> entries = new HashMap<>();
//    private final HashMap<Identifier, WidgetDefinition> entries = new HashMap<>();


    private WidgetDefinitionCollection getOrSetCollection(final Identifier type) {
        WidgetDefinitionCollection collection;

        if (!entries.containsKey(type)) {
            collection = new WidgetDefinitionCollection(type);
            entries.put(type, collection);
        } else {
            collection = entries.get(type);
        }

        return collection;
    }

    public void register(final Identifier type, final Identifier id, final int hashValue, final WidgetDefinition widgetDefinition) {
        getOrSetCollection(type).register(id, hashValue, widgetDefinition);
    }

    public void register(final Identifier type, final Identifier id, final WidgetDefinition widgetDefinition) {
        getOrSetCollection(type).register(id, widgetDefinition);
    }

    public void register(final WidgetDefinition.Type type, final Identifier id, final WidgetDefinition widgetDefinition) {
        register(typeIdentifier(type), id, widgetDefinition);
    }

    public void register(final WidgetDefinition.Type type, final Identifier id, final int hashValue, final WidgetDefinition widgetDefinition) {
        register(typeIdentifier(type), id, hashValue, widgetDefinition);
    }


    public WidgetState getState(final Identifier type, final AbstractWidget widget) {
        return getState(type, widget, 0);
    }

    public WidgetState getState(final WidgetDefinition.Type type, final AbstractWidget widget) {
        return getState(typeIdentifier(type), widget);
    }

    public Identifier typeIdentifier(WidgetDefinition.Type type) {
        return Identifier.fromNamespaceAndPath(WidgetPlus.MOD_ID, type.getSerializedName());
    }

    public WidgetState getState(final Identifier type, final AbstractWidget widget, int hashOffset) {
        WidgetDefinitionCollection collection = entries.getOrDefault(type, null);

        if (collection == null) {
            return null;
        }
        return collection.getState(widget, hashOffset);
    }

//    public WidgetDefinition register
    // TODO: make this have arguments for conditionals
    public WidgetDefinition get(final Identifier type, final int hash_value) {
        WidgetDefinition result;
        WidgetDefinitionCollection collection = entries.getOrDefault(type, null);

        if (collection == null) {
            return null;
        }
        result = collection.get(hash_value);
        return result;
    }

    public WidgetDefinition get(final Identifier type, final Identifier id) {
        WidgetDefinition result;
        WidgetDefinitionCollection collection = entries.getOrDefault(type, null);

        if (collection == null) {
            return null;
        }

        result = collection.get(id);
        return result;

    }

    public void clear() {
        entries.clear();
    }


    // TODO/NOTE: For widgets that extend other widgets and aren't defined manually (hack)
    // Looks for file named by hashCode of the widget first before looking for a legit id file/parent file
//    public WidgetDefinition getWidgetByHashOrId(final int hashCode, final Identifier id) {
//        if (WidgetLocations.BUTTON.equals(id)) {
//            return new WidgetDefinition(
//                    new WidgetDefinition.Target(WidgetDefinition.Type.BUTTON, Optional.empty()),
//                    new TextureWidgetEntry.Unbaked(
//                            Identifier.withDefaultNamespace("widget/button"),
//                            Optional.of(new GuiPipelineOverrides(
//                                    Optional.empty(),
//                                    Optional.empty(),
//                                    Optional.of(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA))
//                            ))
//                    )
//            );
//        }
//
//        return entries.getOrDefault(WidgetPlus.id("" + hashCode), entries.getOrDefault(id, new WidgetDefinition(new WidgetDefinition.Target(WidgetDefinition.Type.BUTTON, Optional.empty()), new EmptyWidgetEntry.Unbaked())));
//    }
}
