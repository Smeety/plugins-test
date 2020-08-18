/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Cas <https://github.com/casvandongen>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.Smeety;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.AgilityShortcut;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.WorldLocation;
import net.runelite.client.graphics.ModelOutlineRenderer;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.components.table.TableComponent;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

@Slf4j
@Singleton
class SmeetyOverlay extends Overlay
{
    @Inject
    private ItemManager itemManager;
    private final ModelOutlineRenderer modelOutlineRenderer;

    private final Client client;
    private final SmeetyConfig config;
    private final SmeetyPlugin plugin;
    private final TextComponent textComponent = new TextComponent();

    private Player player;

    @Inject
    private SmeetyOverlay(final Client client, final SmeetyConfig config, final SmeetyPlugin plugin, final ModelOutlineRenderer modelOutlineRenderer)
    {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.modelOutlineRenderer = modelOutlineRenderer;
    }


    @Override
    public Dimension render(Graphics2D graphics)
    {
        NPC npc = plugin.getNPC_NAME();
        if(npc == null)
        {
            return null;
        }

        String text = "GAYASS SMOKE DEVIL XD";
        if(plugin.is_barraged())
        {
            text += " [BARRAGED: ~" + (plugin.getBarrages_ticks_max() - plugin.getBarrages_ticks()) + "]";
        }

        int distance = client.getLocalPlayer().getWorldLocation().distanceTo(npc.getWorldLocation());
        text += " DISTANCE: " + distance;
        OverlayUtil.renderActorOverlay(graphics, npc, "", Color.YELLOW);
        render_tile(graphics, npc.getLocalLocation(), Color.RED, 12);
        OverlayText(graphics, npc.getLocalLocation(), text, Color.RED, 0, -150);
        return null;
    }

    public void OverlayText(Graphics2D graphics, LocalPoint lp, String text, Color color, int offsetx, int offsety)
    {
        final Point textPoint = Perspective.getCanvasTextLocation(client,
                graphics,
                new LocalPoint(lp.getX() + offsetx, lp.getY() + offsety),
                text,
                0);

        if(textPoint == null)//sometimes fails?e

        {
            return;
        }

        textComponent.setText(text);
        textComponent.setColor(color);
        textComponent.setPosition(new java.awt.Point(textPoint.getX(), textPoint.getY()));
        textComponent.render(graphics);
    }

    private static void drawStrokeAndFill(final Graphics2D graphics2D, final Color outlineColor, final Color fillColor,
                                          final float strokeWidth, final Shape shape)
    {
        graphics2D.setColor(outlineColor);
        final Stroke originalStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(strokeWidth));
        graphics2D.draw(shape);
        graphics2D.setColor(fillColor);
        graphics2D.fill(shape);
        graphics2D.setStroke(originalStroke);
    }

    public void render_tile(Graphics2D graphics, LocalPoint lp, Color color, int size)
    {
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 60);
        Polygon polygon = Perspective.getCanvasTileAreaPoly(client, lp, size);

        if (polygon == null)
        {
            return;
        }
        drawStrokeAndFill(graphics, color, color2,
                1.0f, polygon);
    }


}