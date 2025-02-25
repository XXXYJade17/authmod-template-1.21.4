package com.XXXYJade.AuthMod;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PlayerManager {
    private final Timer timer = new Timer(true);
    private static final Map<String,Boolean> playersLoggedIn=new HashMap<>();
    private static final ConcurrentMap<String, TimerTask> loginTasks = new ConcurrentHashMap<>();

    public PlayerManager() {
        loadPlayers();
    }
    private void loginTask(Player player) {
        String username=player.getName().getString();
        TimerTask task = new TimerTask() { // 创建一个计时器任务
            @Override
            public void run() {
                if (!isLoggedIn(username)) {
                    ((ServerPlayer)player).connection.disconnect(Component.literal("You have not logged in!"));
                    loginTasks.remove(username);    //取消之前的登录任务
                }
            }
        };
            loginTasks.put(username,task);    //添加到计时器任务
            timer.schedule(task,100*1000L); //设置计时器计划时间，如果玩家没有登录则退出游戏
    }
    public void loadPlayers() {
        Set<String> usernameSet=AuthMod.getPasswordManager().getPasswordMap().keySet();
        for (String username : usernameSet) {
            playersLoggedIn.put(username, false);
        }
    }
    public boolean isLoggedIn(String username) {
        return playersLoggedIn.get(username);
    }
}
