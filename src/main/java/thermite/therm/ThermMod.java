package thermite.therm;

import me.lortseam.completeconfig.data.ConfigOptions;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thermite.therm.block.ThermBlocks;
import thermite.therm.item.GoldSweetBerriesItem;
import thermite.therm.item.IceJuiceItem;
import thermite.therm.item.ThermometerItem;
import thermite.therm.networking.ThermNetworkingPackets;

public class ThermMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("therm");
	public static final String modid = "therm";

	//items
	public static final GoldSweetBerriesItem GOLD_SWEET_BERRIES_ITEM = new GoldSweetBerriesItem(new FabricItemSettings().maxCount(64));
	public static final IceJuiceItem ICE_JUICE_ITEM = new IceJuiceItem(new FabricItemSettings().maxCount(16));
	public static final ThermometerItem THERMOMETER_ITEM = new ThermometerItem(new FabricItemSettings().maxCount(1));

	//block items
	public static final BlockItem ICE_BOX_EMPTY_ITEM = new BlockItem(ThermBlocks.ICE_BOX_EMPTY_BLOCK, new FabricItemSettings());
	public static final BlockItem ICE_BOX_FREEZING_ITEM = new BlockItem(ThermBlocks.ICE_BOX_FREEZING_BLOCK, new FabricItemSettings());
	public static final BlockItem ICE_BOX_FROZEN_ITEM = new BlockItem(ThermBlocks.ICE_BOX_FROZEN_BLOCK, new FabricItemSettings());

	//config
	public static final ThermConfig config = new ThermConfig();

	@Override
	public void onInitialize() {

		config.load();
		ConfigOptions.mod(modid).branch(new String[]{"branch", "config"});

		//items
		Registry.register(Registries.ITEM, new Identifier(modid, "gold_sweet_berries"), GOLD_SWEET_BERRIES_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "ice_juice"), ICE_JUICE_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "thermometer"), THERMOMETER_ITEM);

		//blocks
		Registry.register(Registries.BLOCK, new Identifier("therm", "ice_box_empty"), ThermBlocks.ICE_BOX_EMPTY_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier("therm", "ice_box_freezing"), ThermBlocks.ICE_BOX_FREEZING_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier("therm", "ice_box_frozen"), ThermBlocks.ICE_BOX_FROZEN_BLOCK);

		//block item registry
		Registry.register(Registries.ITEM, new Identifier("therm", "ice_box_empty_item"), ICE_BOX_EMPTY_ITEM);
		Registry.register(Registries.ITEM, new Identifier("therm", "ice_box_freezing_item"), ICE_BOX_FREEZING_ITEM);
		Registry.register(Registries.ITEM, new Identifier("therm", "ice_box_frozen_item"), ICE_BOX_FROZEN_ITEM);

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
		});

		ThermNetworkingPackets.registerC2SPackets();

		//events
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

			ServerState serverState = ServerState.getServerState(handler.player.getWorld().getServer());
			ThermPlayerState playerState = ServerState.getPlayerState(handler.player);

		});

		//server tick
		ServerTickEvents.START_SERVER_TICK.register((server) -> {
			ServerState serverState = ServerState.getServerState(server);

			if (config.enableSeasonSystem) {
				serverState.seasonTick += 1;
				if (serverState.seasonTick >= 10) {
					serverState.seasonTick = 0;

					if (serverState.season == 0) {
						serverState.currentSeasonTick += 1;
						if (serverState.currentSeasonTick >= config.springSeasonLength) {
							serverState.season = 1;
							serverState.currentSeasonTick = 0;
						}
					} else if (serverState.season == 1) {
						serverState.currentSeasonTick += 1;
						if (serverState.currentSeasonTick >= config.summerSeasonLength) {
							serverState.season = 2;
							serverState.currentSeasonTick = 0;
						}
					} else if (serverState.season == 2) {
						serverState.currentSeasonTick += 1;
						if (serverState.currentSeasonTick >= config.fallSeasonLength) {
							serverState.season = 3;
							serverState.currentSeasonTick = 0;
						}
					} else if (serverState.season == 3) {
						serverState.currentSeasonTick += 1;
						if (serverState.currentSeasonTick >= config.winterSeasonLength) {
							serverState.season = 0;
							serverState.currentSeasonTick = 0;
						}
					}

					if (config.seasonalWeather) {
						serverState.seasonalWeatherTick += 1;

						//LOGGER.info("season: " + serverState.season + " time: " + serverState.currentSeasonTick);

						if (serverState.seasonalWeatherTick >= 600) {
							serverState.seasonalWeatherTick = 0;

							int rain;
							int thunder;

							if (serverState.season == 0) {
								rain = ThermUtil.randInt(0, 4);
								thunder = ThermUtil.randInt(0, 8);
							} else if (serverState.season == 1) {
								rain = ThermUtil.randInt(0, 10);
								thunder = ThermUtil.randInt(0, 10);
							} else if (serverState.season == 2) {
								rain = ThermUtil.randInt(0, 2);
								thunder = ThermUtil.randInt(0, 6);
							} else if (serverState.season == 3) {
								rain = ThermUtil.randInt(0, 6);
								thunder = ThermUtil.randInt(0, 10);
							} else {
								rain = 1;
								thunder = 1;
							}

							server.getWorlds().forEach((world) -> {
								if (rain == 0) {
									world.setWeather(0, 1200, true, false);
								}
								if (thunder == 0) {
									world.setWeather(0, 1200, true, true);
								}
							});

							//LOGGER.info("rain: " + rain + " thunder: " + thunder + " season: " + serverState.season + " time: " + serverState.currentSeasonTick);

						}

					}

					//LOGGER.info("Season: " + serverState.season + " Time: " + serverState.currentSeasonTick);

				}
				serverState.markDirty();
			}
		});

	}
}