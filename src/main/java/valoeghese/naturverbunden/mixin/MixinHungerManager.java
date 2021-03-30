package valoeghese.naturverbunden.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import valoeghese.naturverbunden.mechanics.PlayerStats;

@Mixin(HungerManager.class)
public class MixinHungerManager {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;heal(F)V"), method = "update(Lnet/minecraft/entity/player/PlayerEntity;)V")
	private void redirectUpdateHeal(PlayerEntity player, float amount) {
		player.heal(((PlayerStats) player).h_expendHealPoints(amount));
	}
}
