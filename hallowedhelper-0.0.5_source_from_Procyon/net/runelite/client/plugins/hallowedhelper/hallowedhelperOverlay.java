// 
// Decompiled by Procyon v0.5.36
// 

package net.runelite.client.plugins.hallowedhelper;

import org.slf4j.LoggerFactory;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.BasicStroke;
import net.runelite.api.DynamicObject;
import net.runelite.api.Point;
import net.runelite.api.ObjectDefinition;
import net.runelite.api.GroundObject;
import java.awt.Polygon;
import java.awt.Shape;
import net.runelite.api.Perspective;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import java.util.Iterator;
import net.runelite.api.Actor;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Graphics2D;
import net.runelite.api.NPC;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.plugins.Plugin;
import java.util.Set;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.api.Client;
import java.awt.Color;
import net.runelite.client.graphics.ModelOutlineRenderer;
import javax.inject.Inject;
import net.runelite.client.game.ItemManager;
import org.slf4j.Logger;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.Overlay;

@Singleton
class hallowedhelperOverlay extends Overlay
{
    private static final Logger log;
    @Inject
    private ItemManager itemManager;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private static final int MAX_DISTANCE = 2350;
    private static final Color SHORTCUT_HIGH_LEVEL_COLOR;
    private final Client client;
    private final hallowedhelperConfig config;
    private final hallowedhelperPlugin plugin;
    private final TextComponent textComponent;
    private Player player;
    public final int bolt1 = 9671;
    public final int bolt2 = 9672;
    public final int bolt3 = 9673;
    private int bridge_build;
    private int portal_build;
    private static final Set<Integer> CHEST_ANIMATIONS;
    private int chest_closed;
    private static final int CROSSBOW_STATUE_ANIM_DEFAULT = 8681;
    private static final int CROSSBOW_STATUE_ANIM_FINAL = 8685;
    private static final int SWORD_THROW_START = 8667;
    
    @Inject
    private hallowedhelperOverlay(final Client client, final hallowedhelperConfig config, final hallowedhelperPlugin plugin, final ModelOutlineRenderer modelOutlineRenderer) {
        super((Plugin)plugin);
        this.textComponent = new TextComponent();
        this.bridge_build = 38808;
        this.portal_build = 38829;
        this.chest_closed = 38830;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.modelOutlineRenderer = modelOutlineRenderer;
    }
    
    public BufferedImage getRedCog() {
        return (BufferedImage)this.itemManager.getImage(23);
    }
    
    public void render_arrow_point(final NPC arrow, final Graphics2D graphics) {
        final int orientation = arrow.getOrientation() / 512;
        switch (orientation) {
            case 0: {
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0, -1);
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0, -2);
                break;
            }
            case 1: {
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, -1, 0);
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, -2, 0);
                break;
            }
            case 2: {
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0, 1);
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0, 2);
                break;
            }
            case 3: {
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 1, 0);
                this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 2, 0);
                break;
            }
        }
    }
    
    public void render_arrows(final Graphics2D graphics) {
        for (final NPC arrow : this.plugin.getArrows()) {
            if (arrow.getWorldLocation().getPlane() != this.client.getPlane()) {
                continue;
            }
            if (this.isOutsideRenderDistance(arrow.getLocalLocation())) {
                continue;
            }
            final LocalPoint l = arrow.getLocalLocation();
            this.render_object_server_tile(graphics, arrow.getWorldLocation(), Color.RED, 0, 0);
            this.render_arrow_point(arrow, graphics);
            if (!this.config.ShowValues()) {
                continue;
            }
            final int or = arrow.getOrientation() / 512;
            switch (arrow.getId()) {
                case 9671: {
                    OverlayUtil.renderActorOverlay(graphics, (Actor)arrow, invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, or), Color.RED);
                    continue;
                }
                case 9672: {
                    OverlayUtil.renderActorOverlay(graphics, (Actor)arrow, invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, or), Color.RED);
                    continue;
                }
                case 9673: {
                    OverlayUtil.renderActorOverlay(graphics, (Actor)arrow, invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, or), Color.RED);
                    continue;
                }
            }
        }
    }
    
    public void render_swords(final Graphics2D graphics) {
        for (final HallowedSepulchreSword SWORDCLASS : this.plugin.getSwords()) {
            final NPC sword = SWORDCLASS.getNpc();
            if (sword.getWorldLocation().getPlane() != this.client.getPlane()) {
                continue;
            }
            if (this.isOutsideRenderDistance(sword.getLocalLocation())) {
                continue;
            }
            final int or = sword.getOrientation() / 512;
            final LocalPoint l = sword.getLocalLocation();
            final GameObject thrower = SWORDCLASS.getThrower();
            if (thrower == null) {
                OverlayUtil.renderActorOverlay(graphics, (Actor)sword, invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, or), Color.RED);
                return;
            }
            final int distance = SWORDCLASS.getDistance();
            final boolean forward = SWORDCLASS.isForward();
            Color c = Color.BLACK;
            final int identifier = this.plugin.identifygameojectbyworldlocation(thrower);
            int maxdistance = 16;
            int mindistance = 2;
            int ticksuntilsafe = 0;
            switch (identifier) {
                case 21191: {
                    maxdistance = 12;
                    break;
                }
                case 13569: {
                    mindistance = 2;
                    break;
                }
            }
            if (forward) {
                if (distance > maxdistance) {
                    c = Color.RED;
                }
                else {
                    c = Color.GREEN;
                    ticksuntilsafe = maxdistance - distance;
                }
            }
            else if (distance == mindistance + 1 || distance == mindistance + 2) {
                c = Color.ORANGE;
            }
            else if (distance <= mindistance) {
                c = Color.GREEN;
            }
            else {
                c = Color.RED;
            }
            ticksuntilsafe = (int)Math.ceil(Math.abs(distance - mindistance) / 2);
            if (c == Color.RED) {
                this.OverlayText(graphics, sword.getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, ticksuntilsafe), c, 0, 0);
                this.OverlayText(graphics, thrower.getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, ticksuntilsafe), c, 0, 0);
            }
            else if (c == Color.GREEN) {
                ticksuntilsafe = (int)Math.ceil(Math.abs(maxdistance - distance) / 2) + 1;
                if (forward) {
                    this.OverlayText(graphics, sword.getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, ticksuntilsafe), c, 0, 0);
                    this.OverlayText(graphics, thrower.getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, ticksuntilsafe), c, 0, 0);
                }
                else {
                    this.OverlayText(graphics, sword.getLocalLocation(), "RUN", c, 0, 0);
                    this.OverlayText(graphics, thrower.getLocalLocation(), "RUN", c, 0, 0);
                }
            }
            else if (c == Color.ORANGE) {
                this.OverlayText(graphics, sword.getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, ticksuntilsafe), c, 0, 0);
            }
            if (this.config.ShowValues()) {
                OverlayUtil.renderTileOverlay(graphics, (TileObject)thrower, invokedynamic(makeConcatWithConstants:(IIIIIZ)Ljava/lang/String;, thrower.getId(), this.plugin.identifygameojectbyworldlocation(thrower), thrower.getWorldLocation().getX(), thrower.getWorldLocation().getY(), distance, SWORDCLASS.forward), Color.CYAN);
            }
            this.render_object_server_tile_group(graphics, sword.getWorldLocation(), c, 1, 1);
        }
    }
    
    public void render_object_server_tile(final Graphics2D graphics, final WorldPoint worldlocation, final Color color, final int offsetx, final int offsety) {
        final WorldPoint w = new WorldPoint(worldlocation.getX() + offsetx, worldlocation.getY() + offsety, worldlocation.getPlane());
        final LocalPoint localPoint = LocalPoint.fromWorld(this.client, w);
        if (localPoint == null) {
            return;
        }
        final Polygon polygon = Perspective.getCanvasTilePoly(this.client, localPoint);
        if (polygon == null) {
            return;
        }
        drawStrokeAndFill(graphics, color, this.config.ServerTileFill(), 1.0f, polygon);
    }
    
    public void render_object_server_tile_group(final Graphics2D graphics, final WorldPoint worldlocation, final Color color, final int offsetx, final int offsety) {
        final WorldPoint w = new WorldPoint(worldlocation.getX() + offsetx, worldlocation.getY() + offsety, worldlocation.getPlane());
        final LocalPoint localPoint = LocalPoint.fromWorld(this.client, w);
        if (localPoint == null) {
            return;
        }
        final Polygon polygon = Perspective.getCanvasTileAreaPoly(this.client, localPoint, 3);
        if (polygon == null) {
            return;
        }
        drawStrokeAndFill(graphics, color, this.config.ServerTileFill(), 1.0f, polygon);
    }
    
    public void render_bridges(final Graphics2D graphics) {
        for (final GroundObject bridge : this.plugin.getBridges()) {
            if (bridge.getPlane() != this.client.getPlane()) {
                continue;
            }
            if (this.isOutsideRenderDistance(bridge.getLocalLocation())) {
                continue;
            }
            ObjectDefinition definition = this.client.getObjectDefinition(bridge.getId());
            if (definition == null) {
                continue;
            }
            if (definition.getImpostorIds() != null) {
                definition = definition.getImpostor();
            }
            final int varbit = definition.getId();
            if (varbit == this.bridge_build) {
                OverlayUtil.renderClickBox(graphics, this.mouse(), bridge.getClickbox(), Color.GREEN);
            }
            else {
                OverlayUtil.renderClickBox(graphics, this.mouse(), bridge.getClickbox(), Color.YELLOW);
            }
        }
    }
    
    public Point mouse() {
        return this.client.getMouseCanvasPosition();
    }
    
    public void render_portal(final Graphics2D graphics) {
        for (final GameObject portal : this.plugin.getPortals()) {
            if (portal.getPlane() != this.client.getPlane()) {
                continue;
            }
            if (this.isOutsideRenderDistance(portal.getLocalLocation())) {
                continue;
            }
            ObjectDefinition definition = this.client.getObjectDefinition(portal.getId());
            if (definition == null) {
                continue;
            }
            if (definition.getImpostorIds() != null) {
                definition = definition.getImpostor();
            }
            final int varBit = definition.getId();
            if (varBit == this.portal_build) {
                OverlayUtil.renderClickBox(graphics, this.mouse(), portal.getClickbox(), this.config.portalOpenColor());
            }
            else {
                OverlayUtil.renderClickBox(graphics, this.mouse(), portal.getClickbox(), this.config.portalColor());
            }
        }
    }
    
    public void render_chest(final Graphics2D graphics) {
        for (final GameObject chest : this.plugin.getChests()) {
            if (chest.getPlane() != this.client.getPlane()) {
                continue;
            }
            if (this.isOutsideRenderDistance(chest.getLocalLocation())) {
                continue;
            }
            ObjectDefinition definition = this.client.getObjectDefinition(chest.getId());
            if (definition == null) {
                continue;
            }
            if (definition.getImpostorIds() != null) {
                definition = definition.getImpostor();
            }
            final int varBit = definition.getId();
            if (varBit != this.chest_closed) {
                continue;
            }
            if (this.plugin.getGraphic() == 267) {
                OverlayUtil.renderClickBox(graphics, this.mouse(), chest.getClickbox(), this.config.chestOpeningFail());
            }
            else if (hallowedhelperOverlay.CHEST_ANIMATIONS.contains(this.plugin.getAnimation())) {
                switch (this.plugin.getAnimation()) {
                    case 3692: {
                        OverlayUtil.renderClickBox(graphics, this.mouse(), chest.getClickbox(), this.config.chestOpeningColor());
                        continue;
                    }
                    case 4344: {
                        OverlayUtil.renderClickBox(graphics, this.mouse(), chest.getClickbox(), this.config.chestOpeningColor2());
                        continue;
                    }
                    case 8691: {
                        OverlayUtil.renderClickBox(graphics, this.mouse(), chest.getClickbox(), this.config.chestOpeningColor3());
                        continue;
                    }
                }
            }
            else {
                OverlayUtil.renderClickBox(graphics, this.mouse(), chest.getClickbox(), this.config.chestColor());
            }
        }
    }
    
    public void render_floor_2(final Graphics2D graphics) {
        if (this.plugin.getFloor_gates().size() == 0) {
            return;
        }
        GameObject closest = this.plugin.getFloor_gates().iterator().next();
        for (final GameObject gate : this.plugin.getFloor_gates()) {
            if (gate.getLocalLocation().distanceTo(this.player.getLocalLocation()) < closest.getLocalLocation().distanceTo(this.player.getLocalLocation())) {
                closest = gate;
            }
        }
        OverlayUtil.renderClickBox(graphics, this.mouse(), closest.getClickbox(), this.config.floorgateColor());
    }
    
    public static Color hex2Rgb(final String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }
    
    public void render_floor_gates(final Graphics2D graphics) {
        if (this.plugin.getCurrentfloor() == -1) {
            return;
        }
        if (this.plugin.getCurrentfloor() == 2) {
            this.render_floor_2(graphics);
            return;
        }
        final Player local = this.client.getLocalPlayer();
        final int clientplane = this.client.getPlane();
        for (final GameObject stairs : this.plugin.getFloor_gates()) {
            if (stairs.getPlane() != clientplane) {
                continue;
            }
            if (this.isOutsideRenderDistance(stairs.getLocalLocation())) {
                continue;
            }
            switch (this.plugin.getCurrentfloor()) {
                case 1: {
                    switch (this.plugin.getSubfloor()) {
                        case 1: {
                            if (this.client.getLocalPlayer().getLocalLocation().getSceneX() > 60) {
                                return;
                            }
                            if (stairs.getHash() != 5193373618L) {
                                continue;
                            }
                            break;
                        }
                        case 2: {
                            if (stairs.getHash() != 5193506773L) {
                                continue;
                            }
                            break;
                        }
                        case 3: {
                            if (stairs.getX() != 7936 && stairs.getX() != 11008) {
                                continue;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    final int posY = this.client.getLocalPlayer().getWorldLocation().getY();
                    if (posY > 12019) {
                        if (stairs.getY() != 6528) {
                            continue;
                        }
                        break;
                    }
                    else {
                        if (stairs.getHash() != 5193504690L) {
                            continue;
                        }
                        break;
                    }
                    break;
                }
                case 3: {
                    if (stairs.getHash() != 5193636143L) {
                        continue;
                    }
                    break;
                }
            }
            OverlayUtil.renderClickBox(graphics, this.mouse(), stairs.getClickbox(), this.config.floorgateColor());
        }
    }
    
    public void render_stairs(final Graphics2D graphics) {
        for (final GameObject stairs : this.plugin.getStairs()) {
            if (stairs.getPlane() != this.client.getPlane()) {
                continue;
            }
            final LocalPoint l = stairs.getLocalLocation();
            if (l == null) {
                continue;
            }
            if (this.isOutsideRenderDistance(l)) {
                continue;
            }
            final Shape Clickbox = stairs.getClickbox();
            if (Clickbox == null) {
                continue;
            }
            OverlayUtil.renderClickBox(graphics, this.mouse(), Clickbox, this.config.stairsColor());
        }
    }
    
    private void render_servertile(final Graphics2D graphics2D) {
        if (!this.config.ShowServerTile()) {
            return;
        }
        final WorldPoint worldPoint = this.player.getWorldLocation();
        if (worldPoint == null) {
            return;
        }
        final LocalPoint localPoint = LocalPoint.fromWorld(this.client, worldPoint);
        if (localPoint == null) {
            return;
        }
        final Polygon polygon = Perspective.getCanvasTilePoly(this.client, localPoint);
        if (polygon == null) {
            return;
        }
        drawStrokeAndFill(graphics2D, this.config.ServerTileOutline(), this.config.ServerTileFill(), 1.0f, polygon);
    }
    
    public Polygon TileOffsetter(final LocalPoint point, final int offsetx, final int offsety) {
        return Perspective.getCanvasTilePoly(this.client, new LocalPoint(point.getX() + offsetx * 128, point.getY() + offsety * 128));
    }
    
    public void OverlayText(final Graphics2D graphics, final LocalPoint lp, final String text, final Color color, final int offsetx, final int offsety) {
        final Point textPoint = Perspective.getCanvasTextLocation(this.client, graphics, new LocalPoint(lp.getX() + offsetx * 128, lp.getY() + offsety * 128), text, 0);
        if (textPoint == null) {
            return;
        }
        this.textComponent.setText(text);
        this.textComponent.setColor(color);
        this.textComponent.setPosition(new java.awt.Point(textPoint.getX(), textPoint.getY()));
        this.textComponent.render(graphics);
    }
    
    public LocalPoint localpointoffset(final LocalPoint lp, final int offsetx, final int offsety) {
        return new LocalPoint(lp.getX() + offsetx * 128, lp.getY() + offsety * 128);
    }
    
    private void render_crossbow(final Graphics2D graphics2D) {
        if (this.plugin.getCrossbowStatues().isEmpty()) {
            return;
        }
        for (final GameObject gameObject : this.plugin.getCrossbowStatues()) {
            if (gameObject.getWorldLocation().isInScene(this.client)) {
                if (this.isOutsideRenderDistance(gameObject.getLocalLocation())) {
                    continue;
                }
                final DynamicObject dynamicObject = (DynamicObject)gameObject.getEntity();
                if (dynamicObject.getAnimationID() == 8681) {
                    continue;
                }
                if (dynamicObject.getAnimationID() == 8685) {
                    continue;
                }
                final Shape shape = gameObject.getConvexHull();
                if (shape == null) {
                    continue;
                }
                drawStrokeAndFill(graphics2D, Color.RED, new Color(255, 0, 0, 20), 1.0f, shape);
            }
        }
    }
    
    private void render_sword_statues(final Graphics2D graphics2D) {
        if (this.plugin.getSwordStatues().isEmpty()) {
            return;
        }
        for (final GameObject gameObject : this.plugin.getSwordStatues()) {
            if (gameObject.getWorldLocation().isInScene(this.client)) {
                if (this.isOutsideRenderDistance(gameObject.getLocalLocation())) {
                    continue;
                }
                final DynamicObject dynamicObject = (DynamicObject)gameObject.getEntity();
                if (dynamicObject.getAnimationID() != 8667) {
                    continue;
                }
                final Shape shape = gameObject.getConvexHull();
                if (shape == null) {
                    continue;
                }
                drawStrokeAndFill(graphics2D, Color.RED, new Color(255, 0, 0, 20), 1.0f, shape);
            }
        }
    }
    
    private static void drawStrokeAndFill(final Graphics2D graphics2D, final Color outlineColor, final Color fillColor, final float strokeWidth, final Shape shape) {
        graphics2D.setColor(outlineColor);
        final Stroke originalStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(strokeWidth));
        graphics2D.draw(shape);
        graphics2D.setColor(fillColor);
        graphics2D.fill(shape);
        graphics2D.setStroke(originalStroke);
    }
    
    public void render_statues(final Graphics2D graphics) {
        for (final HallowedSepulchreWizardStatue statue : this.plugin.getWizardStatues()) {
            final GameObject gameObject = statue.getGameObject();
            if (gameObject == null) {
                if (this.config.ShowDebugInfo()) {
                    hallowedhelperOverlay.log.info("Wtf GameOBJECT is NULL");
                }
                return;
            }
            if (!gameObject.getWorldLocation().isInScene(this.client)) {
                continue;
            }
            if (this.isOutsideRenderDistance(gameObject.getLocalLocation())) {
                continue;
            }
            final int ticks = statue.getTicksUntilNextAnimation();
            final int maxtick = statue.maxTickperfloor(this.plugin.currentfloor, this.plugin.subfloor);
            if (maxtick == -1) {
                continue;
            }
            if (ticks <= -20) {
                continue;
            }
            Color color = Color.ORANGE;
            if (ticks > 1) {
                color = this.config.UnsafeTileColor();
            }
            else {
                if (ticks == 1 || ticks == 0) {
                    color = this.config.SafeTileColor();
                }
                else if (ticks == -11) {
                    color = this.config.UnsafeTileColor();
                }
                if (maxtick != -1) {
                    final int diffrence = Math.abs(maxtick - ticks);
                    if (diffrence < 1) {
                        color = this.config.UnsafeTileColor();
                    }
                    else if (diffrence == 1) {
                        color = this.config.RiskyTileColor();
                    }
                    else if (diffrence == 2) {
                        color = this.config.RiskyTileColor();
                    }
                    else if (diffrence > 2) {
                        color = this.config.SafeTileColor();
                    }
                }
            }
            final int diffrenceinticks = Math.abs(ticks - maxtick);
            this.renderLine(gameObject, graphics, color, diffrenceinticks);
            if (this.config.ShowValues()) {
                OverlayUtil.renderTileOverlay(graphics, (TileObject)statue.getGameObject(), invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, statue.getTicksUntilNextAnimation(), maxtick), color);
            }
            else if (ticks > 0) {
                if (!this.config.ShowFireTickCounter()) {
                    continue;
                }
                this.OverlayText(graphics, statue.getGameObject().getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, ticks), color, 0, 0);
            }
            else {
                if (!this.config.ShowReversedFireTickCounter()) {
                    continue;
                }
                this.OverlayText(graphics, statue.getGameObject().getLocalLocation(), invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, Math.abs(ticks - maxtick)), color, 0, 0);
            }
        }
    }
    
    public boolean checkarrow_in_line(final WorldPoint point, final boolean x) {
        for (final NPC arrow : this.plugin.getArrows()) {
            if (arrow.getWorldLocation().getPlane() != this.client.getPlane()) {
                continue;
            }
            if (this.isOutsideRenderDistance(arrow.getLocalLocation())) {
                continue;
            }
            if (x) {
                if (arrow.getWorldLocation().getX() == point.getX() && Math.abs(arrow.getWorldLocation().getY() - point.getY()) < 5) {
                    return true;
                }
                continue;
            }
            else {
                if (arrow.getWorldLocation().getY() == point.getY() && Math.abs(arrow.getWorldLocation().getX() - point.getX()) < 5) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public void renderLine(final GameObject gameObject, final Graphics2D graphics, final Color color, final int diffrenceinticks) {
        final int or = gameObject.getOrientation().getAngle() / 512;
        final WorldPoint point = gameObject.getWorldLocation();
        final LocalPoint lp = LocalPoint.fromWorld(this.client, point);
        if (or == 1 || or == 3) {
            int base = 1;
            int offset = 0;
            int offset2 = -1;
            if (or == 1) {
                base = -1;
                offset = -1;
                offset2 = 0;
            }
            final LocalPoint pointoffset1 = this.localpointoffset(lp, base * 1 + offset, offset2);
            final LocalPoint pointoffset2 = this.localpointoffset(lp, base * 2 + offset, offset2);
            final LocalPoint pointoffset3 = this.localpointoffset(lp, base * 3 + offset, offset2);
            Color color2 = color;
            Color color3 = color;
            Color color4 = color;
            if (this.config.ShowArrowDanger() && this.plugin.getCurrentfloor() > 2 && color != this.config.UnsafeTileColor()) {
                if (this.checkarrow_in_line(WorldPoint.fromLocal(this.client, pointoffset1), true)) {
                    color2 = this.config.ShowArrowDangerColor();
                }
                if (this.checkarrow_in_line(WorldPoint.fromLocal(this.client, pointoffset2), true)) {
                    color3 = this.config.ShowArrowDangerColor();
                }
                if (this.checkarrow_in_line(WorldPoint.fromLocal(this.client, pointoffset3), true)) {
                    color4 = this.config.ShowArrowDangerColor();
                }
            }
            if (this.config.ShowValues()) {
                final int distance = WorldPoint.fromLocal(this.client, pointoffset1).distanceTo2D(this.client.getLocalPlayer().getWorldLocation());
                this.OverlayText(graphics, lp, invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, distance, Math.abs(distance - diffrenceinticks)), Color.ORANGE, base * 1 + offset, offset2);
                final int distance2 = WorldPoint.fromLocal(this.client, pointoffset2).distanceTo2D(this.client.getLocalPlayer().getWorldLocation());
                this.OverlayText(graphics, lp, invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, distance2, Math.abs(distance - diffrenceinticks)), Color.ORANGE, base * 2 + offset, offset2);
                final int distance3 = WorldPoint.fromLocal(this.client, pointoffset3).distanceTo2D(this.client.getLocalPlayer().getWorldLocation());
                this.OverlayText(graphics, lp, invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, distance3, Math.abs(distance - diffrenceinticks)), Color.ORANGE, base * 3 + offset, offset2);
            }
            Polygon pl = this.TileOffsetter(lp, base * 1 + offset, offset2);
            if (pl == null) {
                return;
            }
            OverlayUtil.renderPolygonThin(graphics, pl, color2);
            pl = this.TileOffsetter(lp, base * 2 + offset, offset2);
            if (pl == null) {
                return;
            }
            OverlayUtil.renderPolygonThin(graphics, pl, color3);
            pl = this.TileOffsetter(lp, base * 3 + offset, offset2);
            if (pl == null) {
                return;
            }
            OverlayUtil.renderPolygonThin(graphics, pl, color4);
        }
        else {
            int base = 1;
            int offset = 0;
            int offset2 = 0;
            if (or == 0) {
                base = -1;
                offset = -1;
                offset2 = -1;
            }
            final LocalPoint pointoffset1 = this.localpointoffset(lp, offset2, base * 1 + offset);
            final LocalPoint pointoffset2 = this.localpointoffset(lp, offset2, base * 2 + offset);
            final LocalPoint pointoffset3 = this.localpointoffset(lp, offset2, base * 3 + offset2);
            Color color2 = color;
            Color color3 = color;
            Color color4 = color;
            if (this.config.ShowArrowDanger() && this.plugin.getCurrentfloor() > 2 && color != this.config.UnsafeTileColor()) {
                if (this.checkarrow_in_line(WorldPoint.fromLocal(this.client, pointoffset1), false)) {
                    color2 = this.config.ShowArrowDangerColor();
                }
                if (this.checkarrow_in_line(WorldPoint.fromLocal(this.client, pointoffset2), false)) {
                    color3 = this.config.ShowArrowDangerColor();
                }
                if (this.checkarrow_in_line(WorldPoint.fromLocal(this.client, pointoffset3), false)) {
                    color4 = this.config.ShowArrowDangerColor();
                }
            }
            if (this.config.ShowValues()) {
                final int distance = WorldPoint.fromLocal(this.client, pointoffset1).distanceTo2D(this.client.getLocalPlayer().getWorldLocation());
                this.OverlayText(graphics, lp, invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, distance, Math.abs(distance - diffrenceinticks)), Color.ORANGE, offset2, base * 1 + offset);
                final int distance2 = WorldPoint.fromLocal(this.client, pointoffset2).distanceTo2D(this.client.getLocalPlayer().getWorldLocation());
                this.OverlayText(graphics, lp, invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, distance2, Math.abs(distance - diffrenceinticks)), Color.ORANGE, offset2, base * 2 + offset);
                final int distance3 = WorldPoint.fromLocal(this.client, pointoffset3).distanceTo2D(this.client.getLocalPlayer().getWorldLocation());
                this.OverlayText(graphics, lp, invokedynamic(makeConcatWithConstants:(II)Ljava/lang/String;, distance3, Math.abs(distance - diffrenceinticks)), Color.ORANGE, offset2, base * 3 + offset);
            }
            Polygon pl = this.TileOffsetter(lp, offset2, base * 1 + offset);
            if (pl == null) {
                return;
            }
            OverlayUtil.renderPolygonThin(graphics, pl, color2);
            pl = this.TileOffsetter(lp, offset2, base * 2 + offset);
            if (pl == null) {
                return;
            }
            OverlayUtil.renderPolygonThin(graphics, pl, color3);
            pl = this.TileOffsetter(lp, offset2, base * 3 + offset);
            if (pl == null) {
                return;
            }
            OverlayUtil.renderPolygonThin(graphics, pl, color4);
        }
    }
    
    private boolean isOutsideRenderDistance(final LocalPoint localPoint) {
        final int maxDistance = this.config.renderDistance().getDistance();
        return maxDistance != 0 && localPoint.distanceTo(this.player.getLocalLocation()) >= maxDistance;
    }
    
    public Dimension render(final Graphics2D graphics) {
        if (!this.plugin.isPlayerInSepulchre()) {
            return null;
        }
        this.player = this.client.getLocalPlayer();
        if (this.player == null) {
            return null;
        }
        this.render_arrows(graphics);
        this.render_swords(graphics);
        this.render_bridges(graphics);
        this.render_crossbow(graphics);
        this.render_servertile(graphics);
        this.render_sword_statues(graphics);
        if (this.config.ShowStairs()) {
            this.render_stairs(graphics);
        }
        this.render_statues(graphics);
        if (this.plugin.isDoorOpen()) {
            if (this.config.ShowChests()) {
                this.render_chest(graphics);
            }
            if (this.config.ShowPortals()) {
                this.render_portal(graphics);
            }
            if (this.config.ShowFloorGate()) {
                this.render_floor_gates(graphics);
            }
        }
        return null;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)hallowedhelperOverlay.class);
        SHORTCUT_HIGH_LEVEL_COLOR = Color.ORANGE;
        CHEST_ANIMATIONS = Set.of(3692, 4344, 8691);
    }
}
