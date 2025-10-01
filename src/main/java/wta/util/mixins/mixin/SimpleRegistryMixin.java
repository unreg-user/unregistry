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
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wta.util.AllInit;
import wta.util.mixins.interfaces.SimpleRegistryFIInner;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;
import wta.util.utils.RegType;
import wta.util.utils.UnregistryNull;

import java.util.*;

import static wta.util.Unregistry.LOGGER;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements MutableRegistry<T>, SimpleRegistryFI<T>, SimpleRegistryFIInner<T> {
	@Shadow @Final @Mutable private Map<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry;
	@Shadow @Final @Mutable private Map<Identifier, RegistryEntry.Reference<T>> idToEntry;
	@Shadow @Final @Mutable private Map<T, RegistryEntry.Reference<T>> valueToEntry;
	@Shadow @Final @Mutable private ObjectList<RegistryEntry.Reference<T>> rawIdToEntry;
	@Shadow @Final @Mutable private Reference2IntMap<T> entryToRawId;
	@Shadow @Final @Mutable private Map<RegistryKey<T>, RegistryEntryInfo> keyToEntryInfo;

	@Shadow private Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;
	@Shadow private Lifecycle lifecycle;

	@Shadow protected abstract void assertNotFrozen();


	@Unique private T unregistryNullValue=null;
	@Unique private final HashSet<Identifier> oldIds=new HashSet<>();

	/**
	 * @author UnregUser
	 * @reason optimization and other...
	 */
	@Overwrite
	public RegistryEntry.Reference<T> add(RegistryKey<T> key, T value, RegistryEntryInfo info){
		this.assertNotFrozen();

		Objects.requireNonNull(key);
		Objects.requireNonNull(value);

		if (this.idToEntry.containsKey(key.getValue())) {
			Util.throwOrPause(new IllegalStateException("Cannot adding duplicate key '" + key + "' to registry"));
		}
		if (this.valueToEntry.containsKey(value)) {
			Util.throwOrPause(new IllegalStateException("Cannot adding duplicate value '" + value + "' to registry"));
		}
		if (value instanceof UnregistryNull){
			Util.throwOrPause(new NullPointerException("UnregistryNull cannot registry"));
		}

		RegistryEntry.Reference<T> reference=unregistry$findReference(key, value);

		if (this.unregistry$register(key, value, info, reference)){
			return reference;
		}
		throw Util.throwOrPause(new IllegalStateException("what?"));
	}

	@Override
	public T unregistry$add(RegistryKey<T> key, RegType type, T value, RegistryEntryInfo info) {
		this.assertNotFrozen();
		Objects.requireNonNull(key);
		RegistryEntry.Reference<T> reference=null;

		if (type.isRegister()){
			Objects.requireNonNull(value);
			reference=unregistry$findReference(key, value);
			if (this.valueToEntry.containsKey(value)) {
				Util.throwOrPause(new IllegalStateException("Adding duplicate value '" + value + "' to registry"));
			}
			if (value instanceof UnregistryNull){
				Util.throwOrPause(new NullPointerException("UnregistryNull cannot registry"));
			}
		}else{
			if (value!=null){
				LOGGER.warn("Not null RegistryKey with {}", type);
			}
		}

		return switch (type){
			case REG -> {
				if (this.unregistry$register(key, value, info, reference)){
					yield value;
				}
				throw Util.throwOrPause(new IllegalStateException("Cannot adding duplicate key '" + key + "' to registry"));
			}
			case REREG -> {
				if (this.unregistry$reregister(key, value, info, reference)){
					yield value;
				}
				throw Util.throwOrPause(new IllegalStateException("Key not found "+key));
			}
			case REG_ALWAYS -> {
				if (this.unregistry$register(key, value, info, reference) || this.unregistry$reregister(key, value, info, reference)) {
					yield value;
				}
				throw Util.throwOrPause(new IllegalStateException("what?"));
			}
			case UNREG -> {
				if (this.unregistry$unregister(key)){
					yield unregistryNullValue;
				}
				throw Util.throwOrPause(new IllegalStateException("Key not found "+key));
			}
			case UNREG_OR_NONE -> {
				this.unregistry$unregister(key);
				yield unregistryNullValue;
			}
		};
	}

	@Override
	public RegistryEntry.Reference<T> unregistry$findReference(RegistryKey<T> key, T value){
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

	@Override
	public boolean unregistry$reregister(RegistryKey<T> key, T value, RegistryEntryInfo info, RegistryEntry.Reference<T> reference){
		Optional<RegistryEntry.Reference<T>> oldValueO=this.getEntry(key);
		if (oldValueO.isEmpty()) return false;
		RegistryEntry.Reference<T> oldValueR = oldValueO.get();
		T oldValue = oldValueR.value();
		int i = rawIdToEntry.indexOf(oldValueR);
		rawIdToEntry.set(i, reference);
		valueToEntry.remove(oldValue);
		keyToEntry.put(key, reference);
		idToEntry.put(key.getValue(), reference);
		valueToEntry.put(value, reference);
		keyToEntryInfo.put(key, info);
		return true;
	}

	@Override
	public boolean unregistry$unregister(RegistryKey<T> key){
		Optional<RegistryEntry.Reference<T>> oldValueO=this.getEntry(key);
		if (oldValueO.isEmpty()) return false;
		RegistryEntry.Reference<T> oldValueR = oldValueO.get();
		T oldValue = oldValueR.value();
		int i = rawIdToEntry.indexOf(oldValueR);
		rawIdToEntry.set(i, null);
		valueToEntry.remove(oldValue);
		keyToEntry.remove(key);
		idToEntry.remove(key.getValue());
		keyToEntryInfo.remove(key);
		oldIds.add(key.getValue());
		return true;
	}

	@Override
	public boolean unregistry$register(RegistryKey<T> key, T value, RegistryEntryInfo info, RegistryEntry.Reference<T> reference){
		if (this.idToEntry.containsKey(key.getValue())) {
			return false;
		}

		this.keyToEntry.put(key, reference);
		this.idToEntry.put(key.getValue(), reference);
		this.valueToEntry.put(value, reference);
		this.rawIdToEntry.add(reference);
		this.keyToEntryInfo.put(key, info);
		return true;
	}

	@Override
	public void unregistry$registerUnregistryNull(T value) {
		if (value==null) Util.throwOrPause(new NullPointerException());
		if (!(value instanceof UnregistryNull)) Util.throwOrPause(new IllegalStateException("Value is not UnregistryNull"));
		unregistryNullValue =value;
	}

	@Override
	public void unregistry$removeIntrusiveValueToEntry(T value) {
		intrusiveValueToEntry.remove(value);
	}

	@Override
	public void unregistry$finalize() {
		Reference2IntArrayMap<T> entryToRawId=new Reference2IntArrayMap<>();
		rawIdToEntry.removeIf(Objects::isNull);
		for (int i=0; i<rawIdToEntry.size(); i++) {
			T objI=rawIdToEntry.get(i).value();
			entryToRawId.put(objI, i);
		}
		for (RegistryKey<T> keyI : keyToEntry.keySet()){
			this.lifecycle = this.lifecycle.add(keyToEntryInfo.get(keyI).lifecycle());
		}
		this.entryToRawId=entryToRawId;
	}

	@Inject(
		  method = "freeze",
		  at = @At(
				value = "INVOKE",
			    target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"
		  )
	)
	private void finalizer(CallbackInfoReturnable<Registry<T>> cir){
		unregistry$finalize();
	}
}
