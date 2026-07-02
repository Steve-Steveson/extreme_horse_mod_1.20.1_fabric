package net.steveson.horsemod.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.steveson.horsemod.Config;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class NoRiderRandomTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final AnimalEntity rideableMob;

    public NoRiderRandomTargetGoal(AnimalEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, targetClass, 10, checkVisibility, checkCanNavigate, targetPredicate);
        this.rideableMob = mob;
    }

    @Override
    public boolean canStart() {
        return (!this.rideableMob.hasPassengers() || Config.FAST_FOOD.get()) && super.canStart();
    }

    //This fixed the null pointer
    @Override
    public boolean shouldContinue() {
        return (this.targetPredicate != null && this.target != null) ? this.targetPredicate.test(this.mob, this.target) : super.shouldContinue();
    }
}
