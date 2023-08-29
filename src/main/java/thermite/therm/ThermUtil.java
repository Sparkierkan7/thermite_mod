package thermite.therm;

import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import thermite.therm.util.BlockStatePosPair;

import java.util.ArrayList;

public class ThermUtil {

    public static String getClimate(float temp) {

        String climate = "n";

        if (temp < 0.0) {
            climate = "frigid";
        } else if (temp < 0.31 && temp >= 0.0) {
            climate = "cold";
        } else if (temp < 0.9 && temp >= 0.31) {
            climate = "temperate";
        } else if (temp < 2.0 && temp > 0.8) {
            climate = "hot";
        } else if (temp >= 2.0) {
            climate = "arid";
        }

        return climate;

    }

    public static ArrayList<BlockStatePosPair> getBlockBox(World world, int x1, int y1, int z1, int x2, int y2, int z2) {

        ArrayList<BlockStatePosPair> arr = new ArrayList<>();

        int y = 0;
        int z = 0;

        int width = Math.abs((x2 - x1));
        int height = Math.abs((y2 - y1));
        int depth = Math.abs((z2 - z1));

        for (int x = 0; x < width;) {

            for (y = 0; y < height;) {

                for (z = 0; z < depth;) {

                    arr.add(new BlockStatePosPair(world.getBlockState(new BlockPos(x1 + x, y1 + y, z1 + z)), new BlockPos(x1 + x, y1 + y, z1 + z)));

                    z++;

                }

                y++;

            }

            x++;

        }

        return arr;

    }

    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
