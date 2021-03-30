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

package valoeghese.naturverbunden.init;

import java.util.function.Function;

import com.mojang.datafixers.types.Type;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import valoeghese.naturverbunden.Naturverbunden;
import valoeghese.naturverbunden.block.ItemBlock;
import valoeghese.naturverbunden.block.entity.ItemBlockEntity;

public class NVBBlocks {
	private static Block register(String id, AbstractBlock.Settings settings, Function<AbstractBlock.Settings, Block> blockifier) {
		return Registry.register(Registry.BLOCK, Naturverbunden.id(id), blockifier.apply(settings));
	}

	private static <T extends BlockEntity> BlockEntityType<T> create(String id, FabricBlockEntityTypeBuilder<T> builder) {
		Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id);
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Naturverbunden.id(id), builder.build(type));
	}

	public static final Block ITEM_BLOCK = register("item_block", AbstractBlock.Settings.of(Material.DECORATION)
			.strength(-1.0f, 0.4f)
			.noCollision()
			.nonOpaque()
			.sounds(BlockSoundGroup.STONE), ItemBlock::new);

	public static final BlockEntityType<ItemBlockEntity> ITEM_BLOCK_ENTITY = create("item_block", FabricBlockEntityTypeBuilder.create(ItemBlockEntity::new, ITEM_BLOCK));

	public static final Block forceRegister() {
		return ITEM_BLOCK;
	}
}
