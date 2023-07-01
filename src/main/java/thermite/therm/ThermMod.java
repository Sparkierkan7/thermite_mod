package thermite.therm;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	@Override
	public void onInitialize() {

		//items
		Registry.register(Registries.ITEM, new Identifier(modid, "gold_sweet_berries"), GOLD_SWEET_BERRIES_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "ice_juice"), ICE_JUICE_ITEM);
		Registry.register(Registries.ITEM, new Identifier(modid, "thermometer"), THERMOMETER_ITEM);

		//item groups
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
			content.add(GOLD_SWEET_BERRIES_ITEM);
			content.add(ICE_JUICE_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.add(THERMOMETER_ITEM);
		});

		ThermNetworkingPackets.registerC2SPackets();

		//events
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

			ServerState serverState = ServerState.getServerState(handler.player.getWorld().getServer());
			ThermPlayerState playerState = ServerState.getPlayerState(handler.player);

		});

	}
}