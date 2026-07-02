package net.steveson.horsemod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.steveson.horsemod.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LlamaEntity.class)
public abstract class LlamaMixin extends AbstractDonkeyEntity {
    protected LlamaMixin(EntityType<? extends AbstractDonkeyEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/LlamaEntity;",
            at = @At(value = "STORE", ordinal = 0))
    public int modifyI(int i, ServerWorld serverWorld, PassiveEntity passiveEntity) {
        int mommy = this.getStrength();
        LlamaEntity llama1 = (LlamaEntity)passiveEntity;
        int daddy = llama1.getStrength();
        int max = Math.max(mommy, daddy);
        int min = Math.min(mommy, daddy);

        int j = (int)Math.ceil(
                max * (1 - (Math.pow(
                        this.random.nextDouble(), (1 + (
                                (double) (min - 1) / Math.max((max - 1), 1)
                        ))
                )))
        );

        double cfgChance = Config.BETTER_LLAMA_CHANCE.get();
        double getBetter = (mommy == daddy ? 2 * cfgChance : cfgChance);

        if (this.random.nextFloat() < getBetter) {
            j = max + 1;
        }
        return j;
    }

    @Shadow
    private static final TrackedData<Integer> STRENGTH = DataTracker.registerData(LlamaEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Shadow
    public int getStrength() {
        return this.dataTracker.get(STRENGTH);
    }
}
