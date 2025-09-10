/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge;

import net.minecraft.client.render.platform.MemoryTracker;
import net.minecraft.client.render.vertex.Tesselator;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static java.lang.Math.min;
import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GLContext.*;

/**
 * Replaces the vanilla tessellator
 * for Eloraam's "Multiple-tessellator texture renderer"
 * rather than patching it.
 *
 * @author halotroop2288
 * @author Eloraam
 */
public class ForgeTesselator extends Tesselator {
	protected static final int nativeBufferSize = 0x200000;
	protected static final int trivertsInBuffer = (nativeBufferSize / 0x30) * 6;

	protected final static boolean     sharedUseVBO      = getCapabilities().GL_ARB_vertex_buffer_object;
	protected final static ByteBuffer  sharedByteBuffer  = MemoryTracker.createByteBuffer(nativeBufferSize * 4);
	protected final static IntBuffer   sharedIntBuffer   = sharedByteBuffer.asIntBuffer();
	protected final static FloatBuffer sharedFloatBuffer = sharedByteBuffer.asFloatBuffer();
	@Nullable
	protected final static IntBuffer   sharedVertexBuffers;
	protected final static int         sharedVboCount    = 10;

	protected int vboIndex;
	protected int vertexBufferSize;

	static {
		if (sharedUseVBO) {
			sharedVertexBuffers = MemoryTracker.createIntBuffer(sharedVboCount);
			glGenBuffersARB(sharedVertexBuffers);
		} else {
			sharedVertexBuffers = null;
		}
	}

	public ForgeTesselator(int bufferSize) {
		super(bufferSize);
		this.vertexBuffer = null;
		this.vertexBufferSize = 0;
	}

	@Override
	public void vertex(double x, double y, double z) {
		if (index >= vertexBufferSize - 32) {
			if (vertexBufferSize == 0) {
				vertexBufferSize = 0x10000;
				vertexBuffer = new int[vertexBufferSize];
			} else {
				vertexBufferSize *= 2;
				vertexBuffer = Arrays.copyOf(vertexBuffer, vertexBufferSize);
			}
		}
		super.vertex(x, y, z);
	}

	@Override
	public void end() {
		if (!this.tesselating) throw new IllegalStateException("Not tessellating!");

		this.tesselating = false;
		int offs = 0;

		while (offs < vertexCount) {
			int vtc = min(vertexCount - offs, this.drawMode == 7 && TRIANGLE_MODE
				? trivertsInBuffer : nativeBufferSize >> 5);

			sharedIntBuffer.clear();
			sharedIntBuffer.put(vertexBuffer, offs * 8, vtc * 8);
			sharedByteBuffer.position(0);
			sharedByteBuffer.limit(vtc * 32);

			offs += vtc;

			if (sharedUseVBO && sharedVertexBuffers != null) {
				vboIndex = (vboIndex + 1) % sharedVboCount;
				glBindBufferARB(GL_ARRAY_BUFFER, sharedVertexBuffers.get(vboIndex));
				glBufferDataARB(GL_ARRAY_BUFFER, sharedByteBuffer, 0x88E0);
			}

			if (this.textured) {
				if (sharedUseVBO) {
					glTexCoordPointer(2, GL_FLOAT, 32, 12L);
				} else {
					sharedFloatBuffer.position(3);
					glTexCoordPointer(2, 32, sharedFloatBuffer);
				}

				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			}

			if (this.colored) {
				if (sharedUseVBO) {
					glColorPointer(4, GL_UNSIGNED_BYTE, 32, 20L);
				} else {
					sharedByteBuffer.position(20);
					glColorPointer(4, true, 32, sharedByteBuffer);
				}

				glEnableClientState(GL_COLOR_ARRAY);
			}

			if (this.hasNormals) {
				if (sharedUseVBO) {
					glNormalPointer(GL_BYTE, 32, 24L);
				} else {
					sharedByteBuffer.position(24);
					glNormalPointer(32, sharedByteBuffer);
				}

				glEnableClientState(GL_NORMAL_ARRAY);
			}

			if (sharedUseVBO) {
				glVertexPointer(3, GL_FLOAT, 32, 0L);
			} else {
				sharedFloatBuffer.position(0);
				glVertexPointer(3, 32, sharedFloatBuffer);
			}

			glEnableClientState(GL_VERTEX_ARRAY);
			if (this.drawMode == 7 && TRIANGLE_MODE) glDrawArrays(4, 0, this.vertexCount);
			else glDrawArrays(this.drawMode, 0, this.vertexCount);
			glDisableClientState(GL_VERTEX_ARRAY);

			if (this.textured) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			if (this.colored) glDisableClientState(GL_COLOR_ARRAY);
			if (this.hasNormals) glDisableClientState(GL_NORMAL_ARRAY);

			if (drawMode == GL_QUADS && TRIANGLE_MODE) glDrawArrays(GL_TRIANGLES, 0, vtc);
			else glDrawArrays(drawMode, 0, vtc);
		}

		if (vertexBufferSize > 0x20000 && index < (vertexBufferSize << 3)) {
			vertexBufferSize = 0;
			vertexBuffer = null;
		}

		this.clear();
	}
}
