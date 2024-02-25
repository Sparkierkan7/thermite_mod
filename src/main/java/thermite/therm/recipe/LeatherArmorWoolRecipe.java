package thermite.therm.recipe;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import thermite.therm.ThermMod;

public class LeatherArmorWoolRecipe extends SpecialCraftingRecipe {

    public LeatherArmorWoolRecipe(Identifier identifier, CraftingRecipeCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    @Override
    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        /*boolean bl = false;
        int i = 0;
        for (int j = 0; j < recipeInputInventory.size(); ++j) {
            ItemStack itemStack = recipeInputInventory.getStack(j);
            if (itemStack.isEmpty()) continue;
            if (PAPER.test(itemStack)) {
                if (bl) {
                    return false;
                }
                bl = true;
                continue;
            }
            if (!(DURATION_MODIFIER.test(itemStack) ? ++i > 3 : !FIREWORK_STAR.test(itemStack))) continue;
            return false;
        }
        return bl && i >= 1;*/

        byte wools = 0;
        byte armors = 0;

        for (int j = 0; j < recipeInputInventory.size(); j++) {
            ItemStack itemStack = recipeInputInventory.getStack(j);
            wools += (byte) (itemStack.isOf(ThermMod.WOOL_CLOTH_ITEM) ? 1 : 0);
            armors += (byte) (itemStack.isOf(Items.LEATHER_HELMET) || itemStack.isOf(Items.LEATHER_CHESTPLATE) || itemStack.isOf(Items.LEATHER_LEGGINGS) || itemStack.isOf(Items.LEATHER_BOOTS) ? 1 : 0);
        }
        return wools == 1 && armors == 1;

    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack inputArmor = new ItemStack(Items.AIR, 1);
        for (int i = 0; i < recipeInputInventory.size(); i++) {
            ItemStack it = recipeInputInventory.getStack(i);
            if (it.isOf(Items.LEATHER_HELMET) || it.isOf(Items.LEATHER_CHESTPLATE) || it.isOf(Items.LEATHER_LEGGINGS) || it.isOf(Items.LEATHER_BOOTS)) { inputArmor = recipeInputInventory.getStack(i); }
        }
        int max = 2;
        /*if (inputArmor.isOf(Items.LEATHER_HELMET) || inputArmor.isOf(Items.LEATHER_BOOTS)) {
            max = 2;
        } else {
            max = 2;
        }*/
        ItemStack stack = new ItemStack(inputArmor.getItem(), 1);
        NbtCompound nbt = inputArmor.getOrCreateNbt().copy();
        if (nbt.getInt("wool") < max) {
            nbt.putInt("wool", nbt.getInt("wool") + 1);
        } else {
            stack = new ItemStack(Items.AIR, 1);
        }
        stack.setNbt(nbt);
        return stack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return new ItemStack(Items.LEATHER_HELMET);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ThermMod.LEATHER_ARMOR_WOOL_RECIPE_SERIALIZER;
    }
}