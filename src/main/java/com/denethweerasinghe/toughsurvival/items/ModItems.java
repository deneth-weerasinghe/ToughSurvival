package com.denethweerasinghe.toughsurvival.items;

import com.denethweerasinghe.toughsurvival.setup.ToughSurvival;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@net.minecraftforge.registries.ObjectHolder(ToughSurvival.MOD_ID)
public class ModItems {

    public static class Drink extends Item{

        private final int hydration;

        Drink(Properties properties, int hunger, float saturation, int value) {
            super(properties.food((new Food.Builder()).hunger(hunger).saturation(saturation).build()).group(ItemGroup.FOOD));
            hydration = value;
        }

        public int getHydration(){
            return hydration;
        }
    }

    public final static Item APPLE_JUICE = null;
    public final static Item BERRY_JUICE = null;
    public final static Item WATERMELON_JUICE = null;
    public final static Item PUMPKIN_JUICE = null;
    public final static Item CARROT_JUICE = null;
    public final static Item SALT_WATER = null;

    public static Item appleJuice = new Drink(new Item.Properties(), 0, 0, 6);
    public static Item berryJuice = new Drink(new Item.Properties(), 0, 0, 6);
    public static Item waterMelonJuice = new Drink(new Item.Properties(), 0, 0, 7);
    public static Item pumpkinJuice = new Drink(new Item.Properties(), 0, 0, 5);
    public static Item carrotJuice = new Drink(new Item.Properties(), 0, 0, 5);
    public static Item saltWater = new Drink(new Item.Properties(), 0, 0, 0);
    public static Item hotChocolate = new Drink(new Item.Properties(), 3, 0.5F, 2);
}
