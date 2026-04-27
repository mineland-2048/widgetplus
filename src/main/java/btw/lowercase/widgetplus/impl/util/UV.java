package btw.lowercase.widgetplus.impl.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record UV(float u0, float v0, float u1, float v1) {
    public static final Codec<UV> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("u0").forGetter(UV::u0),
            Codec.FLOAT.fieldOf("v0").forGetter(UV::v0),
            Codec.FLOAT.fieldOf("u1").forGetter(UV::u1),
            Codec.FLOAT.fieldOf("v1").forGetter(UV::v1)
    ).apply(instance, UV::new));
}