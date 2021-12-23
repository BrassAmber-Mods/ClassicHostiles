package de.dertoaster.classichostiles.init;

import java.util.HashMap;
import java.util.Map;

import de.dertoaster.classichostiles.ClassicHostilesMod;
import de.dertoaster.classichostiles.entity.animal.FoxEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
		//Important: DO NOT replace vanilla entities, that will have unforeseen consequences, rather listen to EntityJoinWorldEvent and exchange the mob with your own
		
		//Register the event handler
		MinecraftForge.EVENT_BUS.register(new CHEntities.EventHandler());
	}

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, ClassicHostilesMod.MODID);
	
	public static final RegistryObject<EntityType<FoxEntity>> FOX = ENTITY_TYPES.register("fox", 
			() -> EntityType.Builder
			.<FoxEntity>of(FoxEntity::new, EntityClassification.CREATURE)
			.sized(0.6F, 0.7F)
			.clientTrackingRange(8)
			.immuneTo(Blocks.SWEET_BERRY_BUSH)
			.build(new ResourceLocation(ClassicHostilesMod.MODID, "fox").toString())
	);
	
	@SubscribeEvent
	public static void onSetup(final EntityAttributeCreationEvent event) {
		event.put(FOX.get(), FoxEntity.createAttributes().build());
	}
	
	static final Map<String, EntityType<?>> REPLACER_MAP;
	
	static {
		REPLACER_MAP = new HashMap<>();
	}
	
	private static class EventHandler {
		@SubscribeEvent
		public void onEntityJoin(final EntityJoinWorldEvent event) {
			if(REPLACER_MAP.isEmpty()) {
				fillReplacementMap();
			}
			if(event.getEntity() == null || !(event.getWorld() instanceof ServerWorld)) {
				return;
			}
			final String registryName = event.getEntity().getType().getRegistryName().toString();
			final EntityType<?> replacement = REPLACER_MAP.getOrDefault(registryName, null);
			if(replacement != null) {
				Entity replacedEnt = replacement.create(
						(ServerWorld) event.getWorld(),
						event.getEntity().getPersistentData(), 
						event.getEntity().getCustomName(),
						null,
						event.getEntity().blockPosition(),
						SpawnReason.NATURAL,
						true,
						true
				);
						
				if(replacedEnt != null) {
					event.getWorld().addFreshEntity(replacedEnt);
					event.setCanceled(true);
					event.getEntity().remove(false);
				}
			}
		}
	}

	static void fillReplacementMap() {
		//Add in the replacements
		REPLACER_MAP.put(EntityType.FOX.getRegistryName().toString(), FOX.get());
	}
}
