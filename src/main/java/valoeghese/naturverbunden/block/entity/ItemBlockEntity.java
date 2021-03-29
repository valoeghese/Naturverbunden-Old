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

package valoeghese.naturverbunden.block.entity;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import valoeghese.naturverbunden.block.NVBBlocks;

public class ItemBlockEntity extends BlockEntity {
	public ItemBlockEntity(BlockPos pos, BlockState state) {
		super(NVBBlocks.ITEM_BLOCK_ENTITY, pos, state);
		this.items = DefaultedList.ofSize(3, ItemStack.EMPTY);
	}

	private final DefaultedList<ItemStack> items;

	public DefaultedList<ItemStack> getItems() {
		return this.items;
	}

	public boolean addItem(ItemStack item) {
		for(int i = 0; i < this.items.size(); ++i) {
			ItemStack itemStack = this.items.get(i);

			if (itemStack.isEmpty()) {
				this.items.set(i, item.split(1));
				this.updateListeners();
				return true;
			}
		}

		return false;
	}

	private void updateListeners() {
		this.markDirty();
		this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
	}


	public void clear() {
		this.items.clear();
	}

	// NBT Hell

	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.items.clear();
		Inventories.readNbt(tag, this.items);
	}

	public NbtCompound writeNbt(NbtCompound tag) {
		this.saveInitialChunkData(tag);
		return tag;
	}

	private NbtCompound saveInitialChunkData(NbtCompound tag) {
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.items, true);
		return tag;
	}

	@Nullable
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, Registry.BLOCK_ENTITY_TYPE.getRawId(this.getType()), this.toInitialChunkDataNbt());
	}

	public NbtCompound toInitialChunkDataNbt() {
		return this.saveInitialChunkData(new NbtCompound());
	}
}
