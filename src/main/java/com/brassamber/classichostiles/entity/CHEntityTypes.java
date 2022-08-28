package com.brassamber.classichostiles.entity;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.hostile.BoarEntity;
import com.brassamber.classichostiles.entity.neutral.BearEntity;
import com.brassamber.classichostiles.entity.neutral.PlainsFoxEntity;
import com.brassamber.classichostiles.entity.passive.MoobloomEntity;
import com.brassamber.classichostiles.item.CHItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.10
 */
@Mod.EventBusSubscriber(modid = ClassicHostiles.MOD_ID, bus = Bus.MOD)
public class CHEntityTypes {
	public static final DeferredRegister<EntityType<?>> DEFERRED_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ClassicHostiles.MOD_ID);

	/*********************************************************** Hostile ********************************************************/

	public static final RegistryObject<EntityType<BoarEntity>> BOAR = registerEntityType("boar", 0x674D31, 0x363636, EntityType.Builder.of(BoarEntity::new, MobCategory.CREATURE).sized(0.9F, 0.9F).clientTrackingRange(10));

	/*********************************************************** Neutral ********************************************************/

	// TODO Immunity Blocks should be BlockTags instead for more configuration options
	public static final RegistryObject<EntityType<PlainsFoxEntity>> PLAINS_FOX = registerEntityType("fox", EntityType.Builder.of(PlainsFoxEntity::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).immuneTo(Blocks.SWEET_BERRY_BUSH));
	public static final RegistryObject<EntityType<BearEntity>> BEAR = registerEntityType("bear", EntityType.Builder.of(BearEntity::new, MobCategory.CREATURE).immuneTo(Blocks.SWEET_BERRY_BUSH).sized(1.4F, 1.4F).clientTrackingRange(10));

	/*********************************************************** Passive ********************************************************/

	public static final RegistryObject<EntityType<MoobloomEntity>> MOOBLOOM = registerEntityType("moobloom", 0xFFFF00, 0xFFFFFF, EntityType.Builder.of(MoobloomEntity::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10));

	/**
	 * register entity spawn placement and build attributes
	 */
	@SubscribeEvent
	public static void initializeEntityAttributes(EntityAttributeCreationEvent event) {
		ClassicHostiles.LOGGER.debug("Registering spawn placements");
		// Hostile
		registerSpawnPlacement(BOAR.get(), BoarEntity::checkHostileAnimalSpawnRules);

		// Neutral
		registerSpawnPlacement(PLAINS_FOX.get(), SpawnPlacements.Type.NO_RESTRICTIONS, PlainsFoxEntity::checkPlainsFoxSpawnRules);
		registerSpawnPlacement(BEAR.get(), BearEntity::checkBearSpawnRules);

		// Passive
		registerSpawnPlacement(MOOBLOOM.get(), MoobloomEntity::checkAnimalSpawnRules);

		ClassicHostiles.LOGGER.debug("Building attributes");
		// Hostile
		event.put(BOAR.get(), BoarEntity.createAttributes().build());

		// Neutral
		event.put(PLAINS_FOX.get(), Fox.createAttributes().build());
		event.put(BEAR.get(), BearEntity.createAttributes().build());

		// Passive
		event.put(MOOBLOOM.get(), Cow.createAttributes().build());
	}

	/*********************************************************** Helper methods ********************************************************/

	/**
	 * Helper method for registering Mob EntityTypes
	 */
	private static <T extends Mob> RegistryObject<EntityType<T>> registerEntityType(String registryName, int eggBackgroundColor, int eggHighlightColor, EntityType.Builder<T> builder) {
		// Register Mob
		RegistryObject<EntityType<T>> entityType = registerEntityType(registryName, builder);
		// Register Spawn Egg
		CHItems.registerItem(registryName + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, eggBackgroundColor, eggHighlightColor, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
		return entityType;
	}

	/**
	 * Helper method for registering EntityTypes without Spawn Eggs
	 */
	private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(String registryName, EntityType.Builder<T> builder) {
		return DEFERRED_ENTITY_TYPES.register(registryName, () -> builder.build(ClassicHostiles.find(registryName)));
	}

	private static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> placementPredicate) {
		SpawnPlacements.register(entityType, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
	}

	private static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type spawnPlacementsType, SpawnPlacements.SpawnPredicate<T> placementPredicate) {
		SpawnPlacements.register(entityType, spawnPlacementsType, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
	}
}
