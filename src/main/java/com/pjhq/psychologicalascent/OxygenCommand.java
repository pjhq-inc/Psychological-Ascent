package com.pjhq.psychologicalascent;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class OxygenCommand { //this is temp until i add a bar coz i couldnt find any fitting graphics
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("oxygen")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("get")
                .executes(context -> getOxygen(context, context.getSource().getPlayerOrException())))
            .then(Commands.literal("set")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, 1000))
                    .executes(context -> setOxygen(
                        context, 
                        context.getSource().getPlayerOrException(),
                        IntegerArgumentType.getInteger(context, "amount")
                    ))))
            .then(Commands.literal("add")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                    .executes(context -> addOxygen(
                        context,
                        context.getSource().getPlayerOrException(),
                        IntegerArgumentType.getInteger(context, "amount")
                    ))))
            .then(Commands.literal("deplete")
                .executes(context -> depleteOxygen(
                    context,
                    context.getSource().getPlayerOrException(),
            ))))
        );
    }

    private static int getOxygen(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        int oxygen = player.getData(OxygenData.OXYGEN);
        context.getSource().sendSuccess(() -> 
            Component.literal("Oxygen level: " + oxygen + "/1000"), false);
        return oxygen;
    }

    private static int setOxygen(CommandContext<CommandSourceStack> context, ServerPlayer player, int amount) {
        player.setData(OxygenData.OXYGEN, amount);
        context.getSource().sendSuccess(() -> 
            Component.literal("Set oxygen to " + amount), true);
        return amount;
    }

    private static int addOxygen(CommandContext<CommandSourceStack> context, ServerPlayer player, int amount) {
        int current = player.getData(OxygenData.OXYGEN);
        int newAmount = Math.max(0, Math.min(1000, current + amount));
        player.setData(OxygenData.OXYGEN, newAmount);
        context.getSource().sendSuccess(() -> 
            Component.literal("Oxygen: " + current + " -> " + newAmount), true);
        return newAmount;
    }

    private static int depleteOxygen(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        setOxygen(context, player, 0) 
        return amount;
    }
}
