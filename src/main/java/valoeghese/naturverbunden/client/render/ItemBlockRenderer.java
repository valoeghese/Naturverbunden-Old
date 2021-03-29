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

package valoeghese.naturverbunden.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import valoeghese.naturverbunden.block.entity.ItemBlockEntity;

public class ItemBlockRenderer implements BlockEntityRenderer<ItemBlockEntity> {
	public ItemBlockRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(ItemBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		DefaultedList<ItemStack> defaultedList = entity.getItems();
		int pos = (int) entity.getPos().asLong();

		System.out.println("e");
		for(int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = (ItemStack)defaultedList.get(i);

			if (stack != ItemStack.EMPTY) {
				matrices.push();
				matrices.translate(0.5 + 0.1 * ((i + 1) & 0b1), 0.1 * i + 0.05, 0.5D + 0.1 * (i & 0b1));

				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, pos + i);
				matrices.pop();
			}
		}
	}
}
