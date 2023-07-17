package thermite.therm;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;

public class ThermConfig extends Config {

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


    @ConfigEntry(comment = "X coordinate of temperature UI relative to its default position.")
    public int temperatureXPos = 0;

    @ConfigEntry(comment = "Y coordinate of temperature UI relative to its default position.")
    public int temperatureYPos = 0;

    @ConfigEntry(comment = "X coordinate of thermometer UI relative to its default position.")
    public int thermometerXPos = 0;

    @ConfigEntry(comment = "Y coordinate of thermometer UI relative to its default position.")
    public int thermometerYPos = 0;

    public ThermConfig() {
        super(ConfigOptions.mod(ThermMod.modid));
    }

}