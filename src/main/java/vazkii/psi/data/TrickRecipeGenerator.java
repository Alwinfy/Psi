package vazkii.psi.data;

import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.Tags;
import vazkii.psi.api.recipe.TrickRecipeBuilder;
import vazkii.psi.common.Psi;
import vazkii.psi.common.crafting.recipe.DimensionTrickRecipe;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceNames;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class TrickRecipeGenerator extends RecipeProvider {
	public TrickRecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		TrickRecipeBuilder.of(ModItems.psidust).input(Tags.Items.DUSTS_REDSTONE).cad(ModItems.cadAssemblyIron).build(consumer);

		TrickRecipeBuilder.of(ModItems.psimetal)
				.input(Tags.Items.INGOTS_GOLD)
				.trick(Psi.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron).build(consumer);

		TrickRecipeBuilder.of(ModItems.psigem)
				.input(Tags.Items.GEMS_DIAMOND)
				.trick(Psi.location(LibPieceNames.TRICK_GREATER_INFUSION))
				.cad(ModItems.cadAssemblyPsimetal).build(consumer);

		TrickRecipeBuilder builder = TrickRecipeBuilder.of(ModItems.ebonySubstance)
				.input(ItemTags.COALS)
				.trick(Psi.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal);
		dimension(builder, consumer, ModItems.ebonySubstance.getRegistryName(), DimensionType.THE_END);

		builder = TrickRecipeBuilder.of(ModItems.ivorySubstance)
				.input(Tags.Items.GEMS_QUARTZ)
				.trick(Psi.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal);
		dimension(builder, consumer, ModItems.ivorySubstance.getRegistryName(), DimensionType.THE_END);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Psi trick crafting recipes";
	}

	public static void dimension(TrickRecipeBuilder builder, Consumer<IFinishedRecipe> parent,
								 ResourceLocation id, DimensionType type) {
		parent.accept(new DimensionResult(id, builder, type));
	}

	public static class DimensionResult extends TrickRecipeBuilder.Result {
		private ResourceLocation dimensionId;

		protected DimensionResult(ResourceLocation id, TrickRecipeBuilder builder, DimensionType type) {
			super(id, builder);
			this.dimensionId = type.getRegistryName();
		}

		@Override
		public void serialize(@Nonnull JsonObject json) {
			super.serialize(json);
			json.addProperty("dimension", dimensionId.toString());
		}

		@Nonnull
		@Override
		public IRecipeSerializer<?> getSerializer() {
			return DimensionTrickRecipe.SERIALIZER;
		}
	}
}
