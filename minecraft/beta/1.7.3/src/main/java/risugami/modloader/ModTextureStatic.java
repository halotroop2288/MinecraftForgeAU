/*
 * Copyright (c) 2026 Caroline Joy Bell.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package risugami.modloader;

import net.minecraft.client.render.texture.DynamicTexture;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class ModTextureStatic extends DynamicTexture {
	private boolean oldanaglyph;
	private int[] pixels;

	public ModTextureStatic(int slot, int dst, BufferedImage source) {
		this(slot, 1, dst, source);
	}

	public ModTextureStatic(int slot, int size, int dst, BufferedImage source) {
		super(slot);
		this.pixels = null;
		this.replicate = size;
		this.atlas = dst;
		this.bind(ModLoader.getMinecraftInstance().textureManager);
		int targetWidth = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH) / 16;
		int targetHeight = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT) / 16;
		int width = source.getWidth();
		int height = source.getHeight();
		this.pixels = new int[targetWidth * targetHeight * 4];
		if (width == height && width == targetWidth) {
			source.getRGB(0, 0, width, height, this.pixels, 0, width);
		} else {
			BufferedImage img = new BufferedImage(targetWidth, targetHeight, 6);
			Graphics2D gfx = img.createGraphics();
			gfx.drawImage(source, 0, 0, targetWidth, targetHeight, 0, 0, width, height, null);
			img.getRGB(0, 0, targetWidth, targetHeight, this.pixels, 0, targetWidth);
			gfx.dispose();
		}

		this.update();
	}

	public void update() {
		for (int i = 0; i < this.pixels.length; ++i) {
			int a = this.pixels[i] >> 24 & 255;
			int r = this.pixels[i] >> 16 & 255;
			int g = this.pixels[i] >> 8 & 255;
			int b = this.pixels[i] >> 0 & 255;
			if (this.anaglyph) {
				int grey = (r + g + b) / 3;
				b = grey;
				g = grey;
				r = grey;
			}

			this.pixels[i * 4 + 0] = (byte) r;
			this.pixels[i * 4 + 1] = (byte) g;
			this.pixels[i * 4 + 2] = (byte) b;
			this.pixels[i * 4 + 3] = (byte) a;
		}

		this.oldanaglyph = this.anaglyph;
	}

	public void tick() {
		if (this.oldanaglyph != this.anaglyph) {
			this.update();
		}

	}

	public static BufferedImage scale2x(BufferedImage in) {
		int width = in.getWidth();
		int height = in.getHeight();
		BufferedImage out = new BufferedImage(width * 2, height * 2, 2);

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int E = in.getRGB(x, y);
				int B;
				if (y == 0) {
					B = E;
				} else {
					B = in.getRGB(x, y - 1);
				}

				int D;
				if (x == 0) {
					D = E;
				} else {
					D = in.getRGB(x - 1, y);
				}

				int F;
				if (x >= width - 1) {
					F = E;
				} else {
					F = in.getRGB(x + 1, y);
				}

				int H;
				if (y >= height - 1) {
					H = E;
				} else {
					H = in.getRGB(x, y + 1);
				}

				int E0;
				int E1;
				int E2;
				int E3;
				if (B != H && D != F) {
					E0 = D == B ? D : E;
					E1 = B == F ? F : E;
					E2 = D == H ? D : E;
					E3 = H == F ? F : E;
				} else {
					E0 = E;
					E1 = E;
					E2 = E;
					E3 = E;
				}

				out.setRGB(x * 2, y * 2, E0);
				out.setRGB(x * 2 + 1, y * 2, E1);
				out.setRGB(x * 2, y * 2 + 1, E2);
				out.setRGB(x * 2 + 1, y * 2 + 1, E3);
			}
		}

		return out;
	}
}
