package com.example.examplemod.entity;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownMultiPotion extends ThrowableItemProjectile implements ItemSupplier {
	public static final double SPLASH_RANGE = 4.0D;
	private static final double SPLASH_RANGE_SQ = 16.0D;
	public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;
	private boolean lingering = false;
	
	public ThrownMultiPotion(Level level) {
		super(EntityRegistry.MULTIPOTION.get(), level);
	}
	
	public ThrownMultiPotion(Level level, LivingEntity entity, boolean lingering) {
		super(EntityRegistry.MULTIPOTION.get(), entity, level);
		this.lingering = lingering;
	}
	
	public ThrownMultiPotion(Level level, double x, double y, double z, boolean lingering) {
		super(EntityRegistry.MULTIPOTION.get(), x, y, z, level);
		this.lingering = lingering;
	}
	
	@Override
	protected Item getDefaultItem() {
		return Items.SPLASH_POTION;
	}
	
	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravity() {
		return 0.05F;
	}
	
	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	@Override
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();
			ListTag tag = itemstack.getOrCreateTag().getList("Potions", 8);
			for (int i=0; i< tag.size(); i++) {
				Potion potion = Registry.POTION.get(new ResourceLocation(tag.getString(i)));
				List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
				boolean flag = potion == Potions.WATER && list.isEmpty();
				if (flag) {
					this.applyWater();
				} else if (!list.isEmpty()) {
					if (this.isLingering()) {
						this.makeAreaOfEffectCloud(itemstack);
					} else {
						this.applySplash(list, pResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)pResult).getEntity() : null);
					}
				}
				
				int j = PotionUtils.getMobEffects(itemstack).stream().anyMatch(e -> e.getEffect().isInstantenous()) ? 2007 : 2002;
				this.level.levelEvent(j, this.blockPosition(), PotionUtils.getColor(potion));
				this.discard();
			}
		}
	}
	
	private void applyWater() {
		AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb, WATER_SENSITIVE);
		if (!list.isEmpty()) {
			for(LivingEntity livingentity : list) {
				double d0 = this.distanceToSqr(livingentity);
				if (d0 < 16.0D && livingentity.isSensitiveToWater()) {
					livingentity.hurt(DamageSource.indirectMagic(livingentity, this.getOwner()), 1.0F);
				}
			}
		}
		
		for(Axolotl axolotl : this.level.getEntitiesOfClass(Axolotl.class, aabb)) {
			axolotl.rehydrate();
		}
		
	}
	
	private void applySplash(List<MobEffectInstance> p_37548_, @Nullable Entity p_37549_) {
		AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
		if (!list.isEmpty()) {
			Entity entity = this.getEffectSource();
			
			for(LivingEntity livingentity : list) {
				if (livingentity.isAffectedByPotions()) {
					double d0 = this.distanceToSqr(livingentity);
					if (d0 < 16.0D) {
						double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
						if (livingentity == p_37549_) {
							d1 = 1.0D;
						}
						
						for(MobEffectInstance mobeffectinstance : p_37548_) {
							MobEffect mobeffect = mobeffectinstance.getEffect();
							if (mobeffect.isInstantenous()) {
								mobeffect.applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance.getAmplifier(), d1);
							} else {
								int i = (int)(d1 * (double)mobeffectinstance.getDuration() + 0.5D);
								if (i > 20) {
									livingentity.addEffect(new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	private void makeAreaOfEffectCloud(ItemStack stack) {
		ListTag list = stack.getOrCreateTag().getList("Potions", 8);
		for (int i=0; i< list.size(); i++) {
			Potion potion = Registry.POTION.get(new ResourceLocation(list.getString(i)));
			
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
			Entity entity = this.getOwner();
			if (entity instanceof LivingEntity) {
				areaeffectcloud.setOwner((LivingEntity)entity);
			}
			
			areaeffectcloud.setRadius(3.0F);
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
			areaeffectcloud.setPotion(potion);
			if (i == 0) {
				for(MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(stack)) {
					areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
				}
			}
			this.level.addFreshEntity(areaeffectcloud);
		}
		
		
	}
	
	private boolean isLingering() {
		return this.lingering;
	}
}
