package com.github.industrialcraft.polystorage;

import io.github.mattidragon.extendeddrawers.block.base.DrawerInteractionHandler;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
import io.github.mattidragon.extendeddrawers.drawer.DrawerSlot;
import io.github.mattidragon.extendeddrawers.item.UpgradeItem;
import io.github.mattidragon.extendeddrawers.registry.ModBlocks;
import io.github.mattidragon.extendeddrawers.registry.ModItems;
import io.github.theepicblock.polymc.api.wizard.PacketConsumer;
import io.github.theepicblock.polymc.api.wizard.VItemFrame;
import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.api.wizard.WizardInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class StorageDrawerWizard extends Wizard {
    private final VItemFrame itemFrame;
    public StorageDrawerWizard(WizardInfo info) {
        super(info);
        this.itemFrame = new VItemFrame();
        PolyStorageMain.VIRTUAL_ENTITY_CLICK_REGISTRY.clickCallbacks.put(this.itemFrame.getId(), (player,pos,attack) -> {
            Vec3d hitpos = pos != null ? pos : getPosition();
            Direction side = this.getBlockState().get(Properties.HORIZONTAL_FACING);
            if(getBlockState().getBlock() instanceof DrawerInteractionHandler interactionHandler) {
                ItemStack hand = player.getStackInHand(Hand.MAIN_HAND);
                if(hand != null && pos != null){//only react once to interactAt
                    if(hand.getItem() instanceof UpgradeItem){
                        interactionHandler.upgrade(getBlockState(), getWorld(), getBlockPos(), hitpos, side, player, hand);
                        return;
                    }
                    if(hand.isOf(Items.LAVA_BUCKET)){
                        interactionHandler.toggleVoid(getBlockState(), getWorld(), getBlockPos(), hitpos, side);
                        return;
                    }
                    if(hand.isOf(ModItems.LOCK)){
                        interactionHandler.toggleLock(getBlockState(), getWorld(), getBlockPos(), hitpos, side);
                        return;
                    }
                }
                if (attack)
                    getBlockState().onBlockBreakStart(getWorld(), getBlockPos(), player);
                else
                    getBlockState().onUse(getWorld(), player, Hand.MAIN_HAND, new BlockHitResult(hitpos, side, getBlockPos(), true));
            }
        });
    }
    @Override
    public void onRemove(PacketConsumer players) {
        super.onRemove(players);
        PolyStorageMain.VIRTUAL_ENTITY_CLICK_REGISTRY.clickCallbacks.remove(this.itemFrame.getId());
    }
    @Override
    public void addPlayer(PacketConsumer player) {
        Direction direction = this.getBlockState().get(Properties.HORIZONTAL_FACING);
        this.itemFrame.spawn(player, this.getPosition().add(-(0.5 - (double)direction.getOffsetX() * 0.46875), 0, -(0.5 - (double)direction.getOffsetZ() * 0.46875)), direction);
        this.itemFrame.makeInvisible(player);
    }
    @Override
    public void onMove(PacketConsumer players) {
        this.itemFrame.move(players, this.getPosition(), (byte)0, (byte)0, true);
    }
    @Override
    public void onTick(PacketConsumer players) {//todo: move to drawer update checking
        DrawerSlot drawerSlot = ((DrawerBlockEntity)getBlockEntity()).storages[0];
        ItemStack stack = drawerSlot.getResource().toStack();

        stack.setCustomName(Text.literal((drawerSlot.isLocked()?"L":"") + (drawerSlot.isVoiding()?"V":"") + (drawerSlot.getUpgrade()!=null?((int)(Math.log(drawerSlot.getUpgrade().modifier.applyAsLong(1))/Math.log(2))):"")).setStyle(Style.EMPTY.withFont(new Identifier(PolyStorageMain.MOD_ID, "iconfont")).withColor(Formatting.WHITE)).append(Text.literal(drawerSlot.getAmount() + "/" + drawerSlot.getCapacity()).setStyle(Style.EMPTY.withFont(new Identifier("minecraft","default")))));
        this.itemFrame.sendItemStack(players, stack);
    }
    @Override
    public void removePlayer(PacketConsumer player) {
        this.itemFrame.remove(player);
    }

    @Override
    public boolean needsTicking() {
        return true;
    }
}
