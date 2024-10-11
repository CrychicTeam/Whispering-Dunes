package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import static org.crychicteam.dunes.Dunes.REGISTRATE;

public class DunesItem {

    public static final ItemEntry<Item> GIANT_CACTUS_FRUIT;

    static  {
        GIANT_CACTUS_FRUIT = REGISTRATE.item("giant_cactus_fruit" , Item::new)
                .initialProperties(() -> new Item.Properties().food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationMod(1.5f)
                        .fast()
                        .build()))
                .properties(p -> p.stacksTo(64))
                .defaultModel()
                .lang("Giant Cactus Fruit")
                .tab(CreativeModeTabs.FOOD_AND_DRINKS)
                .register();
    }

    public static void register() {
    }
}