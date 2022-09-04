package com.github.industrialcraft.polystorage;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class VirtualEntityClickRegistry {
    public final HashMap<Integer, TriConsumer<ServerPlayerEntity,Vec3d,Boolean>> clickCallbacks;
    public VirtualEntityClickRegistry() {
        this.clickCallbacks = new HashMap<>();
    }
}
