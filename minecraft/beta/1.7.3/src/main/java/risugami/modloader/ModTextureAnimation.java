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
import org.lwjgl.opengl.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ModTextureAnimation extends DynamicTexture {
	private final int tickRate;
	private final byte[][] images;
	private int index;
	private int ticks;

	public ModTextureAnimation(int slot, int dst, BufferedImage source, int rate) {
		this(slot, 1, dst, source, rate);
	}

	public ModTextureAnimation(int slot, int size, int dst, BufferedImage source, int rate) {
		super(slot);
		this.index = 0;
		this.ticks = 0;
		this.replicate = size;
		this.atlas = dst;
		this.tickRate = rate;
		this.ticks = rate;
		this.bind(ModLoader.getMinecraftInstance().textureManager);
		int targetWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
		int targetHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
		int width = source.getWidth();
		int height = source.getHeight();
		int images = (int) Math.floor(height / width);
		if (images <= 0) {
			throw new IllegalArgumentException("source has no complete images");
		} else {
			this.images = new byte[images][];
			if (width != targetWidth) {
				BufferedImage img = new BufferedImage(targetWidth, targetHeight * images, 6);
				Graphics2D gfx = img.createGraphics();
				gfx.drawImage(source, 0, 0, targetWidth, targetHeight * images, 0, 0, width, height, null);
				gfx.dispose();
				source = img;
			}

			for (int i = 0; i < images; ++i) {
				int[] temp = new int[targetWidth * targetHeight];
				source.getRGB(0, targetHeight * i, targetWidth, targetHeight, temp, 0, targetWidth);
				this.images[i] = new byte[targetWidth * targetHeight * 4];

				for (int j = 0; j < temp.length; ++j) {
					int a = temp[j] >> 24 & 255;
					int r = temp[j] >> 16 & 255;
					int g = temp[j] >> 8 & 255;
					int b = temp[j] >> 0 & 255;
					this.images[i][j * 4 + 0] = (byte) r;
					this.images[i][j * 4 + 1] = (byte) g;
					this.images[i][j * 4 + 2] = (byte) b;
					this.images[i][j * 4 + 3] = (byte) a;
				}
			}

		}
	}

	public void tick() {
		if (this.ticks >= this.tickRate) {
			++this.index;
			if (this.index >= this.images.length) {
				this.index = 0;
			}

			this.pixels = this.images[this.index];
			this.ticks = 0;
		}

		++this.ticks;
	}
}
