package net.steveson.horsemod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.steveson.horsemod.Config;
import net.steveson.horsemod.entity.NoRiderRandomTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Predicate;

@Mixin(HorseEntity.class)
public abstract class HorseMixin extends AbstractHorseEntity {

    protected HorseMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    protected void initCustomGoals() {
        super.initCustomGoals();
        if (Config.H_EATS_CHICKENS.get()) {
            this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0D, true));
//            this.targetSelector.add(5, new ActiveTargetGoal<>(this, ChickenEntity.class, 10, true, true, PREY_SELECTOR));
            this.targetSelector.add(5, new NoRiderRandomTargetGoal<>(this, ChickenEntity.class, true, true, PREY_SELECTOR));
        }
    }

    @Unique
    public boolean tryAttack(Entity target) {
        this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        if (this.getHealth() < this.getMaxHealth()) {
            this.heal(2);
        }
        return target.damage(this.getDamageSources().mobAttack(this), 4.0F);
    }

    @Unique
    private static final Predicate<LivingEntity> PREY_SELECTOR = (potentialVictim) -> {
      return potentialVictim.isBaby();
    };
}
