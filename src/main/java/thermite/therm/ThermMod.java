package thermite.therm;

import me.lortseam.completeconfig.data.ConfigOptions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thermite.therm.block.ThermBlocks;
import thermite.therm.block.entity.FireplaceBlockEntity;
import thermite.therm.effect.ThermStatusEffects;
import thermite.therm.item.*;
import thermite.therm.networking.ThermNetworkingPackets;
import thermite.therm.recipe.LeatherArmorWoolRecipe;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//TODO: Add default values to persistent states.
//TODO: PURIFY main temperature ticking.
//TODO: Biome climate overrider in config.

public class ThermMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("therm");
	public static final String modid = "therm";
	public static final String modVersion = "5.0.0.3";

	//items
	public static final GoldSweetBerriesItem GOLD_SWEET_BERRIES_ITEM = new GoldSweetBerriesItem(new FabricItemSettings().maxCount(64));
	public static final IceJuiceItem ICE_JUICE_ITEM = new IceJuiceItem(new FabricItemSettings().maxCount(16));
	public static final ThermometerItem THERMOMETER_ITEM = new ThermometerItem(new FabricItemSettings().maxCount(1));
	public static final WoolClothItem WOOL_CLOTH_ITEM = new WoolClothItem(new FabricItemSettings().maxCount(64));
	public static final TesterItem TESTER_ITEM = new TesterItem(new FabricItemSettings().maxCount(1));

	//block items
	public static final BlockItem ICE_BOX_EMPTY_ITEM = new BlockItem(ThermBlocks.ICE_BOX_EMPTY_BLOCK, new FabricItemSettings());
	public static final BlockItem ICE_BOX_FREEZING_ITEM = new BlockItem(ThermBlocks.ICE_BOX_FREEZING_BLOCK, new FabricItemSettings());
	public static final BlockItem ICE_BOX_FROZEN_ITEM = new BlockItem(ThermBlocks.ICE_BOX_FROZEN_BLOCK, new FabricItemSettings());
	public static final BlockItem FIREPLACE_ITEM = new BlockItem(ThermBlocks.FIREPLACE_BLOCK, new FabricItemSettings());

	//block entities
	//public static final BlockEntityType<FireplaceBlockEntity> FIREPLACE_BLOCK_ENTITY = null;
	public static final BlockEntityType<FireplaceBlockEntity> FIREPLACE_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier(modid, "fireplace_block_entity"),
			FabricBlockEntityTypeBuilder.create(FireplaceBlockEntity::new, ThermBlocks.FIREPLACE_BLOCK).build()
	);

	//special recipes
	public static final RecipeSerializer<LeatherArmorWoolRecipe> LEATHER_ARMOR_WOOL_RECIPE_SERIALIZER = RecipeSerializer.register("crafting_special_leather_armor_wool", new SpecialRecipeSerializer<LeatherArmorWoolRecipe>(LeatherArmorWoolRecipe::new));

	//config
	public static final ThermConfig config = new ThermConfig();

	@Override
	public void onInitialize() {

		config.load();
		ConfigOptions.mod(modid).branch(new String[]{"branch", "config"});

		//status effects
		Registry.register(Registries.STATUS_EFFECT, new Identifier(modid, "cooling"), ThermStatusEffects.COOLING);

		//items
		Registry.register(Registries.ITEM, new Identifier(modid, "gold_sweet_berries"), GOLD_SWEET_BERRIES_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "ice_juice"), ICE_JUICE_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "thermometer"), THERMOMETER_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "wool_cloth"), WOOL_CLOTH_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "tester_item"), TESTER_ITEM);

		//blocks
		Registry.register(Registries.BLOCK, new Identifier(modid, "ice_box_empty"), ThermBlocks.ICE_BOX_EMPTY_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(modid, "ice_box_freezing"), ThermBlocks.ICE_BOX_FREEZING_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(modid, "ice_box_frozen"), ThermBlocks.ICE_BOX_FROZEN_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(modid, "fireplace"), ThermBlocks.FIREPLACE_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(modid, "smoke"), ThermBlocks.SMOKE_BLOCK);

		//block item registry
		Registry.register(Registries.ITEM, new Identifier(modid, "ice_box_empty_item"), ICE_BOX_EMPTY_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "ice_box_freezing_item"), ICE_BOX_FREEZING_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "ice_box_frozen_item"), ICE_BOX_FROZEN_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "fireplace_item"), FIREPLACE_ITEM);

		//item groups
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
			content.add(GOLD_SWEET_BERRIES_ITEM);
			content.add(ICE_JUICE_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.add(THERMOMETER_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
			content.add(ICE_BOX_EMPTY_ITEM);
			content.add(FIREPLACE_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
			content.add(WOOL_CLOTH_ITEM);
		});

		ThermNetworkingPackets.registerC2SPackets();

		//events
		EventListeners.register();

		//commands
		Commands.register();

	}
}