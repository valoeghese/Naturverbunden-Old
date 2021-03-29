/*
 * Naturverbunden
 * Copyright (C) 2020 Valoeghese
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import valoeghese.naturverbunden.block.NVBBlocks;
import valoeghese.naturverbunden.block.entity.ItemBlockEntity;

@Mixin(Item.class)
public class MixinItem {
	@Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
	private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
		try {
			World world = context.getWorld();
			BlockPos pos = context.getBlockPos().offset(context.getSide());
			ItemStack stack = context.getStack();

			if (stack != ItemStack.EMPTY && !world.isOutOfHeightLimit(pos)) {
				if (world.isAir(pos)) {
					BlockState state = NVBBlocks.ITEM_BLOCK.getDefaultState();

					world.setBlockState(pos, state);
					ItemBlockEntity entity = NVBBlocks.ITEM_BLOCK_ENTITY.instantiate(pos, state);
					
					stack.decrement(1);

					world.addBlockEntity(entity);
					entity.addItem(new ItemStack(stack.getItem(), 1));
					
					info.setReturnValue(ActionResult.SUCCESS);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in Item Block Placement", e);
		}
	}
}
