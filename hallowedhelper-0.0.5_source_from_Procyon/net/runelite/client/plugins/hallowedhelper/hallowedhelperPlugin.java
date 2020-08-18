// 
// Decompiled by Procyon v0.5.36
// 

package net.runelite.client.plugins.hallowedhelper;

import org.slf4j.LoggerFactory;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.GameObjectChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.Entity;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.GameState;
import net.runelite.client.events.ConfigChanged;
import com.google.inject.Provides;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.queries.GroundObjectQuery;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import java.util.Iterator;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.coords.LocalPoint;
import java.util.HashSet;
import net.runelite.api.GroundObject;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import java.util.Set;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.Client;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import org.slf4j.Logger;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;
import net.runelite.client.plugins.Plugin;

@Extension
@PluginDescriptor(name = "Hallowed Helper", description = "Helps with hallowed.", type = PluginType.IMPORTANT)
public class hallowedhelperPlugin extends Plugin
{
    private static final Logger log;
    private static final String GAME_MESSAGE_ENTER_LOBBY1 = "You make your way back to the lobby of the Hallowed Sepulchre.";
    private static final String GAME_MESSAGE_ENTER_LOBBY2 = "The obelisk teleports you back to the lobby of the Hallowed Sepulchre.";
    private static final String GAME_MESSAGE_ENTER_SEPULCHRE = "You venture further down into the Hallowed Sepulchre.";
    private static final String GAME_MESSAGE_DOOR_CLOSES = "<col=ef1020>You hear a loud rumbling noise as the door to the next floor closes.";
    private static final String GAME_MESSAGE_DOOR_CLOSES2 = "<col=ef1020>You hear the sound of a magical barrier activating.";
    private int ticksleftvar;
    public int currentfloor;
    public int subfloor;
    @Inject
    private ConfigManager configManager;
    @Inject
    private Client client;
    @Inject
    private hallowedhelperConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private hallowedhelperOverlay hallowedhelperoverlay;
    @Inject
    private hallowedhelperInfoPanel infoPanel;
    @Inject
    private InfoBoxManager infoBoxManager;
    public int LastChangedBowTick;
    private boolean doorOpen;
    private final Set<HallowedSepulchreSword> swords;
    private final Set<NPC> arrows;
    private final Set<GameObject> chests;
    private final Set<GroundObject> bridges;
    private final Set<GameObject> portals;
    private final Set<GameObject> stairs;
    private final Set<GameObject> floor_gates;
    private final Set<GameObject> crossbowStatues;
    private final Set<GameObject> swordStatues;
    private final Set<HallowedSepulchreWizardStatue> wizardStatues;
    private boolean playerInSepulchre;
    private static final int ANIM_TICK_SPEED_2 = 2;
    private static final int ANIM_TICK_SPEED_3 = 3;
    private static final int WIZARD_STATUE_ANIM_FIRE = 8658;
    private static final Set<Integer> SWORD_STATUES;
    private static final Set<Integer> CROSSBOWMAN_STATUE_IDS;
    private static final Set<Integer> WIZARD_STATUE_2TICK_IDS;
    private static final Set<Integer> WIZARD_STATUE_3TICK_IDS;
    private static final Set<Integer> SWORD_IDS;
    private static final Set<Integer> ARROW_IDS;
    private final int bridge_id = 39527;
    private final int portal_id = 39533;
    private static final Set<Integer> STAIRS_IDS;
    private static final Set<Integer> FLOOR_GATE_IDS;
    private static final Set<Integer> CHEST_IDS;
    private int animation;
    private int graphic;
    boolean waitforspot;
    private static final Set<Integer> REGION_IDS;
    
    public hallowedhelperPlugin() {
        this.ticksleftvar = 10392;
        this.currentfloor = -1;
        this.subfloor = -1;
        this.LastChangedBowTick = 0;
        this.doorOpen = true;
        this.swords = new HashSet<HallowedSepulchreSword>();
        this.arrows = new HashSet<NPC>();
        this.chests = new HashSet<GameObject>();
        this.bridges = new HashSet<GroundObject>();
        this.portals = new HashSet<GameObject>();
        this.stairs = new HashSet<GameObject>();
        this.floor_gates = new HashSet<GameObject>();
        this.crossbowStatues = new HashSet<GameObject>();
        this.swordStatues = new HashSet<GameObject>();
        this.wizardStatues = new HashSet<HallowedSepulchreWizardStatue>();
        this.playerInSepulchre = false;
        this.animation = -1;
        this.graphic = -1;
        this.waitforspot = false;
    }
    
    public void getFloor() {
        hallowedhelperPlugin.log.info(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.client.getMapRegions()[0]));
        if (this.bridges.size() > 0) {
            final GroundObject bridge = this.bridges.iterator().next();
            final int xdistance = bridge.getX() - this.client.getLocalPlayer().getLocalLocation().getX();
            if (xdistance > -1408) {
                this.currentfloor = 1;
                this.subfloor = 3;
                return;
            }
        }
        switch (this.client.getMapRegions()[0]) {
            case 8796: {
                this.currentfloor = 1;
                this.subfloor = 1;
                break;
            }
            case 8797: {
                this.currentfloor = 1;
                this.subfloor = 2;
                break;
            }
            case 9052: {
                this.currentfloor = 1;
                this.subfloor = 3;
                break;
            }
            case 9820:
            case 9821: {
                this.currentfloor = 2;
                this.subfloor = 1;
                break;
            }
            case 9306: {
                this.currentfloor = 3;
                this.subfloor = 1;
                break;
            }
            case 9818: {
                this.currentfloor = 4;
                this.subfloor = 1;
                break;
            }
            case 8794: {
                this.currentfloor = 5;
                this.subfloor = 1;
                break;
            }
            default: {
                this.currentfloor = -1;
                this.subfloor = -1;
                break;
            }
        }
    }
    
    public void get_third_floor_sub() {
        if (this.floor_gates.size() == 0) {
            this.subfloor = 0;
            return;
        }
        final GameObject oj = this.floor_gates.iterator().next();
        final LocalPoint localPoint = oj.getLocalLocation();
        final int offset = localPoint.getX() - this.client.getLocalPlayer().getLocalLocation().getX();
        if (offset > 640) {
            this.subfloor = 0;
            return;
        }
        if (offset < -640) {
            this.subfloor = 1;
            return;
        }
        this.subfloor = 2;
    }
    
    @Subscribe
    private void onNpcSpawned(final NpcSpawned event) {
        if (!this.playerInSepulchre) {
            return;
        }
        this.addNpc(event.getNpc());
    }
    
    @Subscribe
    private void onNpcDespawned(final NpcDespawned event) {
        if (!this.playerInSepulchre) {
            return;
        }
        this.removeNpc(event.getNpc());
    }
    
    private void addNpc(final NPC npc) {
        final int id = npc.getId();
        if (hallowedhelperPlugin.ARROW_IDS.contains(id)) {
            this.arrows.add(npc);
        }
        else if (hallowedhelperPlugin.SWORD_IDS.contains(id)) {
            this.swords.add(new HallowedSepulchreSword(npc));
        }
    }
    
    private void removeNpc(final NPC npc) {
        final int id = npc.getId();
        if (hallowedhelperPlugin.ARROW_IDS.contains(id)) {
            this.arrows.remove(npc);
        }
        else if (hallowedhelperPlugin.SWORD_IDS.contains(id)) {
            for (final HallowedSepulchreSword sword : this.swords) {
                if (sword.getNpc() == npc) {
                    this.swords.remove(sword);
                    break;
                }
            }
        }
    }
    
    private void addGroundObject(final GroundObject groundObject) {
        final int id = groundObject.getId();
        if (id == 39527) {
            this.bridges.add(groundObject);
        }
    }
    
    @Subscribe
    private void onAnimationChanged(final AnimationChanged event) {
        if (!this.playerInSepulchre) {
            return;
        }
        final Player local = this.client.getLocalPlayer();
        if (event.getActor() != local) {
            return;
        }
        this.animation = local.getAnimation();
        if (this.config.GlitchyHit()) {
            if (this.animation == 1816) {
                event.getActor().setAnimation(653);
                this.waitforspot = true;
            }
            else if (this.animation == 836) {
                event.getActor().setAnimation(653);
            }
        }
        if (this.config.GlitchyGrapple() && this.animation == 6067) {
            event.getActor().setAnimation(6106);
        }
    }
    
    private boolean isInSepulchreRegion() {
        return hallowedhelperPlugin.REGION_IDS.contains(this.client.getMapRegions()[0]);
    }
    
    private void locateSepulchreGameObjects() {
        final LocatableQueryResults<GameObject> locatableQueryResults = (LocatableQueryResults<GameObject>)new GameObjectQuery().result(this.client);
        for (final GameObject gameObject : locatableQueryResults) {
            this.addGameObject(gameObject);
        }
        final LocatableQueryResults<GroundObject> groundObjectResults = (LocatableQueryResults<GroundObject>)new GroundObjectQuery().result(this.client);
        for (final GroundObject groundObject : groundObjectResults) {
            this.addGroundObject(groundObject);
        }
        for (final NPC npc : this.client.getNpcs()) {
            this.addNpc(npc);
        }
    }
    
    @Subscribe
    private void onChatMessage(final ChatMessage message) {
        if (!this.playerInSepulchre || message.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        final String message2 = message.getMessage();
        switch (message2) {
            case "<col=ef1020>You hear a loud rumbling noise as the door to the next floor closes.":
            case "<col=ef1020>You hear the sound of a magical barrier activating.": {
                this.doorOpen = false;
                message.setMessage("Some fucking idiot accidently closed the door of the sepulcre wtf!");
                break;
            }
            case "You make your way back to the lobby of the Hallowed Sepulchre.":
            case "The obelisk teleports you back to the lobby of the Hallowed Sepulchre.": {
                this.clearSepulchreGameObjects();
                break;
            }
            case "You venture further down into the Hallowed Sepulchre.": {
                this.doorOpen = true;
                if (!this.overlayManager.anyMatch(o -> o instanceof hallowedhelperOverlay)) {
                    this.overlayManager.add((Overlay)this.hallowedhelperoverlay);
                }
                if (!this.overlayManager.anyMatch(o -> o instanceof hallowedhelperInfoPanel)) {
                    this.overlayManager.add((Overlay)this.infoPanel);
                    break;
                }
                break;
            }
        }
    }
    
    public void clearSepulchreGameObjects() {
        this.swords.clear();
        this.arrows.clear();
        this.chests.clear();
        this.bridges.clear();
        this.portals.clear();
        this.floor_gates.clear();
        this.wizardStatues.clear();
        this.crossbowStatues.clear();
        this.swordStatues.clear();
        this.stairs.clear();
    }
    
    @Provides
    hallowedhelperConfig provideConfig(final ConfigManager configManager) {
        return (hallowedhelperConfig)configManager.getConfig((Class)hallowedhelperConfig.class);
    }
    
    @Subscribe
    private void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals("hallowedhelper")) {
            final String key = event.getKey();
            switch (key) {
            }
        }
    }
    
    protected void startUp() {
        if (this.client.getGameState() != GameState.LOGGED_IN || !this.isInSepulchreRegion()) {
            return;
        }
        this.getFloor();
        this.overlayManager.add((Overlay)this.hallowedhelperoverlay);
        hallowedhelperPlugin.log.info("Adding panel...");
        this.overlayManager.add((Overlay)this.infoPanel);
        this.playerInSepulchre = true;
        this.locateSepulchreGameObjects();
    }
    
    protected void shutDown() {
        this.overlayManager.remove((Overlay)this.hallowedhelperoverlay);
        this.overlayManager.remove((Overlay)this.infoPanel);
        this.clearSepulchreGameObjects();
        this.playerInSepulchre = false;
    }
    
    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        final GameState gameState = event.getGameState();
        switch (gameState) {
            case LOGGED_IN: {
                if (this.isInSepulchreRegion()) {
                    this.playerInSepulchre = true;
                    this.getFloor();
                    break;
                }
                if (this.playerInSepulchre) {
                    this.shutDown();
                    break;
                }
                break;
            }
            case LOGIN_SCREEN: {
                if (this.playerInSepulchre) {
                    this.shutDown();
                    break;
                }
                break;
            }
            default: {
                if (!this.playerInSepulchre) {
                    break;
                }
                this.clearSepulchreGameObjects();
                this.getFloor();
                if (this.client.getVarbitValue(this.ticksleftvar) == 1) {
                    this.doorOpen = false;
                    break;
                }
                break;
            }
        }
    }
    
    @Subscribe
    public void onGameTick(final GameTick event) {
        if (!this.playerInSepulchre) {
            return;
        }
        this.graphic = this.client.getLocalPlayer().getSpotAnimation();
        if (this.waitforspot && this.client.getLocalPlayer().getSpotAnimation() == 1805) {
            this.client.getLocalPlayer().setSpotAnimation(137);
            this.waitforspot = false;
        }
        this.updateStatues();
    }
    
    public int identifygameojectbyworldlocation(final GameObject sword) {
        return sword.getWorldLocation().getX() + sword.getWorldLocation().getY();
    }
    
    private void updateStatues() {
        if (this.wizardStatues.isEmpty()) {
            return;
        }
        for (final HallowedSepulchreWizardStatue wizardStatue : this.wizardStatues) {
            wizardStatue.updateTicksUntilNextAnimation();
        }
        if (this.swords.isEmpty()) {
            return;
        }
        for (final HallowedSepulchreSword sword : this.swords) {
            if (sword.getThrower() == null) {
                final GameObject thrower = this.get_sword_thrower(sword.getNpc());
                if (thrower == null) {
                    continue;
                }
                sword.setThrower(thrower);
            }
            sword.updateState();
        }
    }
    
    public GameObject get_sword_thrower(final NPC sword) {
        final int orientation = sword.getOrientation() / 512;
        for (final GameObject gameObject : this.swordStatues) {
            if (this.currentfloor == 2) {
                if (this.identifygameojectbyworldlocation(gameObject) == 19087) {
                    continue;
                }
                if (this.identifygameojectbyworldlocation(gameObject) == 9295) {}
            }
            switch (orientation) {
                case 0:
                case 2: {
                    if (Math.abs(gameObject.getWorldLocation().getX() - sword.getWorldLocation().getX()) < 4) {
                        return gameObject;
                    }
                    continue;
                }
                case 1:
                case 3: {
                    if (Math.abs(gameObject.getWorldLocation().getY() - sword.getWorldLocation().getY()) < 4) {
                        return gameObject;
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    @Subscribe
    private void onGameObjectSpawned(final GameObjectSpawned event) {
        final GameObject GO = event.getGameObject();
        this.addGameObject(GO);
    }
    
    private void addGameObject(final GameObject gameObject) {
        final int id = gameObject.getId();
        final Entity entity = gameObject.getEntity();
        if (hallowedhelperPlugin.CROSSBOWMAN_STATUE_IDS.contains(id)) {
            this.crossbowStatues.add(gameObject);
        }
        else if (hallowedhelperPlugin.SWORD_STATUES.contains(id)) {
            this.swordStatues.add(gameObject);
        }
        else if (hallowedhelperPlugin.WIZARD_STATUE_2TICK_IDS.contains(id)) {
            this.wizardStatues.add(new HallowedSepulchreWizardStatue(gameObject, 8658, 2));
        }
        else if (hallowedhelperPlugin.WIZARD_STATUE_3TICK_IDS.contains(id)) {
            this.wizardStatues.add(new HallowedSepulchreWizardStatue(gameObject, 8658, 3));
        }
        else if (hallowedhelperPlugin.CHEST_IDS.contains(id)) {
            if (!this.chests.contains(gameObject)) {
                this.chests.add(gameObject);
            }
        }
        else if (hallowedhelperPlugin.FLOOR_GATE_IDS.contains(id)) {
            if (!this.floor_gates.contains(gameObject)) {
                this.floor_gates.add(gameObject);
            }
        }
        else if (hallowedhelperPlugin.STAIRS_IDS.contains(id) && !this.stairs.contains(gameObject)) {
            this.stairs.add(gameObject);
        }
        switch (id) {
            case 39533: {
                this.portals.add(gameObject);
                break;
            }
        }
    }
    
    @Subscribe
    private void onGameObjectDespawned(final GameObjectDespawned event) {
        final GameObject gameobject = event.getGameObject();
        final int id = gameobject.getId();
        if (hallowedhelperPlugin.CROSSBOWMAN_STATUE_IDS.contains(id)) {
            this.crossbowStatues.remove(gameobject);
        }
        else if (hallowedhelperPlugin.SWORD_STATUES.contains(id)) {
            this.swordStatues.remove(gameobject);
        }
        else if (hallowedhelperPlugin.WIZARD_STATUE_2TICK_IDS.contains(id)) {
            for (final HallowedSepulchreWizardStatue statue : this.wizardStatues) {
                if (statue.getGameObject() == gameobject) {
                    this.wizardStatues.remove(statue);
                    break;
                }
            }
        }
        else if (hallowedhelperPlugin.WIZARD_STATUE_3TICK_IDS.contains(id)) {
            for (final HallowedSepulchreWizardStatue statue : this.wizardStatues) {
                if (statue.getGameObject() == gameobject) {
                    this.wizardStatues.remove(statue);
                    break;
                }
            }
        }
        else if (hallowedhelperPlugin.CHEST_IDS.contains(id)) {
            this.chests.remove(gameobject);
        }
        else if (hallowedhelperPlugin.FLOOR_GATE_IDS.contains(id)) {
            this.floor_gates.remove(gameobject);
        }
        else if (hallowedhelperPlugin.STAIRS_IDS.contains(id)) {
            this.stairs.remove(gameobject);
        }
        switch (id) {
            case 39533: {
                this.portals.remove(gameobject);
                break;
            }
        }
    }
    
    @Subscribe
    private void onGameObjectChanged(final GameObjectChanged event) {
    }
    
    @Subscribe
    private void onProjectileMoved(final ProjectileMoved event) {
    }
    
    @Subscribe
    private void onGroundObjectSpawned(final GroundObjectSpawned event) {
        final GroundObject GO = event.getGroundObject();
        this.addGroundObject(GO);
    }
    
    @Subscribe
    private void onGroundObjectDespawned(final GroundObjectDespawned event) {
        final GroundObject GO = event.getGroundObject();
        if (39527 == GO.getId()) {
            this.bridges.remove(GO);
        }
    }
    
    int getCurrentfloor() {
        return this.currentfloor;
    }
    
    int getSubfloor() {
        return this.subfloor;
    }
    
    public int getLastChangedBowTick() {
        return this.LastChangedBowTick;
    }
    
    boolean isDoorOpen() {
        return this.doorOpen;
    }
    
    Set<HallowedSepulchreSword> getSwords() {
        return this.swords;
    }
    
    Set<NPC> getArrows() {
        return this.arrows;
    }
    
    Set<GameObject> getChests() {
        return this.chests;
    }
    
    Set<GroundObject> getBridges() {
        return this.bridges;
    }
    
    Set<GameObject> getPortals() {
        return this.portals;
    }
    
    Set<GameObject> getStairs() {
        return this.stairs;
    }
    
    Set<GameObject> getFloor_gates() {
        return this.floor_gates;
    }
    
    Set<GameObject> getCrossbowStatues() {
        return this.crossbowStatues;
    }
    
    Set<GameObject> getSwordStatues() {
        return this.swordStatues;
    }
    
    Set<HallowedSepulchreWizardStatue> getWizardStatues() {
        return this.wizardStatues;
    }
    
    boolean isPlayerInSepulchre() {
        return this.playerInSepulchre;
    }
    
    int getAnimation() {
        return this.animation;
    }
    
    int getGraphic() {
        return this.graphic;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)hallowedhelperPlugin.class);
        SWORD_STATUES = Set.of(38428, 38429, 38430, 38431, 38432, 38437, 38438, 38439, 38436, 38440);
        CROSSBOWMAN_STATUE_IDS = Set.of(38444, 38445, 38446);
        WIZARD_STATUE_2TICK_IDS = Set.of(38421, 38422, 38423, 38424, 38425);
        WIZARD_STATUE_3TICK_IDS = Set.of(38409, 38410, 38411, 38412, 38416, 38417, 38418, 38419, 38420);
        SWORD_IDS = Set.of(9669, 9670, 9671);
        ARROW_IDS = Set.of(9672, 9673, 9674);
        STAIRS_IDS = Set.of(38462, 38464, 38465, 38466, 38468, 38469, 38471, 38472);
        FLOOR_GATE_IDS = Set.of(39622, 39623, 39624);
        CHEST_IDS = Set.of(39544, 39545);
        REGION_IDS = Set.of(new Integer[] { 8794, 8795, 8796, 8797, 8798, 9050, 9051, 9052, 9053, 9054, 9306, 9307, 9308, 9309, 9310, 9562, 9563, 9564, 9565, 9566, 9818, 9819, 9820, 9821, 9822, 10074, 10075, 10076, 10077, 10078, 10330, 10331, 10332, 10333, 10334 });
    }
    
    public enum ObjectState
    {
        normal, 
        shooting;
    }
    
    public class StateAndTick
    {
        ObjectState state;
        int ticknumber;
    }
}
