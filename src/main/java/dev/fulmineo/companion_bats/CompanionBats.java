package dev.fulmineo.companion_bats;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.fulmineo.companion_bats.entity.CompanionBatEntity;
import dev.fulmineo.companion_bats.entity.DynamiteEntity;
import dev.fulmineo.companion_bats.init.CompanionBatCommandInit;
import dev.fulmineo.companion_bats.init.CompanionBatLootTableInit;
import dev.fulmineo.companion_bats.item.CompanionBatArmorItem;
import dev.fulmineo.companion_bats.item.CompanionBatCommandFluteAttackItem;
import dev.fulmineo.companion_bats.item.CompanionBatCommandFluteGuardItem;
import dev.fulmineo.companion_bats.item.CompanionBatCommandFluteRestItem;
import dev.fulmineo.companion_bats.item.CompanionBatExperiencePieItem;
import dev.fulmineo.companion_bats.item.CompanionBatAccessoryItem;
import dev.fulmineo.companion_bats.item.CompanionBatFluteItem;
import dev.fulmineo.companion_bats.item.CompanionBatItem;
import dev.fulmineo.companion_bats.screen.CompanionBatScreenHandler;

public class CompanionBats implements ModInitializer {

	public static Logger LOGGER = LogManager.getLogger();

    // Identifiers

    public static final String MOD_ID = "companion_bats";

    public static final ScreenHandlerType<CompanionBatScreenHandler> BAT_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "bat_item"), CompanionBatScreenHandler::new);

    // Entities

    public static final EntityType<CompanionBatEntity> COMPANION_BAT = Registry.register(
        Registry.ENTITY_TYPE,
        new Identifier(MOD_ID, "bat"),
        FabricEntityTypeBuilder.<CompanionBatEntity>create(SpawnGroup.CREATURE, CompanionBatEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<DynamiteEntity> DYNAMITE = Registry.register(
        Registry.ENTITY_TYPE,
        new Identifier(MOD_ID, "dynamite"),
		FabricEntityTypeBuilder.<DynamiteEntity>create(SpawnGroup.MISC, DynamiteEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeChunks(4).trackedUpdateRate(10).build()
	);

    // Items

	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID,"group"), () -> new ItemStack(Registry.ITEM.get(new Identifier(MOD_ID,"bat_item"))));

    public static final Item BAT_ITEM = new CompanionBatItem(new FabricItemSettings().maxDamage((int)CompanionBatEntity.getMaxLevelHealth()).group(GROUP));
	public static final Item BAT_FLUTE_ITEM = new CompanionBatFluteItem(new FabricItemSettings().maxCount(1));
	public static final Item COMMAND_FLUTE_ATTACK = new CompanionBatCommandFluteAttackItem(new FabricItemSettings().maxCount(1).group(GROUP));
	public static final Item COMMAND_FLUTE_REST = new CompanionBatCommandFluteRestItem(new FabricItemSettings().maxCount(1));
	public static final Item COMMAND_FLUTE_GUARD = new CompanionBatCommandFluteGuardItem(new FabricItemSettings().maxCount(1));
    public static final Item SPIRIT_SHARD = new Item(new FabricItemSettings().group(GROUP));
    public static final Item SPIRIT_CRYSTAL = new Item(new FabricItemSettings().group(GROUP));
    public static final Item EXPERIENCE_PIE = new CompanionBatExperiencePieItem(new FabricItemSettings().food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.5F).build()).rarity(Rarity.UNCOMMON).group(GROUP));

	// Accessories

	public static final Item BUNNY_EARS = new CompanionBatAccessoryItem("bunny_ears", CompanionBatAbility.CANNOT_ATTACK, 1, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item WITHER_MASK = new CompanionBatAccessoryItem("wither_mask", CompanionBatAbility.ATTACK_EVERYONE, 1, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item DECORATIVE_FLOWER = new CompanionBatAccessoryItem("decorative_flower", CompanionBatAbility.ATTACK_HOSTILES, 1, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item WOLF_PELT = new CompanionBatAccessoryItem("wolf_pelt", CompanionBatAbility.ATTACK_PASSIVE, 1, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item PLATED_BOOTS = new CompanionBatAccessoryItem("plated_boots", CompanionBatAbility.INCREASED_ARMOR, 1, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item PUMPKIN_BANDANA = new CompanionBatAccessoryItem("pumpkin_bandana", CompanionBatAbility.NATURAL_REGENERATION, 1, new FabricItemSettings().group(GROUP).maxCount(1));

	// Armors

	public static final Item INFERNO_SUIT = new CompanionBatArmorItem("inferno_suit", CompanionBatClass.INFERNO, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item VAMPIRIC_ATTIRE = new CompanionBatArmorItem("vampiric_attire", CompanionBatClass.VAMPIRE, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item LOOTER_JACKET = new CompanionBatArmorItem("looter_jacket", CompanionBatClass.LOOTER, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item KNIGHT_PLATE = new CompanionBatArmorItem("knight_plate", CompanionBatClass.KNIGHT, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item ALCHEMIST_ROBE = new CompanionBatArmorItem("alchemist_robe", CompanionBatClass.ALCHEMIST, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item DUELIST_COSTUME = new CompanionBatArmorItem("duelist_costume", CompanionBatClass.DUELIST, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item NINJA_GARB = new CompanionBatArmorItem("ninja_garb", CompanionBatClass.NINJA, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item MUMMY_BANDAGES = new CompanionBatArmorItem("mummy_bandages", CompanionBatClass.MUMMY, new FabricItemSettings().group(GROUP).maxCount(1));
	public static final Item DESTROYER_GEAR = new CompanionBatArmorItem("destroyer_gear", CompanionBatClass.DESTROYER, new FabricItemSettings().group(GROUP).maxCount(1));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(COMPANION_BAT, CompanionBatEntity.createMobAttributes());

		// Items

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bat_item"), 		  	BAT_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bat_flute"), 	 	  	BAT_FLUTE_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "command_flute"), 		COMMAND_FLUTE_ATTACK);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "command_flute_rest"), 	COMMAND_FLUTE_REST);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "command_flute_guard"), COMMAND_FLUTE_GUARD);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "spirit_shard"), 	  	SPIRIT_SHARD);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "spirit_crystal"),    	SPIRIT_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "experience_pie"),  	EXPERIENCE_PIE);

		// Accessories

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bunny_ears"), 		  	BUNNY_EARS);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "wither_mask"), 	  	WITHER_MASK);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "decorative_flower"), 	DECORATIVE_FLOWER);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "wolf_pelt"), 		  	WOLF_PELT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "plated_boots"), 		PLATED_BOOTS);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pumpkin_bandana"), 	PUMPKIN_BANDANA);

		// Armors

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "inferno_suit"), 	  	INFERNO_SUIT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "vampiric_attire"),   	VAMPIRIC_ATTIRE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "looter_jacket"), 	  	LOOTER_JACKET);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "knight_plate"), 	  	KNIGHT_PLATE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "alchemist_robe"), 	  	ALCHEMIST_ROBE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "duelist_costume"),	  	DUELIST_COSTUME);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ninja_garb"),		  	NINJA_GARB);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mummy_bandages"),		MUMMY_BANDAGES);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "destroyer_gear"),		DESTROYER_GEAR);

		CompanionBatLootTableInit.init();
		CompanionBatCommandInit.init();
    }

	public static void info(String message){
        LOGGER.log(Level.INFO, message);
    }
}