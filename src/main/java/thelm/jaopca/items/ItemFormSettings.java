package thelm.jaopca.items;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import com.google.common.base.Function;

import net.minecraft.item.EnumRarity;
import thelm.jaopca.api.forms.IFormType;
import thelm.jaopca.api.items.IItemCreator;
import thelm.jaopca.api.items.IItemFormSettings;
import thelm.jaopca.api.materials.IMaterial;

public class ItemFormSettings implements IItemFormSettings {

	ItemFormSettings() {}

	private IItemCreator itemCreator = JAOPCAItem::new;
	private ToIntFunction<IMaterial> itemStackLimitFunction = material->64;
	private Predicate<IMaterial> hasEffectFunction = material->material.hasEffect();
	private Function<IMaterial, EnumRarity> displayRarityFunction = material->material.getDisplayRarity();
	private ToIntFunction<IMaterial> burnTimeFunction = material->-1;

	@Override
	public IFormType getType() {
		return ItemFormType.INSTANCE;
	}

	@Override
	public IItemFormSettings setItemCreator(IItemCreator itemCreator) {
		this.itemCreator = itemCreator;
		return this;
	}

	@Override
	public IItemCreator getItemCreator() {
		return itemCreator;
	}

	@Override
	public IItemFormSettings setItemStackLimitFunction(ToIntFunction<IMaterial> itemStackLimitFunction) {
		this.itemStackLimitFunction = itemStackLimitFunction;
		return this;
	}

	@Override
	public ToIntFunction<IMaterial> getItemStackLimitFunction() {
		return itemStackLimitFunction;
	}

	@Override
	public IItemFormSettings setHasEffectFunction(Predicate<IMaterial> hasEffectFunction) {
		this.hasEffectFunction = hasEffectFunction;
		return this;
	}

	@Override
	public Predicate<IMaterial> getHasEffectFunction() {
		return hasEffectFunction;
	}

	@Override
	public IItemFormSettings setDisplayRarityFunction(Function<IMaterial, EnumRarity> displayRarityFunction) {
		this.displayRarityFunction = displayRarityFunction;
		return this;
	}

	@Override
	public Function<IMaterial, EnumRarity> getDisplayRarityFunction() {
		return displayRarityFunction;
	}
}
