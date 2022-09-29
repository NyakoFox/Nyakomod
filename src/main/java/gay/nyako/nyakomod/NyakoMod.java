package gay.nyako.nyakomod;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import gay.nyako.nyakomod.behavior.CoinBagItemDispenserBehavior;
import gay.nyako.nyakomod.behavior.SoulJarItemDispenserBehavior;
import gay.nyako.nyakomod.block.*;
import gay.nyako.nyakomod.command.*;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.mixin.ScoreboardCriterionMixin;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.resource.ResourceType;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NyakoMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("nyakomod");

	public static final gay.nyako.nyakomod.NyakoConfig CONFIG = gay.nyako.nyakomod.NyakoConfig.createAndLoad();

	public static final IntProperty COINS_PROPERTY = IntProperty.of("coins", 1, SingleCoinBlock.MAX_COINS);
	public static SlimeSkyManager SLIME_SKY_MANAGER;
	public static final ArmorMaterial NYAKO_ARMOR_MATERIAL = new NyakoArmorMaterial();
	public static final ScoreboardCriterion COIN_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:coins");
	public static Enchantment CUNKLESS_CURSE_ENCHANTMENT = Registry.register(Registry.ENCHANTMENT, new Identifier("nyakomod", "cunkless_curse"), new CunkCurseEnchantment());

	@Environment(EnvType.SERVER)
	public static CachedResourcePack CACHED_RESOURCE_PACK = new CachedResourcePack();

	@Environment(EnvType.SERVER)
	public static ModelManager MODEL_MANAGER = new ModelManager();

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ShopDataResourceReloadListener());
		NyakoNetworking.registerGlobalReceivers();
		NyakoLoot.register();
		NyakoGacha.register();
		InstrumentRegistry.register();
		NyakoCriteria.register();

		FabricDefaultAttributeRegistry.register(NyakoEntities.PET_SPRITE, PetSpriteEntity.createPetAttributes());
		FabricDefaultAttributeRegistry.register(NyakoEntities.PET_DRAGON, PetDragonEntity.createPetAttributes());

		DispenserBlock.registerBehavior(NyakoItems.SOUL_JAR, new SoulJarItemDispenserBehavior());
		DispenserBlock.registerBehavior(NyakoItems.BAG_OF_COINS_ITEM, new CoinBagItemDispenserBehavior());
		DispenserBlock.registerBehavior(NyakoItems.HUNGRY_BAG_OF_COINS_ITEM, new CoinBagItemDispenserBehavior());

		CunkCoinUtils.registerCoinAmounts();
		registerCommands();

		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			CachedResourcePack.setPlayerResourcePack(handler.player);
			((ServerPlayerEntityAccess)handler.player).setSafeMode(true);
		}));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SLIME_SKY_MANAGER = SlimeSkyManager.forWorld(server.getWorld(World.OVERWORLD));
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			for (ServerPlayerEntity player : world.getPlayers()) {
				if (((ServerPlayerEntityAccess)player).isInSafeMode()) {
					if (!((ServerPlayerEntityAccess)player).getJoinPos().equals(player.getPos())) {
						((ServerPlayerEntityAccess)player).setSafeMode(false);
					}
				}
			}

			if (world.getTime() % (10 * 60 * 20) == 0) {
				for (ServerPlayerEntity player : world.getPlayers()) {
					if (player.abilities.invulnerable) continue;
					if (player.isDead()) continue;

					var access = (PlayerEntityAccess) player;

					if (access.getMilkSaturation() > 0) {
						access.setMilkSaturation(access.getMilkSaturation() - 1);
						continue;
					}
					if (access.getMilk() > 0) {
						access.setMilk(access.getMilk() - 1);
						continue;
					}
				}
			}

			if (world.getRegistryKey() == World.OVERWORLD) {
				if (SLIME_SKY_MANAGER == null) return;

				SLIME_SKY_MANAGER.tick();
			}
		});
	}

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			BackCommand.register(dispatcher);
			XpCommand.register(dispatcher);
			LoreCommand.register(dispatcher);
			RenameCommand.register(dispatcher);
			PackCommand.register(dispatcher);
			SmiteCommand.register(dispatcher);
			IconsCommand.register(dispatcher);
			SlimeDebugCommand.register(dispatcher);
			ShopCommand.register(dispatcher);
			DumpJsonCommand.register(dispatcher);
			DumpNbtCommand.register(dispatcher);
		});
	}

}