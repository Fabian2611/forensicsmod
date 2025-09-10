package io.fabianbuthere.forensics.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.fabianbuthere.forensics.Forensics;
import io.fabianbuthere.forensics.util.FingerprintHelper;
import io.fabianbuthere.forensics.data.WorldFingerprintData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Forensics.MOD_ID)
public class FingerprintCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("forensics")
                        .requires(source -> source.isPlayer() && source.hasPermission(3))
                        .then(Commands.literal("fingerprint")
                                .then(Commands.literal("clear")
                                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    BlockPos pos = new BlockPos(
                                                                            IntegerArgumentType.getInteger(context, "x"),
                                                                            IntegerArgumentType.getInteger(context, "y"),
                                                                            IntegerArgumentType.getInteger(context, "z")
                                                                    );

                                                                    ServerLevel level = context.getSource().getLevel();
                                                                    WorldFingerprintData data = WorldFingerprintData.get(level);

                                                                    if (data.hasFingerprint(pos)) {
                                                                        data.removeFingerprint(pos);
                                                                        context.getSource().sendSuccess(() -> Component.literal("§aFingerprint cleared at " + pos.toShortString()), false);
                                                                    } else {
                                                                        context.getSource().sendSuccess(() -> Component.literal("§cNo fingerprint found at " + pos.toShortString()), false);
                                                                    }

                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("get")
                                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    BlockPos pos = new BlockPos(
                                                                            IntegerArgumentType.getInteger(context, "x"),
                                                                            IntegerArgumentType.getInteger(context, "y"),
                                                                            IntegerArgumentType.getInteger(context, "z")
                                                                    );

                                                                    ServerLevel level = context.getSource().getLevel();

                                                                    if (FingerprintHelper.hasFingerprint(level, pos)) {
                                                                        UUID fingerprintUUID = FingerprintHelper.getFingerprint(level, pos);
                                                                        String playerName = FingerprintHelper.getPlayerName(level, fingerprintUUID);
                                                                        context.getSource().sendSuccess(() -> Component.literal("§aFingerprint found at " + pos.toShortString() + ": " + playerName + " (" + fingerprintUUID + ")"), false);
                                                                    }
                                                                    else {
                                                                        context.getSource().sendSuccess(() -> Component.literal("§cNo fingerprint found at " + pos.toShortString()), false);
                                                                    }

                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("footprint")
                                .then(Commands.literal("clear")
                                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    BlockPos pos = new BlockPos(
                                                                            IntegerArgumentType.getInteger(context, "x"),
                                                                            IntegerArgumentType.getInteger(context, "y"),
                                                                            IntegerArgumentType.getInteger(context, "z")
                                                                    );

                                                                    ServerLevel level = context.getSource().getLevel();
                                                                    WorldFingerprintData data = WorldFingerprintData.get(level);

                                                                    if (data.hasFootprint(pos)) {
                                                                        data.removeFootprint(pos);
                                                                        context.getSource().sendSuccess(() -> Component.literal("§aFootprint cleared at " + pos.toShortString()), false);
                                                                    } else {
                                                                        context.getSource().sendSuccess(() -> Component.literal("§cNo footprint found at " + pos.toShortString()), false);
                                                                    }

                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("get")
                                        .then(Commands.argument("x", IntegerArgumentType.integer())
                                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    BlockPos pos = new BlockPos(
                                                                            IntegerArgumentType.getInteger(context, "x"),
                                                                            IntegerArgumentType.getInteger(context, "y"),
                                                                            IntegerArgumentType.getInteger(context, "z")
                                                                    );

                                                                    ServerLevel level = context.getSource().getLevel();

                                                                    if (FingerprintHelper.hasFootprint(level, pos)) {
                                                                        UUID footprintUUID = FingerprintHelper.getFootprint(level, pos);
                                                                        String playerName = FingerprintHelper.getPlayerName(level, footprintUUID);
                                                                        context.getSource().sendSuccess(() -> Component.literal("§aFootprint found at " + pos.toShortString() + ": " + playerName + " (" + footprintUUID + ")"), false);
                                                                    }
                                                                    else {
                                                                        context.getSource().sendSuccess(() -> Component.literal("§cNo footprint found at " + pos.toShortString()), false);
                                                                    }

                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("debug")
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                                .then(Commands.argument("z", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            BlockPos pos = new BlockPos(
                                                                    IntegerArgumentType.getInteger(context, "x"),
                                                                    IntegerArgumentType.getInteger(context, "y"),
                                                                    IntegerArgumentType.getInteger(context, "z")
                                                            );

                                                            ServerLevel level = context.getSource().getLevel();
                                                            WorldFingerprintData data = WorldFingerprintData.get(level);

                                                            // Check current position and surrounding positions
                                                            context.getSource().sendSuccess(() -> Component.literal("§eDebug info for " + pos.toShortString() + ":"), false);

                                                            for (int yOffset = -2; yOffset <= 2; yOffset++) {
                                                                BlockPos checkPos = pos.offset(0, yOffset, 0);
                                                                boolean hasFingerprint = data.hasFingerprint(checkPos);
                                                                boolean hasFootprint = data.hasFootprint(checkPos);

                                                                if (hasFingerprint || hasFootprint) {
                                                                    String msg = "§a" + checkPos.toShortString() + " - ";
                                                                    if (hasFingerprint) {
                                                                        UUID fpUUID = data.getFingerprint(checkPos);
                                                                        String playerName = getPlayerName(level, fpUUID);
                                                                        msg += "Fingerprint: " + playerName + " ";
                                                                    }
                                                                    if (hasFootprint) {
                                                                        UUID ftUUID = data.getFootprint(checkPos);
                                                                        String playerName = getPlayerName(level, ftUUID);
                                                                        msg += "Footprint: " + playerName + " ";
                                                                    }
                                                                    final String finalMsg = msg;
                                                                    context.getSource().sendSuccess(() -> Component.literal(finalMsg), false);
                                                                }
                                                            }

                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
        );
    }

    private static String getPlayerName(ServerLevel level, UUID playerUUID) {
        Player player = level.getPlayerByUUID(playerUUID);
        return player != null ? player.getName().getString() : "Unknown Player";
    }
}
