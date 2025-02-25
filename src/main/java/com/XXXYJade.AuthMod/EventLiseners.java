package com.XXXYJade.AuthMod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

public class EventLiseners {
    private AuthMod authMod;

    public EventLiseners(AuthMod authMod) {
        this.authMod = authMod;
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
    }
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        
    }
}
