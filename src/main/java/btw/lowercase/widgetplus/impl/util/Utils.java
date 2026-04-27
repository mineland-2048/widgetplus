package btw.lowercase.widgetplus.impl.util;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public final class Utils {
    public static final Codec<BlendFunction> BLEND_FUNCTION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("src_color").forGetter(blend -> blend.sourceColor().toString()),
            Codec.STRING.fieldOf("dest_color").forGetter(blend -> blend.destColor().toString()),
            Codec.STRING.optionalFieldOf("src_alpha").forGetter(blend -> Optional.of(blend.sourceAlpha().toString())),
            Codec.STRING.optionalFieldOf("dest_alpha").forGetter(blend -> Optional.of(blend.destAlpha().toString()))
    ).apply(instance, (srcColor, dstColor, srcAlpha, dstAlpha) -> new BlendFunction(
            sourceFactorOf(srcColor),
            destFactorOf(dstColor),
            sourceFactorOf(srcAlpha.orElse(srcColor)),
            destFactorOf(dstAlpha.orElse(dstColor))
    )));

    public static final Codec<ColorTargetState> COLOR_TARGET_STATE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BLEND_FUNCTION_CODEC.optionalFieldOf("blend").forGetter(ColorTargetState::blendFunction),
            Codec.INT.orElse(ColorTargetState.WRITE_ALL).fieldOf("write_mask").forGetter(ColorTargetState::writeMask)
    ).apply(instance, ColorTargetState::new));

    private static SourceFactor sourceFactorOf(final String name) {
        return switch (name) {
            case "constant_alpha" -> SourceFactor.CONSTANT_ALPHA;
            case "constant_color" -> SourceFactor.CONSTANT_COLOR;
            case "dst_alpha" -> SourceFactor.DST_ALPHA;
            case "dst_color" -> SourceFactor.DST_COLOR;
            case "one" -> SourceFactor.ONE;
            case "one_minus_constant_alpha" -> SourceFactor.ONE_MINUS_CONSTANT_ALPHA;
            case "one_minus_constant_color" -> SourceFactor.ONE_MINUS_CONSTANT_COLOR;
            case "one_minus_dst_alpha" -> SourceFactor.ONE_MINUS_DST_ALPHA;
            case "one_minus_dst_color" -> SourceFactor.ONE_MINUS_DST_COLOR;
            case "one_minus_src_alpha" -> SourceFactor.ONE_MINUS_SRC_ALPHA;
            case "one_minus_src_color" -> SourceFactor.ONE_MINUS_SRC_COLOR;
            case "src_alpha" -> SourceFactor.SRC_ALPHA;
            case "src_alpha_saturate" -> SourceFactor.SRC_ALPHA_SATURATE;
            case "src_color" -> SourceFactor.SRC_COLOR;
            case "zero" -> SourceFactor.ZERO;
            default -> SourceFactor.ONE; // OpenGL default
        };
    }

    private static DestFactor destFactorOf(final String name) {
        return switch (name) {
            case "constant_alpha" -> DestFactor.CONSTANT_ALPHA;
            case "constant_color" -> DestFactor.CONSTANT_COLOR;
            case "dst_alpha" -> DestFactor.DST_ALPHA;
            case "dst_color" -> DestFactor.DST_COLOR;
            case "one" -> DestFactor.ONE;
            case "one_minus_constant_alpha" -> DestFactor.ONE_MINUS_CONSTANT_ALPHA;
            case "one_minus_constant_color" -> DestFactor.ONE_MINUS_CONSTANT_COLOR;
            case "one_minus_dst_alpha" -> DestFactor.ONE_MINUS_DST_ALPHA;
            case "one_minus_dst_color" -> DestFactor.ONE_MINUS_DST_COLOR;
            case "one_minus_src_alpha" -> DestFactor.ONE_MINUS_SRC_ALPHA;
            case "one_minus_src_color" -> DestFactor.ONE_MINUS_SRC_COLOR;
            case "src_alpha" -> DestFactor.SRC_ALPHA;
            case "src_color" -> DestFactor.SRC_COLOR;
            case "zero" -> DestFactor.ZERO;
            default -> DestFactor.ZERO; // OpenGL default
        };
    }
}
