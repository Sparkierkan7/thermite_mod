package thermite.therm;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;

public class ThermConfig extends Config {

    //seasons
    @ConfigEntry(comment = "A small built in season system that affects your temperature depending on the season. You can configure the length of each season in half seconds, (one minecraft day = 2400 half seconds).")
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
    @ConfigEntry(comment = "(Experimental) makes weather reflect the current season. If you enable this make sure to run (/gamerule doWeatherCycle false) to disable the vanilla weather cycle.")
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

    //game
    @ConfigEntry(comment = "Multiplier for how much each level of fire protection cools you (Default: 0.5)")
    public float fireProtectionCoolingMultiplier = 0.5f;

    @ConfigEntry(comment = "Hyperthermia damage per 5 seconds. (Default: 1.0)")
    public float hyperthermiaDamage = 1.0f;

    @ConfigEntry(comment = "Hypothermia damage per 5 seconds. (Default: 1.0)")
    public float hypothermiaDamage = 1.0f;

    public ThermConfig() {
        super(ConfigOptions.mod(ThermMod.modid));
    }

}