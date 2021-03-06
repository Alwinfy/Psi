package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import vazkii.psi.client.core.handler.ColorHandler;
import vazkii.psi.common.lib.LibMisc;

public class ItemTriggerExosuitSensor extends ItemExosuitSensor {
	public static final String EVENT_TRIGGER = LibMisc.MOD_ID + ".event.spell_detonate";

	public ItemTriggerExosuitSensor(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		return ColorHandler.pulseColor(0xBC650F, 0.1f, 96);
	}

	@Override
	public String getEventType(ItemStack stack) {
		return EVENT_TRIGGER;
	}
}
