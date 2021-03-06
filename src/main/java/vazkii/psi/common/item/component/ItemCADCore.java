/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 00:17:09 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;

public class ItemCADCore extends ItemCADComponent {

	public ItemCADCore(Item.Properties properties) {
		super(properties);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.CORE;
	}

}
