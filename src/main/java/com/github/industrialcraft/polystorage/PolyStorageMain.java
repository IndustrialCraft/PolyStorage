package com.github.industrialcraft.polystorage;

import io.github.mattidragon.extendeddrawers.registry.ModBlocks;
import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.resource.ModdedResources;
import io.github.theepicblock.polymc.api.resource.PolyMcResourcePack;
import io.github.theepicblock.polymc.api.resource.SoundAsset;
import io.github.theepicblock.polymc.api.wizard.PacketConsumer;
import io.github.theepicblock.polymc.api.wizard.VItemFrame;
import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.api.wizard.WizardInfo;
import io.github.theepicblock.polymc.impl.misc.logging.SimpleLogger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolyStorageMain implements ModInitializer, PolyMcEntrypoint {
	public static final String MOD_ID = "polystorage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final VirtualEntityClickRegistry VIRTUAL_ENTITY_CLICK_REGISTRY = new VirtualEntityClickRegistry();
	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

	}

	@Override
	public void registerPolys(PolyRegistry registry) {
		registry.registerBlockPoly(ModBlocks.SINGLE_DRAWER, new BlockPoly() {
			@Override
			public BlockState getClientBlock(BlockState input) {
				return Blocks.DROPPER.getDefaultState().with(Properties.FACING, input.get(Properties.HORIZONTAL_FACING));
			}
			@Override
			public boolean hasWizard() {
				return true;
			}

			@Override
			public Wizard createWizard(WizardInfo info) {
				return new StorageDrawerWizard(info);
			}
		});
	}

	@Override
	public void registerModSpecificResources(ModdedResources moddedResources, PolyMcResourcePack pack, SimpleLogger logger) {
		pack.setAsset(MOD_ID, "font/iconfont.json", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "font/iconfont.json")));

		pack.setAsset(MOD_ID, "textures/font/lock.png", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "textures/font/lock.png")));
		pack.setAsset(MOD_ID, "textures/font/void.png", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "textures/font/void.png")));
		pack.setAsset(MOD_ID, "textures/font/t1.png", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "textures/font/t1.png")));
		pack.setAsset(MOD_ID, "textures/font/t2.png", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "textures/font/t2.png")));
		pack.setAsset(MOD_ID, "textures/font/t3.png", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "textures/font/t3.png")));
		pack.setAsset(MOD_ID, "textures/font/t4.png", new SoundAsset(() -> moddedResources.getInputStream(MOD_ID, "textures/font/t4.png")));
	}
}
