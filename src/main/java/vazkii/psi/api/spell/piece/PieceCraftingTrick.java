package vazkii.psi.api.spell.piece;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.item.ItemCAD;

/**
 * Pieces extending this class can be used for crafting.
 */
public abstract class PieceCraftingTrick extends PieceTrick {
	public PieceCraftingTrick(Spell spell) {
		super(spell);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		if (cad.getItem() instanceof ItemCAD) {
			((ItemCAD) cad.getItem()).craft(cad, context.caster, this);
		}
		return null;
	}

	/**
	 * Whether the passed trick can craft the recipe containing this trick instance.
	 * Recipes requiring no trick will always work.
	 *
	 * @param trick Trick from casted spell
	 */
	public abstract boolean canCraft(PieceCraftingTrick trick);
}
