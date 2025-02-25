package com.XXXYJade.AuthMod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(AuthMod.MODID)
public class AuthMod
{
    private static AuthMod intance;

    public static final String MODID = "authmod";
    private static final Logger LOGGER = LogManager.getLogger("authmod");

    private static PasswordManager passwordManager;
    private static PlayerManager playerManager;
    private static EventLiseners eventLiseners;
    private static CommandManager commandManager;

    public AuthMod(IEventBus modEventBus, ModContainer modContainer){
        intance=this;
        passwordManager=new PasswordManager();
        playerManager=new PlayerManager();
        eventLiseners=new EventLiseners(this);
        commandManager=new CommandManager(this);
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) -> {
            this.commandManager.registerAllCommands(event.getServer().getCommands().getDispatcher());
        });

    }

    public static PasswordManager getPasswordManager() {
        return passwordManager;
    }
    public static PlayerManager getPlayerManager() {
        return playerManager;
    }
    public static EventLiseners getEventLiseners() {
        return eventLiseners;
    }
    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
