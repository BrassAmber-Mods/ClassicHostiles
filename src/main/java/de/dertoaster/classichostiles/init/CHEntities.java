package de.dertoaster.classichostiles.init;

import de.dertoaster.classichostiles.ClassicHostilesMod;
import de.dertoaster.classichostiles.entity.animal.FoxEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ClassicHostilesMod.MODID, bus = Bus.MOD)
public class CHEntities {
	
	public static void registerToEventBus(IEventBus eventbus) {
		ENTITY_TYPES.register(eventbus);
		VANILLA_ENTITY_TYPES.register(eventbus);
	}

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, ClassicHostilesMod.MODID);
	public static final DeferredRegister<EntityType<?>> VANILLA_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "minecraft");
	
	public static final RegistryObject<EntityType<FoxEntity>> FOX = VANILLA_ENTITY_TYPES.register("fox", 
			() -> EntityType.Builder
			.<FoxEntity>of(FoxEntity::new, EntityClassification.CREATURE)
			.sized(0.6F, 0.7F)
			.clientTrackingRange(8)
			.immuneTo(Blocks.SWEET_BERRY_BUSH)
			.build(new ResourceLocation("fox").toString())
	);
	
	@SubscribeEvent
	public static void setup(final EntityAttributeCreationEvent event) {
		event.put(FOX.get(), FoxEntity.createAttributes().build());
	}
}
