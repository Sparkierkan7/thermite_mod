package thermite.therm;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {

    public static void register() {

        //thermite_resetPlayerState command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("thermite_resetPlayerState").requires(source -> source.hasPermissionLevel(4))
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> {

                            ServerState serverState = ServerState.getServerState(EntityArgumentType.getPlayer(context, "player").getWorld().getServer());
                            ThermPlayerState playerState = ServerState.getPlayerState(EntityArgumentType.getPlayer(context, "player"));

                            playerState.temp = 50;
                            playerState.tempRate = 0.0625;
                            playerState.restingTemp = 404;
                            playerState.minTemp = -400;
                            playerState.maxTemp = 400;
                            playerState.damageType = "";
                            playerState.damageTick = 0;
                            playerState.maxDamageTick = 10;
                            playerState.searchFireplaceTick = 4;
                            serverState.markDirty();

                            context.getSource().sendMessage(Text.literal("Reset " + EntityArgumentType.getPlayer(context, "player").getName().getString() + "'s playerState."));

                            return 1;
                        }))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("thermite_test").requires(source -> source.hasPermissionLevel(4))
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> {

                            ServerState serverState = ServerState.getServerState(EntityArgumentType.getPlayer(context, "player").getWorld().getServer());
                            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                            ThermPlayerState playerState = ServerState.getPlayerState(player);

                            //LOGGER.info("" + player.getWorld().getBiome(player.getBlockPos()).isIn());

							/*Random rand = new Random();
							//serverState.windYaw = rand.nextDouble(0, 360)*Math.PI/180;
							//serverState.windPitch = 360*Math.PI/180;

							//double yaw = rand.nextDouble(0, 360)*Math.PI/180;
							//double pitch = 360*Math.PI/180;

							for (int i = 0; i < 32; i++) {

								double turbulence = 20*Math.PI/180;
								//yaw += rand.nextDouble(-turbulence, turbulence);
								//pitch += rand.nextDouble(-turbulence, turbulence);

								Vec3d dir = new Vec3d((Math.cos(serverState.windPitch+rand.nextDouble(-turbulence, turbulence)) * Math.cos(serverState.windYaw+rand.nextDouble(-turbulence, turbulence))), (Math.sin(serverState.windPitch+rand.nextDouble(-turbulence, turbulence)) * Math.cos(serverState.windYaw+rand.nextDouble(-turbulence, turbulence))), Math.sin(serverState.windYaw+rand.nextDouble(-turbulence, turbulence)));

								Vec3d startPos = new Vec3d(player.getPos().x, player.getPos().y + 1, player.getPos().z);

								BlockHitResult r = player.getWorld().raycast(new RaycastContext(startPos, startPos.add(dir.multiply(32)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.WATER, player));

								//context.getSource().sendMessage(Text.literal("" + rand.nextDouble(-turbulence, turbulence)*Math.PI/180));

								if (!player.getWorld().getBlockState(r.getBlockPos()).isAir()) {
									player.getWorld().setBlockState(r.getBlockPos(), Blocks.RED_CONCRETE.getDefaultState());
								} else if (player.getWorld().getBlockState(r.getBlockPos()).isAir()) {
									player.getWorld().setBlockState(r.getBlockPos(), Blocks.LIME_CONCRETE.getDefaultState());
								}

							}*/

                            return 1;
                        }))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("windRandomize").requires(source -> source.hasPermissionLevel(4))
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> {

                            ServerState serverState = ServerState.getServerState(EntityArgumentType.getPlayer(context, "player").getWorld().getServer());
                            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                            ThermPlayerState playerState = ServerState.getPlayerState(player);

                            Random rand = new Random();
                            serverState.windYaw = rand.nextDouble(0, 360)*Math.PI/180;
                            serverState.windPitch = 360*Math.PI/180;
                            serverState.windTempModifier = rand.nextDouble(-serverState.windTempModifierRange, serverState.windTempModifierRange);
                            serverState.precipitationWindModifier = rand.nextDouble(-serverState.windTempModifierRange, 0);
                            serverState.markDirty();
                            context.getSource().sendMessage(Text.literal("Wind Randomized."));
                            context.getSource().sendMessage(Text.literal("Wind Yaw: " + serverState.windYaw*180/Math.PI));
                            context.getSource().sendMessage(Text.literal("Wind Temperature Modifier: " + serverState.windTempModifier));
                            context.getSource().sendMessage(Text.literal("Precipitation Modifier: " + serverState.precipitationWindModifier));

                            return 1;
                        }))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("showWind").requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {

                    ServerState serverState = ServerState.getServerState(context.getSource().getServer());
                    PlayerEntity player = context.getSource().getPlayer();
                    ThermPlayerState playerState = ServerState.getPlayerState(player);

                    Vec3d dir = new Vec3d((Math.cos(serverState.windPitch) * Math.cos(serverState.windYaw)), (Math.sin(serverState.windPitch) * Math.cos(serverState.windYaw)), Math.sin(serverState.windYaw));

                    player.getWorld().addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 1, player.getZ(), dir.x*4, dir.y*4, dir.z*4);

                    return 1;
                })));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("windInfo").requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {

                    ServerState serverState = ServerState.getServerState(context.getSource().getServer());
                    PlayerEntity player = context.getSource().getPlayer();
                    ThermPlayerState playerState = ServerState.getPlayerState(player);

                    context.getSource().sendMessage(Text.literal("§e=====Wind Info====="));
                    context.getSource().sendMessage(Text.literal("§eWind Yaw: §6" + serverState.windYaw*180/Math.PI));
                    context.getSource().sendMessage(Text.literal("§eWind Temperature Modifier: §6" + serverState.windTempModifier));
                    context.getSource().sendMessage(Text.literal("§ePrecipitation Modifier: §6" + serverState.precipitationWindModifier));
                    context.getSource().sendMessage(Text.literal("§eNext Randomize: §a" + serverState.windRandomizeTick + "§7/24000"));

                    return 1;
                })));

    }

}
