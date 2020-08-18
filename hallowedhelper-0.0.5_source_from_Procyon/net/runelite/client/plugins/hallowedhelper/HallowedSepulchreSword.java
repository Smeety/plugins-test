// 
// Decompiled by Procyon v0.5.36
// 

package net.runelite.client.plugins.hallowedhelper;

import net.runelite.api.GameObject;
import lombok.NonNull;
import net.runelite.api.NPC;

class HallowedSepulchreSword
{
    @NonNull
    private final NPC npc;
    private int ticksUntilNextAnimation;
    private int maxTick;
    public boolean forward;
    public GameObject thrower;
    public int distance;
    
    public void updateState() {
        if (this.thrower != null) {
            final int newdistance = this.thrower.getWorldLocation().distanceTo2D(this.npc.getWorldLocation());
            if (this.distance != -1 && newdistance < this.distance) {
                this.forward = false;
            }
            this.distance = newdistance;
        }
    }
    
    void setThrower(final GameObject setthrower) {
        this.thrower = setthrower;
    }
    
    void setForward(final boolean state) {
        this.forward = state;
    }
    
    @NonNull
    NPC getNpc() {
        return this.npc;
    }
    
    int getTicksUntilNextAnimation() {
        return this.ticksUntilNextAnimation;
    }
    
    int getMaxTick() {
        return this.maxTick;
    }
    
    boolean isForward() {
        return this.forward;
    }
    
    GameObject getThrower() {
        return this.thrower;
    }
    
    int getDistance() {
        return this.distance;
    }
    
    HallowedSepulchreSword(@NonNull final NPC npc) {
        this.ticksUntilNextAnimation = -20;
        this.maxTick = -1;
        this.forward = true;
        this.distance = -1;
        if (npc == null) {
            throw new NullPointerException("npc is marked non-null but is null");
        }
        this.npc = npc;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HallowedSepulchreSword)) {
            return false;
        }
        final HallowedSepulchreSword other = (HallowedSepulchreSword)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$npc = this.getNpc();
        final Object other$npc = other.getNpc();
        if (this$npc == null) {
            if (other$npc == null) {
                return true;
            }
        }
        else if (this$npc.equals(other$npc)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof HallowedSepulchreSword;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $npc = this.getNpc();
        result = result * 59 + (($npc == null) ? 43 : $npc.hashCode());
        return result;
    }
}
