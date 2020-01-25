package com.example.toughsurvival.items;

import com.example.toughsurvival.setup.ToughSurvival;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@net.minecraftforge.registries.ObjectHolder(ToughSurvival.MOD_ID)
public class ModItems {

    public static class Drink extends Item {
        // drinks will only have a hydration value but no hunger associated values, hence the zeros
        static Food DRINK_ITEM = (new Food.Builder()).hunger(0).saturation(0).build();

        Drink(Properties properties) {
            super(properties.food(DRINK_ITEM).group(ItemGroup.FOOD));
        }
    }

    public static class FoodDrink extends Item{

        FoodDrink(Properties properties, int hunger, float saturation) {
            super(properties.food((new Food.Builder()).hunger(hunger).saturation(saturation).build()));
        }
    }

    public final static Item apple_juice = null;
    public final static Item berry_juice = null;
    public final static Item watermelon_juice = null;
    public final static Item pumpkin_juice = null;
    public final static Item carrot_juice = null;
    public final static Item salt_water = null;

    public static Item appleJuice = new Drink(new Item.Properties());
    public static Item berryJuice = new Drink(new Item.Properties());
    public static Item waterMelonJuice = new Drink(new Item.Properties());
    public static Item pumpkinJuice = new Drink(new Item.Properties());
    public static Item carrotJuice = new Drink(new Item.Properties());
    public static Item saltWater = new Drink(new Item.Properties());
    public static Item hotChocolate = new FoodDrink(new Item.Properties(), 3, 0.5F);





}
