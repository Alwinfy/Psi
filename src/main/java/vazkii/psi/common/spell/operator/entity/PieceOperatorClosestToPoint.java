/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [23/01/2016, 00:36:43 (GMT)]
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;
import vazkii.psi.api.internal.MathHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorClosestToPoint extends PieceOperator {

	SpellParam position;
	SpellParam list;
	
	public PieceOperatorClosestToPoint(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(list = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper listVal = this.<EntityListWrapper>getParamValue(context, list);
		Vector3 positionVal = this.<Vector3>getParamValue(context, position);

		double closest = Double.MAX_VALUE;
		Entity closestEntity = null;
		for(Entity e : listVal) {
			double dist = MathHelper.pointDistanceSpace(positionVal.x, positionVal.y, positionVal.z, e.posX, e.posY, e.posZ);
			if(dist < closest) {
				closest = dist;
				closestEntity = e;
			}
		}
		
		return closestEntity;
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}