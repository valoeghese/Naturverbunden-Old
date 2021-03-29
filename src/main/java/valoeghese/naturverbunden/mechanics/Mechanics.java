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

package valoeghese.naturverbunden.mechanics;

import java.util.function.Consumer;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import valoeghese.naturverbunden.block.NVBBlocks;
import valoeghese.naturverbunden.block.entity.ItemBlockEntity;

public class Mechanics {
	public static void placeItem(ItemUsageContext context, Consumer<ActionResult> sreturn) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos().offset(context.getSide());
		ItemStack stack = context.getStack();

		if (stack != ItemStack.EMPTY && !world.isOutOfHeightLimit(pos)) {
			if (world.isAir(pos)) {
				BlockState state = NVBBlocks.ITEM_BLOCK.getDefaultState();

				// Set the block in the world
				world.setBlockState(pos, state);
				ItemBlockEntity entity = (ItemBlockEntity) world.getBlockEntity(pos);
				entity.addItem(new ItemStack(stack.getItem(), 1));

				// Decrement stack
				stack.decrement(1);

				sreturn.accept(ActionResult.SUCCESS);
			}
		}
	}
}
