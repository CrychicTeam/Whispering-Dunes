package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import org.crychicteam.dunes.content.item.cactus.CactusFruits;
import org.crychicteam.dunes.content.item.cactus.CactusJam;
import org.crychicteam.dunes.content.item.cactus.DwarfCactusSeedItem;

import static org.crychicteam.dunes.Dunes.REGISTRATE;

public class DunesItem {

    public static final ItemEntry<CactusFruits> GIANT_CACTUS_FRUIT;
    public static final ItemEntry<CactusFruits> DWARF_CACTUS_FRUIT;
    public static final ItemEntry<CactusJam> CACTUS_JAM;
    public static final ItemEntry<CactusJam> CACTUS_FRUIT_JAM;

    public static final ItemEntry<DwarfCactusSeedItem> DWARF_CACTUS_SEED;

    static  {
        GIANT_CACTUS_FRUIT = REGISTRATE.item("giant_cactus_fruit" , CactusFruits::new)
                .initialProperties(() -> new Item.Properties().food(new FoodProperties.Builder()
                        .nutrition(4)
                        .saturationMod(1.5f)
                        .build()))
                .properties(p -> p.stacksTo(64))
                .defaultModel()
                .defaultLang()
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), "item/generated")
                        .texture("layer0", "item/cactus/" + ctx.getName()))
                .tab(CreativeModeTabs.FOOD_AND_DRINKS)
                .register();

        DWARF_CACTUS_FRUIT = REGISTRATE.item("dwarf_cactus_fruit", CactusFruits::new)
                .initialProperties(() -> new Item.Properties().food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationMod(1.0f)
                        .alwaysEat()
                        .fast()
                        .build()))
                .properties(p -> p.stacksTo(64))
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), "item/generated")
                        .texture("layer0", "item/cactus/" + ctx.getName()))
                .defaultLang()
                .tab(CreativeModeTabs.FOOD_AND_DRINKS)
                .register();

        CACTUS_JAM = REGISTRATE.item("cactus_jam", CactusJam::new)
                .initialProperties(() -> new Item.Properties().food(new FoodProperties.Builder()
                        .nutrition(4)
                        .saturationMod(2.0f)
                        .alwaysEat()
                        .build()))
                .properties(p -> p.stacksTo(1))
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), "item/generated")
                        .texture("layer0", "item/cactus/" + ctx.getName()))
                .defaultLang()
                .tab(CreativeModeTabs.FOOD_AND_DRINKS)
                .register();

        CACTUS_FRUIT_JAM = REGISTRATE.item("cactus_fruit_jam", CactusJam::new)
                .initialProperties(() -> new Item.Properties().food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationMod(1.5f)
                        .alwaysEat()
                        .build()))
                .properties(p -> p.stacksTo(1))
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), "item/generated")
                        .texture("layer0", "item/cactus/" + ctx.getName()))
                .defaultLang()
                .tab(CreativeModeTabs.FOOD_AND_DRINKS)
                .register();

        DWARF_CACTUS_SEED = REGISTRATE.item("dwarf_cactus_seed", props ->
                        new DwarfCactusSeedItem())
                .initialProperties(Item.Properties::new)
                .properties(p -> p.stacksTo(64))
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), "item/generated")
                        .texture("layer0", "item/cactus/" + ctx.getName()))
                .defaultLang()
                .tab(CreativeModeTabs.INGREDIENTS)
                .register();
    }

    public static void register() {
    }
}