/*
 * Naturverbunden
 * Copyright (C) 2021 Valoeghese
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package valoeghese.naturverbunden.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import valoeghese.naturverbunden.mechanics.PlayerStats;

// Fun fact
// Did you know the Aztecs didn't have wheels?
// The Aztecs had amazing aqueducts, however

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends LivingEntity implements PlayerStats {
	protected MixinServerPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private float healpoints = 8.0f;
	private long sprintTimeTarget = 0;
	private long attackTimeTarget = 0;
	private boolean sprintUnlock = true;

	@Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
	private void onReadCustomDataFromNbt(NbtCompound tag, CallbackInfo info) {
		if (tag.contains("healData")) {
			tag = tag.getCompound("healData"); 
			this.healpoints = tag.getFloat("healpoints");
			this.sprintTimeTarget = tag.getLong("sprintTimeTarget");
			this.attackTimeTarget = tag.getLong("attackTimeTarget");
			this.sprintUnlock = tag.getBoolean("sprintUnlock");
		}
	}

	@Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
	private void writeCustomDataToNbt(NbtCompound tag, CallbackInfo info) {
		NbtCompound healData = new NbtCompound();
		healData.putFloat("healpoints", this.healpoints);
		healData.putLong("sprintTimeTarget", this.sprintTimeTarget);
		healData.putLong("attackTimeTarget", this.attackTimeTarget);
		healData.putBoolean("sprintUnlock", this.sprintUnlock);

		tag.put("healData", healData);
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