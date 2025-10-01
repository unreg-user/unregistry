package wta.util.utils;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;

public interface UnregistryNull {
	ItemUnregNull itemUnregNull=new ItemUnregNull();

	class ItemUnregNull extends Item implements  UnregistryNull{
		private ItemUnregNull() {
			super(new Item.Settings());
			SimpleRegistryFI.removeIntrusive(Registries.ITEM, this);
		}
	}
}
