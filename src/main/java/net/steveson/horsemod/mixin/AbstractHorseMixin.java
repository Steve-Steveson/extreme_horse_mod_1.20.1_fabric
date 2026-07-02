package net.steveson.horsemod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.steveson.horsemod.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseMixin extends AnimalEntity {
    protected AbstractHorseMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
//	@Inject(at = @At("HEAD"), method = "loadLevel")
//	private void init(CallbackInfo info) {
//		// This code is injected into the start of MinecraftServer.loadLevel()V
//	}
    /**
     * @author Minecraft
     * @reason Complete re-write of to use config min and max, keep matching stats, and not lose max stats every time.
     */
    @Overwrite
    public static double calculateAttributeBaseValue(double parentBase, double otherParentBase, double min, double max, Random random) {
        double cfgMin;
        double cfgMax;
        if (max == 1) {
            cfgMax = Config.H_MAX_JUMP.get();
            cfgMin = Config.H_MIN_JUMP.get();
        } else if (max == 30) {
            cfgMax = Config.H_MAX_HEALTH.get();
            cfgMin = Config.H_MIN_HEALTH.get();
        } else {
            cfgMax = Config.H_MAX_SPEED.get();
            cfgMin = Config.H_MIN_SPEED.get();
        }

        if (max <= min || cfgMax < cfgMin) {
            throw new IllegalArgumentException("Incorrect range for a horse attribute");
        } else {
            double parentValue1 = MathHelper.clamp(parentBase, cfgMin, cfgMax);
            double parentValue2 = MathHelper.clamp(otherParentBase, cfgMin, cfgMax);
            if (Config.KEEP_MATCHING_STATS.get() && Math.abs(parentValue1 - parentValue2) <= 0.0025 * (max-min)) {
                double avr = (cfgMax + cfgMin) / 2;
                if (Math.abs(avr - parentValue1) >= Math.abs(avr - parentValue2)) {
                    return parentValue1;
                } else {
                    return parentValue2;
                }
            } else {
                double d0partOfRange = 0.3 * (max - min);
                double d1maxSpread = Math.abs(parentValue1 - parentValue2) + d0partOfRange;
                double d2parentalAverage = (parentValue1 + parentValue2) / 2;
                double d3randomness = (random.nextDouble() + random.nextDouble() + random.nextDouble()) / 3.0D - 0.5D;
                double d4unclampedResult = d2parentalAverage + d1maxSpread * d3randomness;

                return MathHelper.clamp(d4unclampedResult, cfgMin, cfgMax);
            }
        }
    }

    /**
     * @author Minecraft
     * @reason fall damage reduction for ultra-high jumpers
     */
    @Overwrite
    // this uses "extends Animal"
    public int computeFallDamage(float fallDistance, float damageMultiplier) {
        double jumpValue = MathHelper.clamp(getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH), 1, 2);
        double jumpHeight = jumpValue * jumpValue * 3.6 + 2.3;

        return MathHelper.ceil(((fallDistance - jumpHeight + 5.9) * 0.5F - 3.0F) * damageMultiplier);
    }
}