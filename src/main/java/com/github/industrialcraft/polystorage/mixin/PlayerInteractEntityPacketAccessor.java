package com.github.industrialcraft.polystorage.mixin;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerInteractEntityC2SPacket.class)
public interface PlayerInteractEntityPacketAccessor {
    @Accessor
    int getEntityId();
}
