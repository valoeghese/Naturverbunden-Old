package valoeghese.naturverbunden.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import valoeghese.naturverbunden.mechanics.PlayerStats;

// Fun fact
// Did you know the Aztecs didn't have wheels?
// The Aztecs had amazing aqueducts, however

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements PlayerStats {
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private float healpoints = 8.0f;
	private long sprintTimeTarget = 0;
	private long attackTimeTarget = 0;
	private boolean sprintUnlock = true;

	/**
	 * @reason replace healing with long/short rest.
	 */
	@Inject(at = @At("RETURN"), method = "canFoodHeal", cancellable = true)
	private void onCanFoodHeal(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValueZ() && ((PlayerStats) this).h_allowNaturalHeal(this.world.getTime()));
	}

	@Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
	private void onReadCustomDataFromNbt(NbtCompound tag, CallbackInfo info) {
		if (!this.world.isClient()) {
			if (tag.contains("healData")) {
				tag = tag.getCompound("healData"); 
				this.healpoints = tag.getFloat("healpoints");
				this.sprintTimeTarget = tag.getLong("sprintTimeTarget");
				this.attackTimeTarget = tag.getLong("attackTimeTarget");
				this.sprintUnlock = tag.getBoolean("sprintUnlock");
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
	private void writeCustomDataToNbt(NbtCompound tag, CallbackInfo info) {
		if (!this.world.isClient()) {
			NbtCompound healData = new NbtCompound();
			healData.putFloat("healpoints", this.healpoints);
			healData.putLong("sprintTimeTarget", this.sprintTimeTarget);
			healData.putLong("attackTimeTarget", this.attackTimeTarget);
			healData.putBoolean("sprintUnlock", this.sprintUnlock);

			tag.put("healData", healData);
		}
	}

	// Checks

	@Override
	public boolean h_allowNaturalHeal(long tickTime) {
		return this.sprintUnlock && this.healpoints > 0.0f && tickTime > this.sprintTimeTarget && tickTime > this.attackTimeTarget;
	}

	// Updating Stuff

	@Override
	public float h_expendHealPoints(float heal) {
		float result = Math.min(this.healpoints, heal);
		this.healpoints -= result;
		return result;
	}

	@Override
	public void h_setUnlockVonSprint(long tickTime, boolean unlock) {
		if (this.sprintUnlock != unlock) {
			if (this.sprintUnlock = unlock) {
				this.sprintTimeTarget = tickTime + 600; // 20ticks * 30seconds
			}
		}
	}

	@Override
	public void h_attackedAt(long tickTime) {
		this.attackTimeTarget = tickTime + 600;
	}

	@Override
	public void h_resetHealPoints() {
		this.healpoints = 8.0f;
	}
}
