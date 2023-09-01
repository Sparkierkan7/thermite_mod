package thermite.therm;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ThermConfig extends Config {

    //seasons
    @ConfigEntry(comment = "(Experimental) A small built in season system that affects your temperature depending on the season. You can configure the length of each season in half seconds, (one minecraft day = 2400 half seconds).")
    public boolean enableSeasonSystem = false;

    @ConfigEntry(comment = "Length of spring (Default: 48000 half seconds = 20 days).")
    public long springSeasonLength = 48000;

    @ConfigEntry(comment = "Length of summer (Default 48000 half seconds = 20 days).")
    public long summerSeasonLength = 48000;

    @ConfigEntry(comment = "Length of fall (Default 48000 half seconds = 20 days).")
    public long fallSeasonLength = 48000;

    @ConfigEntry(comment = "Length of winter (Default 48000 half seconds = 20 days).")
    public long winterSeasonLength = 48000;

    @ConfigEntry(comment = "This option doesn't work yet.")
    public String startingSeason = "spring";

    @ConfigEntry(comment = "Multiplier for how much seasons affect your temperature.")
    public float seasonTemperatureExtremenessMultiplier = 1.0f;


    //weather
    @ConfigEntry(comment = "(Experimental) Makes weather reflect the current season. If you enable this make sure to run (/gamerule doWeatherCycle false) to disable the vanilla weather cycle.")
    public boolean seasonalWeather = false;


    //gui
    @ConfigEntry(comment = "X coordinate of temperature UI relative to its default position. (Default: 0)")
    public int temperatureXPos = 0;

    @ConfigEntry(comment = "Y coordinate of temperature UI relative to its default position. (Default: 0)")
    public int temperatureYPos = 0;

    @ConfigEntry(comment = "X coordinate of thermometer UI relative to its default position. (Default: 0)")
    public int thermometerXPos = 0;

    @ConfigEntry(comment = "Y coordinate of thermometer UI relative to its default position. (Default: 0)")
    public int thermometerYPos = 0;

    @ConfigEntry(comment = "Different styles for the temperature display. (options: gauge, glass_thermometer)")
    public String temperatureDisplayType = "glass_thermometer";

    @ConfigEntry(comment = "Whether or not temperature damage decreases your saturation. Beware disabling this makes it really easy to bypass temperature damage just by eating. (Default: true)")
    public boolean temperatureDamageDecreasesSaturation = true;

    @ConfigEntry(comment = "When enabled, being cold enough causes a blue outline effect. And being hot enough causes an orange one. (Default: true)")
    public boolean enableTemperatureVignette = true;

    //game
    @ConfigEntry(comment = "Multiplier for how much each level of fire protection cools you (Default: 0.5)")
    public float fireProtectionCoolingMultiplier = 0.5f;

    @ConfigEntry(comment = "Hyperthermia damage per 5 seconds. (Default: 1.0)")
    public float hyperthermiaDamage = 1.5f;

    @ConfigEntry(comment = "Hypothermia damage per 5 seconds. (Default: 1.0)")
    public float hypothermiaDamage = 1.5f;

    @ConfigEntry(comment = "Helmets that will change your temperature.")
    public Map<String, Integer> helmetTempItems = new HashMap(Map.of("leather_helmet", 1));

    @ConfigEntry(comment = "Chestplates that will change your temperature.")
    public Map<String, Integer> chestplateTempItems = new HashMap(Map.of("leather_chestplate", 3));

    @ConfigEntry(comment = "Leggings that will change your temperature.")
    public Map<String, Integer> leggingTempItems = new HashMap(Map.of("leather_leggings", 2));

    @ConfigEntry(comment = "Boots that will change your temperature.")
    public Map<String, Integer> bootTempItems = new HashMap(Map.of("leather_boots", 1));

    @ConfigEntry(comment = "Items that when held will change your temperature.")
    public Map<String, Integer> heldTempItems = new HashMap(Map.of("torch", 3, "lava_bucket", 3));

    @ConfigEntry(comment = "Blocks that will heat you up when near.")
    public Map<String, Integer> heatingBlocks = new HashMap(Map.of("Block{minecraft:torch}", 3, "Block{minecraft:fire}", 3, "Block{minecraft:lava}", 8, "Block{minecraft:campfire}", 15, "Block{minecraft:wall_torch}", 3));

    @ConfigEntry(comment = "Blocks that will cool you down when near.")
    public Map<String, Integer> coolingBlocks = new HashMap(Map.of("Block{minecraft:ice}", 1, "Block{minecraft:packed_ice}", 3, "Block{minecraft:blue_ice}", 6));

    @ConfigEntry(comment = "Base temperature for frigid climates. (Default: 25.0)")
    public double frigidClimateTemp = 25;

    @ConfigEntry(comment = "Base temperature for cold climates. (Default: 30.0)")
    public double coldClimateTemp = 30;

    @ConfigEntry(comment = "Base temperature for temperate climates. (Default: 50.0)")
    public double temperateClimateTemp = 50;

    @ConfigEntry(comment = "Base temperature for hot climates. (Default: 55.0)")
    public double hotClimateTemp = 55;

    @ConfigEntry(comment = "Base temperature for arid climates. (Default: 70.0)")
    public double aridClimateTemp = 70;

    @ConfigEntry(comment = "First threshold for hypothermia, being below this you will start to freeze (Default: 35)")
    public int freezeThreshold1 = 35;

    @ConfigEntry(comment = "Second threshold for hypothermia, being below this you will freeze faster. (Default: 25)")
    public int freezeThreshold2 = 25;

    @ConfigEntry(comment = "First threshold for hyperthermia, being above this you will start to burn (Default: 65)")
    public int burnThreshold1 = 65;

    @ConfigEntry(comment = "Second threshold for hyperthermia, being above this you will burn faster (Default: 75)")
    public int burnThreshold2 = 75;

    @ConfigEntry(comment = "Damage interval for hypothermia and hyperthermia in seconds (Default: 3)")
    public int temperatureDamageInterval = 3;

    @ConfigEntry(comment = "Damage interval for extreme hypothermia and hyperthermia in seconds (Default: 2)")
    public int extremetemperatureDamageInterval = 2;

    //TODO enableCustomVignette option

    public ThermConfig() {
        super(ConfigOptions.mod(ThermMod.modid));
    }

}