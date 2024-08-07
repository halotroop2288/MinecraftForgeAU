/*
 * This software is provided under the terms of the Minecraft Forge Public License v1.1.
 */
package net.minecraftforge.mixin;

import net.fabricmc.api.*;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author halotroop2288
 */
@Environment(EnvType.SERVER)
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
}
