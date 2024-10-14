package org.crychicteam.dunes.content.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.*;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.dunes.init.registrate.DunesArmorSet;
import org.crychicteam.dunes.init.registrate.DunesMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CactusAffinity extends MobEffect {
    public CactusAffinity(MobEffectCategory pCategory, int pColor) {
        super(MobEffectCategory.NEUTRAL, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.isUsingItem() && (livingEntity.getMainHandItem().getItem() instanceof InstrumentItem || livingEntity.getOffhandItem().getItem() instanceof InstrumentItem) && Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet((Player) livingEntity).equals(DunesArmorSet.CACTUS_ARMORSET)
        && !livingEntity.level().isClientSide()) {
            double throwDamage = 0;
            if (DunesMisc.CACTUS_AFFINITY != null) throwDamage = Objects.requireNonNull(livingEntity.getAttribute(DunesMisc.CACTUS_AFFINITY.get())).getValue();
            if (Cibrary.ARMOR_SET_MANAGER.getArmorSetState((Player)livingEntity).equals(ArmorSet.State.ACTIVE)) throwDamage *= 2;
            if (!livingEntity.level().isClientSide()) armorSetEffect(livingEntity, throwDamage);
        }
        int damage = amplifier * 5;
        if (DunesMisc.CACTUS_AFFINITY != null) {
            damage = (int) (amplifier * 5 / livingEntity.getAttributeValue(DunesMisc.CACTUS_AFFINITY.get()));
        }

        DamageSource cactusDamageFromSelf = new DamageSource(livingEntity.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.CACTUS), livingEntity, livingEntity);
        livingEntity.hurt(cactusDamageFromSelf, (float) damage);
    }

    private void armorSetEffect(LivingEntity livingEntity, double damage){
        for (EquipmentSlot slot : EquipmentSlot.values()){
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack item = livingEntity.getItemBySlot(slot);
                if (item.getDamageValue() >= item.getMaxDamage() - damage - 100) return;
                item.hurtAndBreak((int) damage,livingEntity, entity -> {});
            }
        }
        volcanoArrowEffect(livingEntity, damage);
    }

    public void volcanoArrowEffect(LivingEntity livingEntity, double damage) {
        Random random = new Random();
        int arrowCount = (int) ((damage + 1) * 5);
        double searchRadius = 3.0;
        double searchHeight = 4.0;

        Vec3 eyePosition = livingEntity.getEyePosition();
        Vec3 lookVec = livingEntity.getLookAngle();
        double rayTraceDistance = 10.0;

        HitResult hitResult = livingEntity.level().clip(new ClipContext(
                eyePosition,
                eyePosition.add(lookVec.scale(rayTraceDistance)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                livingEntity
        ));

        Vec3 targetPoint;
        if (hitResult.getType() == HitResult.Type.MISS) {
            targetPoint = eyePosition.add(lookVec.scale(rayTraceDistance));
        } else {
            targetPoint = hitResult.getLocation();
        }

        AABB searchBox = new AABB(
                targetPoint.x - searchRadius, targetPoint.y - searchHeight, targetPoint.z - searchRadius,
                targetPoint.x + searchRadius, targetPoint.y + searchHeight, targetPoint.z + searchRadius
        );

        List<BlockPos> validSpawnPositions = findValidSpawnPositions(livingEntity.level(), searchBox);
        if (validSpawnPositions.isEmpty()) {
            return;
        }

        for (int i = 0; i < arrowCount; i++) {
            if (validSpawnPositions.isEmpty()) {
                break;
            }
            BlockPos spawnPos = validSpawnPositions.remove(random.nextInt(validSpawnPositions.size()));

            Arrow arrow = new Arrow(EntityType.ARROW, livingEntity.level()) {
                @Override
                protected void onHitBlock(BlockHitResult pResult) {
                    if (!livingEntity.level().getBlockState(pResult.getBlockPos()).getFluidState().is(Fluids.WATER)){
                        super.onHitBlock(pResult);
                        this.discard();
                        var damage = new DamageSource(livingEntity.level().registryAccess()
                                .registryOrThrow(Registries.DAMAGE_TYPE)
                                .getHolderOrThrow(DamageTypes.CACTUS), livingEntity, livingEntity);
                        livingEntity.level().explode(livingEntity,damage,null, pResult.getBlockPos().getX(), pResult.getBlockPos().getY(), pResult.getBlockPos().getZ(), 2, false, Level.ExplosionInteraction.NONE);
                    }}

                @Override
                protected void onHitEntity(EntityHitResult pResult) {
                    if (pResult.getEntity().is(Objects.requireNonNull(this.getOwner()))) {
                        pResult.getEntity().setInvulnerable(true);
                    }
                    super.onHitEntity(pResult);
                    if (this.getOwner() instanceof LivingEntity owner) {
                        owner.setInvulnerable(false);
                    }
                }
            };

            arrow.setOwner(livingEntity);
            arrow.setCritArrow(true);
            arrow.setBaseDamage(damage);
            arrow.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5);

            double dx = random.nextGaussian() * 0.2;
            double dy = 0.5 + random.nextDouble() * 0.5;
            double dz = random.nextGaussian() * 0.2;
            Vec3 direction = new Vec3(dx, dy, dz).normalize();

            if (spawnPos.getY() < livingEntity.getY()) {
                direction = direction.add(0, 0.2, 0).normalize();
            }

            double speed = 0.3 + random.nextDouble() * 0.5;
            arrow.setDeltaMovement(direction.scale(speed));
            arrow.setPierceLevel((byte) 10);
            livingEntity.level().addFreshEntity(arrow);
        }
    }

    private List<BlockPos> findValidSpawnPositions(Level level, AABB searchBox) {
        List<BlockPos> validPositions = new ArrayList<>();
        BlockPos.betweenClosed(
                new BlockPos((int)searchBox.minX, (int)searchBox.minY, (int)searchBox.minZ),
                new BlockPos((int)searchBox.maxX, (int)searchBox.maxY, (int)searchBox.maxZ)
        ).forEach(pos -> {
            if (isValidSpawnPosition(level, pos)) {
                validPositions.add(pos.immutable());
            }
        });
        return validPositions;
    }

    private boolean isValidSpawnPosition(Level level, BlockPos pos) {
        return (level.isEmptyBlock(pos) && !level.isEmptyBlock(pos.below()));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int baseInterval = 5;
        int interval = (int) (baseInterval * (1 + Math.log(amplifier + 1)));
        return duration % interval == 0;
//        return true;
    }
}
