package net.steveson.horsemod.mixin;

import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseMixin {
//	@Inject(at = @At("HEAD"), method = "loadLevel")
//	private void init(CallbackInfo info) {
//		// This code is injected into the start of MinecraftServer.loadLevel()V
//	}
}