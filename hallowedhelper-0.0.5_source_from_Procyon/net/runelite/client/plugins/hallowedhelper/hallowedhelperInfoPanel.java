// 
// Decompiled by Procyon v0.5.36
// 

package net.runelite.client.plugins.hallowedhelper;

import org.slf4j.LoggerFactory;
import net.runelite.api.GroundObject;
import net.runelite.api.GameObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.client.util.ColorUtil;
import java.awt.Color;
import net.runelite.client.ui.overlay.components.table.TableAlignment;
import net.runelite.client.ui.overlay.components.table.TableComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.api.Client;
import org.slf4j.Logger;
import net.runelite.client.ui.overlay.OverlayPanel;

public class hallowedhelperInfoPanel extends OverlayPanel
{
    private static final Logger log;
    private final Client client;
    private final hallowedhelperConfig config;
    private final hallowedhelperPlugin plugin;
    private int maxfloor;
    private int userX;
    
    @Inject
    hallowedhelperInfoPanel(final Client client, final hallowedhelperConfig config, final hallowedhelperPlugin plugin) {
        this.maxfloor = 1;
        this.userX = 0;
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.TOP_LEFT);
    }
    
    public Dimension render(final Graphics2D graphics) {
        final Player local = this.client.getLocalPlayer();
        final WorldPoint WorldPoint = local.getWorldLocation();
        final LocalPoint LocalLocation = local.getLocalLocation();
        this.maxfloor = this.getMaxFloor();
        final TableComponent tableComponent = new TableComponent();
        tableComponent.setColumnAlignments(new TableAlignment[] { TableAlignment.LEFT, TableAlignment.RIGHT });
        tableComponent.addRow(new String[] { "Hallowed-Info", "" });
        tableComponent.addRow(new String[] { "Location: ", this.getLocation() });
        tableComponent.addRow(new String[] { "TicksLeft: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.plugin.isDoorOpen() ? this.client.getVarbitValue(10392) : 0) });
        tableComponent.addRow(new String[] { "DoorOpen: ", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, this.plugin.isDoorOpen() ? ColorUtil.prependColorTag("Yes", Color.GREEN) : ColorUtil.prependColorTag("No", Color.RED)) });
        tableComponent.addRow(new String[] { "Level: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.client.getRealSkillLevel(Skill.AGILITY)) });
        if (this.config.ShowValues()) {
            tableComponent.addRow(new String[] { "Plane: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.client.getPlane()) });
            tableComponent.addRow(new String[] { "WorldX: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, WorldPoint.getX()) });
            tableComponent.addRow(new String[] { "WorldX (Reg): ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, WorldPoint.getRegionX()) });
            tableComponent.addRow(new String[] { "WorldY (Reg): ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, WorldPoint.getRegionY()) });
            tableComponent.addRow(new String[] { "WorldY: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, WorldPoint.getX()) });
            tableComponent.addRow(new String[] { "LocalX: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, LocalLocation.getX()) });
            tableComponent.addRow(new String[] { "LocalY: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, LocalLocation.getY()) });
            tableComponent.addRow(new String[] { "SceneX: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, LocalLocation.getSceneX()) });
            tableComponent.addRow(new String[] { "SceneY: ", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, LocalLocation.getSceneY()) });
            tableComponent.addRow(new String[] { "Region", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.client.getMapRegions()[0]) });
            final Integer swordcount = this.plugin.getSwords().size();
            final Integer arrowcount = this.plugin.getArrows().size();
            final Integer chestcount = this.plugin.getChests().size();
            tableComponent.addRow(new String[] { "Swords", swordcount.toString() });
            tableComponent.addRow(new String[] { "Arrows", arrowcount.toString() });
            tableComponent.addRow(new String[] { "Floor-Stairs", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.plugin.getFloor_gates().size()) });
            tableComponent.addRow(new String[] { "Stairs", invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.plugin.getStairs().size()) });
            tableComponent.addRow(new String[] { "Chests", invokedynamic(makeConcatWithConstants:(Ljava/lang/Integer;)Ljava/lang/String;, chestcount) });
        }
        this.panelComponent.getChildren().add(tableComponent);
        return super.render(graphics);
    }
    
    public String getLocation() {
        switch (this.plugin.getCurrentfloor()) {
            case -1: {
                return "Lobby";
            }
            case 1: {
                return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, ColorUtil.prependColorTag(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, this.getSubFloor()), Color.GREEN), ColorUtil.prependColorTag(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.maxfloor), Color.WHITE));
            }
            case 2: {
                return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, ColorUtil.prependColorTag("2", Color.GREEN), ColorUtil.prependColorTag(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.maxfloor), Color.WHITE));
            }
            case 3: {
                return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, ColorUtil.prependColorTag("3", Color.GREEN), ColorUtil.prependColorTag(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.maxfloor), Color.WHITE), this.ThirdrdFloorLocation());
            }
            case 4: {
                return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, ColorUtil.prependColorTag("4", Color.GREEN), ColorUtil.prependColorTag(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.maxfloor), Color.WHITE));
            }
            case 5: {
                return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, ColorUtil.prependColorTag("5", Color.GREEN), ColorUtil.prependColorTag(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.maxfloor), Color.WHITE));
            }
            default: {
                return "?";
            }
        }
    }
    
    private String ThirdrdFloorLocation() {
        if (this.plugin.getFloor_gates().size() == 0) {
            return "No Idea";
        }
        final GameObject oj = this.plugin.getFloor_gates().iterator().next();
        final LocalPoint localPoint = oj.getLocalLocation();
        final int offset = localPoint.getX() - this.client.getLocalPlayer().getLocalLocation().getX();
        if (offset > 640) {
            return "Left";
        }
        if (offset < -640) {
            return "Right";
        }
        return "Mid";
    }
    
    public String getSubFloor() {
        if (this.plugin.getBridges().size() > 0) {
            final GroundObject bridge = this.plugin.getBridges().iterator().next();
            final int xdistance = bridge.getX() - this.client.getLocalPlayer().getLocalLocation().getX();
            if (xdistance > -1408) {
                return "C";
            }
        }
        switch (this.client.getMapRegions()[0]) {
            case 8796: {
                return "A";
            }
            case 8797: {
                return "B";
            }
            case 9052: {
                return "C";
            }
            default: {
                return "";
            }
        }
    }
    
    public int getMaxFloor() {
        final int lv = this.client.getRealSkillLevel(Skill.AGILITY);
        if (lv > 91) {
            return 5;
        }
        if (lv > 81) {
            return 4;
        }
        if (lv > 71) {
            return 3;
        }
        if (lv > 61) {
            return 2;
        }
        if (lv > 51) {
            return 1;
        }
        return 0;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)hallowedhelperInfoPanel.class);
    }
}
