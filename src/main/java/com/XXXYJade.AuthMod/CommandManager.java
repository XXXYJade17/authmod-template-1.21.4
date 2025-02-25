package com.XXXYJade.AuthMod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CommandManager {
    private final AuthMod authMod;

    public CommandManager(AuthMod authMod) {
        this.authMod = authMod;
    }

    public void registerAllCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        registerCommand(dispatcher);
        loginCommand(dispatcher);
//        registerChangePasswordCommand(dispatcher);
//        registerAdminCommands(dispatcher);
    }

    private void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> register = Commands.literal("register")
                .then(Commands.argument("password", StringArgumentType.word())
                        .then(Commands.argument("confirmPassword", StringArgumentType.word())
                                .executes(this::register)));
        dispatcher.register(register);

        LiteralArgumentBuilder<CommandSourceStack> reg = Commands.literal("reg")
                .then(Commands.argument("password", StringArgumentType.word())
                        .then(Commands.argument("confirmPassword", StringArgumentType.word())
                                .executes(this::register)));
        dispatcher.register(reg);
    }
    private int register(CommandContext<CommandSourceStack> context) {
        try {
            Player player=context.getSource().getPlayerOrException();
            String username=player.getName().getString();
            String password=StringArgumentType.getString(context,"password");
            String confirmPassword=StringArgumentType.getString(context,"confirmPassword");
            if(!password.equals(confirmPassword)){
                context.getSource().sendFailure(Component.literal("两次密码输入不一致!"));
                return 0;
            }
            if(authMod.getPasswordManager().hasPassword(username)){
                context.getSource().sendFailure(Component.literal("你已经注册过了!"));
                return 0;
            }
            context.getSource().sendSuccess(()->Component.literal("注册成功"),false);
            authMod.getPasswordManager().addNewPassword(username,password);
            return 1;
        }catch(CommandSyntaxException e){
            return 0;
        }
    }
    private void loginCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> login = Commands.literal("login")
                .then(Commands.argument("password", StringArgumentType.word())
                        .executes(this::login));
        dispatcher.register(login);
    }
    private int login(CommandContext<CommandSourceStack> context){
        PasswordManager passwordManager=authMod.getPasswordManager();

        try{
            Player player=context.getSource().getPlayerOrException();
            String username=player.getName().getString();
            String password=StringArgumentType.getString(context,"password");
            if(passwordManager.hasPassword(username)){
                context.getSource().sendFailure(Component.literal("你还没有注册!"));
                return 0;
            }
            if(PasswordUtils.verifyPassword(password,passwordManager.getPassword(username))){
                context.getSource().sendSuccess(()->Component.literal("登录成功!"),false);
                return 1;
            }
            else{
                context.getSource().sendFailure(Component.literal("密码错误!"));
                return 0;
            }
        }catch (CommandSyntaxException e){
            return 0;
        }
    }
}


