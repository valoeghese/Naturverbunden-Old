package valoeghese.naturverbunden.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import valoeghese.naturverbunden.mechanics.PlayerStats;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
	@Inject(at = @At("HEAD"), method = "wakeSleepingPlayers")
	private void onWakeSleepingPlayers(CallbackInfo info) {
		@SuppressWarnings("resource")
		World world = (World) (Object) this;

		if (world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {
			world.getPlayers().stream().filter(LivingEntity::isSleeping).forEach(playerEntity -> {
				if (playerEntity.getHealth() < playerEntity.getMaxHealth() && playerEntity.getHungerManager().getFoodLevel() > 0) {
					playerEntity.heal(playerEntity.getMaxHealth() / 2);
					playerEntity.addExhaustion(30.0f);
					((PlayerStats) playerEntity).h_resetHealPoints();
				}
			});
		}
	}
}
