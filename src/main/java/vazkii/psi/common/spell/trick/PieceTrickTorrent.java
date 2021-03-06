/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 18:16:41 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickTorrent extends PieceTrick {

	SpellParam<Vector3> position;

	public PieceTrickTorrent(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.POTENCY, 20);
		meta.addStat(EnumSpellStat.COST, 80);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.getEntityWorld().getDimension().doesWaterVaporize())
			return null;

		Vector3 positionVal = this.getParamValue(context, position);

		if (positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if (!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = positionVal.toBlockPos();

		BlockState state = context.caster.getEntityWorld().getBlockState(pos);
		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.getBlockSnapshot(context.caster.getEntityWorld(), pos), context.caster.getEntityWorld().getBlockState(pos.offset(Direction.UP)), context.caster);
		MinecraftForge.EVENT_BUS.post(placeEvent);
		if (placeEvent.isCanceled())
			return null;
		if (state.isAir(context.caster.getEntityWorld(), pos) || state.getMaterial().isReplaceable())
			context.caster.getEntityWorld().setBlockState(pos, Blocks.WATER.getDefaultState());
		else {
			pos = pos.up();
			state = context.caster.getEntityWorld().getBlockState(pos);
			if (state.isAir(context.caster.getEntityWorld(), pos) || state.getMaterial().isReplaceable())
				context.caster.getEntityWorld().setBlockState(pos, Blocks.WATER.getDefaultState());
		}

		return null;
	}

}
