package com.github.industrialcraft.polystorage.mixin;

import com.github.industrialcraft.polystorage.PolyStorageMain;
import com.mojang.brigadier.ParseResults;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class VirtualEntityClickMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onPlayerInteractEntity", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSneaking(Z)V", shift = At.Shift.AFTER))
    public void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci){
        int id = ((PlayerInteractEntityPacketAccessor)packet).getEntityId();
        TriConsumer<ServerPlayerEntity,Vec3d,Boolean> callback = PolyStorageMain.VIRTUAL_ENTITY_CLICK_REGISTRY.clickCallbacks.get(id);
        if(callback != null){
            packet.handle(new PlayerInteractEntityC2SPacket.Handler() {
                @Override
                public void interact(Hand hand) {
                    if(hand == Hand.MAIN_HAND)
                        callback.accept(player, null, false);
                }
                @Override
                public void interactAt(Hand hand, Vec3d pos) {
                    if(hand == Hand.MAIN_HAND)
                        callback.accept(player, pos, false);
                }
                @Override
                public void attack() {
                    callback.accept(player, null, true);
                }
            });
            ci.cancel();
        }
    }
}
