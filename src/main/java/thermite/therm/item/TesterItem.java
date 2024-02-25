package thermite.therm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import thermite.therm.ThermMod;

public class TesterItem extends Item {

    public TesterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        ThermMod.LOGGER.info("STATE: " + context.getWorld().getBlockState(context.getBlockPos()).toString());
        ThermMod.LOGGER.info("BLOCK: " + context.getWorld().getBlockState(context.getBlockPos()).getBlock().toString());

        return super.useOnBlock(context);
    }
}
