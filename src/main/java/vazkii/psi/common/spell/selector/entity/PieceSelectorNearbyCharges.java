package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellCharge;

import java.util.Objects;
import java.util.function.Predicate;

public class PieceSelectorNearbyCharges extends PieceSelectorNearby {


	public PieceSelectorNearbyCharges(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e instanceof EntitySpellCharge && (Objects.requireNonNull(((EntitySpellCharge) e).getThrower()).getName().equals(context.caster.getName()));
	}
}
