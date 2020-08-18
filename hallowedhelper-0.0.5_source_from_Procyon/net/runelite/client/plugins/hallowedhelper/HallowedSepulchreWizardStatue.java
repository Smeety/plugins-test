// 
// Decompiled by Procyon v0.5.36
// 

package net.runelite.client.plugins.hallowedhelper;

import net.runelite.api.DynamicObject;
import lombok.NonNull;
import net.runelite.api.GameObject;

class HallowedSepulchreWizardStatue
{
    @NonNull
    private final GameObject gameObject;
    private int ticksUntilNextAnimation;
    private final int animationId;
    private final int animationTickSpeed;
    private int maxTick;
    private static final int WIZARD_STATUE_ANIM_FIRE_DANGER = 8656;
    private boolean danger;
    
    public int maxTickperfloor(final int floor, final int subfloor) {
        if (floor == 3 && this.maxTick == -1 && subfloor == 1) {
            return -12;
        }
        return this.maxTick;
    }
    
    void updateTicksUntilNextAnimation() {
        if (this.ticksUntilNextAnimation >= 0) {
            --this.ticksUntilNextAnimation;
        }
        else {
            final int animation = this.getAnimation();
            if (animation == this.animationId) {
                if (this.ticksUntilNextAnimation != -20) {
                    this.maxTick = this.ticksUntilNextAnimation;
                }
                this.ticksUntilNextAnimation = this.animationTickSpeed;
            }
            else if (this.ticksUntilNextAnimation > -20) {
                --this.ticksUntilNextAnimation;
            }
        }
    }
    
    int getAnimation() {
        final DynamicObject dynamicObject = (DynamicObject)this.gameObject.getEntity();
        return dynamicObject.getAnimationID();
    }
    
    @NonNull
    GameObject getGameObject() {
        return this.gameObject;
    }
    
    int getTicksUntilNextAnimation() {
        return this.ticksUntilNextAnimation;
    }
    
    int getAnimationId() {
        return this.animationId;
    }
    
    int getAnimationTickSpeed() {
        return this.animationTickSpeed;
    }
    
    int getMaxTick() {
        return this.maxTick;
    }
    
    boolean isDanger() {
        return this.danger;
    }
    
    HallowedSepulchreWizardStatue(@NonNull final GameObject gameObject, final int animationId, final int animationTickSpeed) {
        this.ticksUntilNextAnimation = -20;
        this.maxTick = -1;
        this.danger = false;
        if (gameObject == null) {
            throw new NullPointerException("gameObject is marked non-null but is null");
        }
        this.gameObject = gameObject;
        this.animationId = animationId;
        this.animationTickSpeed = animationTickSpeed;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HallowedSepulchreWizardStatue)) {
            return false;
        }
        final HallowedSepulchreWizardStatue other = (HallowedSepulchreWizardStatue)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$gameObject = this.getGameObject();
        final Object other$gameObject = other.getGameObject();
        if (this$gameObject == null) {
            if (other$gameObject == null) {
                return true;
            }
        }
        else if (this$gameObject.equals(other$gameObject)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof HallowedSepulchreWizardStatue;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $gameObject = this.getGameObject();
        result = result * 59 + (($gameObject == null) ? 43 : $gameObject.hashCode());
        return result;
    }
}
