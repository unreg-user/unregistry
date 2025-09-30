package wta.util.mixins.mixin;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import wta.util.mixins.func.OtherFunc;
import wta.util.mixins.interfaces.MutableRegistryFI;
import wta.util.utils.RegType;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryFixer<T> implements MutableRegistry<T>, MutableRegistryFI<T> {

	@Shadow
	@Final
	@Mutable
	private Map<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry;

	@Shadow
	@Final
	@Mutable
	private Map<Identifier, RegistryEntry.Reference<T>> idToEntry;

	@Shadow
	@Final
	@Mutable
	private Map<T, RegistryEntry.Reference<T>> valueToEntry;

	@Shadow
	@Final
	@Mutable
	private ObjectList<RegistryEntry.Reference<T>> rawIdToEntry;

	@Shadow
	@Final
	@Mutable
	private Reference2IntMap<T> entryToRawId;

	@Shadow
	@Final
	@Mutable
	private Map<RegistryKey<T>, RegistryEntryInfo> keyToEntryInfo;

	@Shadow
	private Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;

	@Shadow
	private Lifecycle lifecycle;

	/**
	 * @author UnregUser
	 * @reason optimization and other...
	 */
	@Overwrite
	public RegistryEntry.Reference<T> add(RegistryKey<T> key, T value, RegistryEntryInfo info){
		if (this.idToEntry.containsKey(key.getValue())) {
			Util.throwOrPause(new IllegalStateException("Adding duplicate key '" + key + "' to registry"));
		}
		RegistryEntry.Reference<T> reference;
		if (this.intrusiveValueToEntry != null) {
			reference = this.intrusiveValueToEntry.remove(value);
			if (reference == null) {
				String var10002 = String.valueOf(key);
				throw new AssertionError("Missing intrusive holder for " + var10002 + ":" + value);
			}

			reference.setRegistryKey(key);
		} else {
			reference = this.keyToEntry.computeIfAbsent(key, (k) -> RegistryEntry.Reference.standAlone(this.getEntryOwner(), k));
		}

		this.keyToEntry.put(key, reference);
		this.idToEntry.put(key.getValue(), reference);
		this.valueToEntry.put(value, reference);
		int i = this.rawIdToEntry.size();
		this.rawIdToEntry.add(reference);
		this.entryToRawId.put(value, i);
		this.keyToEntryInfo.put(key, info);
		//this.lifecycle = this.lifecycle.add(info.lifecycle());
		return reference;
	}

	@Override
	public RegistryEntry.Reference<T> unregistry$add(RegistryKey<T> key, RegType type, T value, RegistryEntryInfo info) {
		Objects.requireNonNull(key);
		RegistryEntry.Reference<T> reference=null;

		if (type.isRegister()){
			Objects.requireNonNull(value);
			reference=findReference(key, value);
		}else{
			OtherFunc.requireNull(value);
		}

		return switch (type){
			case REG -> this.add(key, value, info);
			case REREG -> {
				if (this.reregister(key, value, info, reference)){
					yield reference;
				}
				throw Util.throwOrPause(new IllegalStateException("Key not found "+key));
			}
			case REG_ALWAYS -> {
				if (this.reregister(key, value, info, reference)){
					yield reference;
				}
				yield this.add(key, value, info);
			}
			case UNREG -> {
				if (this.unregister(key)){
					yield null;
				}
				throw Util.throwOrPause(new IllegalStateException("Key not found "+key));
			}
			case UNREG_OR_NONE -> {
				this.unregister(key);
				yield null;
			}
		};
	}

	@Unique
	private RegistryEntry.Reference<T> findReference(RegistryKey<T> key, T value){
		RegistryEntry.Reference<T> reference;
		if (intrusiveValueToEntry != null) {
			reference = intrusiveValueToEntry.remove(value);
			if (reference == null) {
				String var10002 = String.valueOf(key);
				throw new AssertionError("Missing intrusive holder for " + var10002 + ":" + value);
			}
			reference.setRegistryKey(key);
		} else {
			reference = keyToEntry.computeIfAbsent(key, (k) -> RegistryEntry.Reference.standAlone(this.getEntryOwner(), k));
		}
		return reference;
	}

	@Unique
	private boolean reregister(RegistryKey<T> key, T value, RegistryEntryInfo info, RegistryEntry.Reference<T> reference){
		Optional<RegistryEntry.Reference<T>> oldValueO=this.getEntry(key);
		if (oldValueO.isEmpty()) return false;
		RegistryEntry.Reference<T> oldValueR = oldValueO.get();
		T oldValue = oldValueR.value();
		int i = rawIdToEntry.indexOf(oldValueR);
		rawIdToEntry.set(i, reference);
		//entryToRawId.removeInt(oldValue);
		//entryToRawId.put(value, i);
		valueToEntry.remove(oldValue);
		keyToEntry.put(key, reference);
		idToEntry.put(key.getValue(), reference);
		valueToEntry.put(value, reference);
		keyToEntryInfo.put(key, info);
		return true;
	}

	@Unique
	private boolean unregister(RegistryKey<T> key){
		Optional<RegistryEntry.Reference<T>> oldValueO=this.getEntry(key);
		if (oldValueO.isEmpty()) return false;
		RegistryEntry.Reference<T> oldValueR = oldValueO.get();
		T oldValue = oldValueR.value();
		int i = rawIdToEntry.indexOf(oldValueR);
		rawIdToEntry.set(i, null);
		//entryToRawId.removeInt(oldValue);
		valueToEntry.remove(oldValue);
		keyToEntry.remove(key);
		idToEntry.remove(key.getValue());
		keyToEntryInfo.remove(key);
		return true;
	}

	@Override
	public void unregistry$finalize() {
		Reference2IntArrayMap<T> entryToRawId=new Reference2IntArrayMap<>();
		rawIdToEntry.removeIf(Objects::isNull);
		for (int i=0; i<rawIdToEntry.size(); i++) {
			T objI=rawIdToEntry.get(i).value();
			entryToRawId.put(objI, i);
		}
		this.entryToRawId=entryToRawId;
	}

	/*/@SuppressWarnings("unchecked")
	@Overwrite
	public int getRawId(@Nullable T value) {
		Identifier id=((Registry<Registry<?>>) Registries.REGISTRIES).getId(this);
		System.out.println(id+" "+entryToRawId.isEmpty());
		if (entryToRawId.isEmpty()){
			unregistry$finalize();
		}
		return entryToRawId.getInt(value);
	}/*/
}
