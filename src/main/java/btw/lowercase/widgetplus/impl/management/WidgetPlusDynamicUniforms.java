package btw.lowercase.widgetplus.impl.management;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import net.minecraft.client.renderer.DynamicUniformStorage;
import org.joml.Vector2d;
import org.jspecify.annotations.NonNull;

import java.nio.ByteBuffer;

public class WidgetPlusDynamicUniforms implements AutoCloseable {
    private static final int UBO_SIZE = (new Std140SizeCalculator()).putVec2().putInt().putInt().get();

    private final DynamicUniformStorage<Data> storage = new DynamicUniformStorage<>("Widget Plus Uniform UBO", UBO_SIZE, 2);

    public GpuBufferSlice write(final Vector2d mousePosition, final int elapsedPauseTime, final int screenOpenTime) {
        return this.storage.writeUniform(new Data(mousePosition, elapsedPauseTime, screenOpenTime));
    }

    public void reset() {
        this.storage.endFrame();
    }

    @Override
    public void close() {
        this.storage.close();
    }

    public record Data(Vector2d mousePosition, int elapsedPauseTime,
                       int screenOpenTime) implements DynamicUniformStorage.DynamicUniform {
        public void write(final @NonNull ByteBuffer buffer) {
            Std140Builder.intoBuffer(buffer)
                    .putVec2((float) this.mousePosition.x, (float) this.mousePosition.y)
                    .putInt(this.elapsedPauseTime)
                    .putInt(this.screenOpenTime);
        }
    }
}