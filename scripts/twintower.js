// Twin Tower
// Based on Twintower23.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft

var Breite         = 100;  // width and depth of the tower footprint in blocks
var HoeheStockwerk = 5;    // height of each floor in blocks

// Calculate how many upper floors fit between the player's position
// and the world build limit (Y=319), accounting for the base slab,
// the 6-storey lower section, and the roof slab.
var startY           = Math.floor(player.getY());
var maxBuildY        = 319;
var fixedHeight      = 1 + (6 * HoeheStockwerk) + 1;  // base + lower section + roof
var AnzahlStockwerke = Math.floor((maxBuildY - startY - fixedHeight) / HoeheStockwerk);
var DistAElev  = 15;  // elevator core offset from the side
var DistBElev  = 27;  // elevator core offset from the front
var BreiteAElev = 71; // elevator core width
var BreiteBElev = 47; // elevator core depth
// Precompute as integers (floor) to avoid passing floats to drone methods
var HalbeAElev = Math.floor((BreiteAElev - 2) / 2);  // = 34
var HalbeBElev = Math.floor((BreiteBElev - 2) / 2);  // = 22

// ── Base slab ──────────────────────────────────────────────────────────────

function BaueBodenplatte() {
    drone.box("QUARTZ_BLOCK", Breite, 1, Breite);
}

// ── Lower section: 6-storey-tall iron/glass facade on all 4 sides ──────────

function BaueStockwerkeUnten6() {
    drone.turn(3);
    drone.back(1);
    for (var j = 0; j < 4; j++) {
        for (var i = 0; i < Breite / 3; i++) {
            drone.back(1);
            drone.box("IRON_BLOCK", 1, 6 * HoeheStockwerk, 1);
            drone.back(1);
            drone.box("GLASS", 1, 6 * HoeheStockwerk, 1);
            drone.back(1);
            drone.box("GLASS", 1, 6 * HoeheStockwerk, 1);
        }
        drone.turn(3);
    }
}

// ── Floor slab between storeys ─────────────────────────────────────────────

function BaueZwischendecke() {
    drone.box("QUARTZ_BLOCK", Breite, 1, Breite);
}

// ── One elevator shaft: hollow quartz walls, cleared interior ──────────────

function BaueAufzug() {
    drone.right(1);
    drone.box("AIR", HalbeAElev, 1, HalbeBElev);
    drone.left(1);
    drone.box0("QUARTZ_BLOCK", HalbeAElev, HoeheStockwerk, HalbeBElev);
}

// ── Four elevator shafts arranged in the centre of the floor ───────────────

function BaueAufzugtrakt() {
    drone.fwd(DistBElev);
    drone.right(DistAElev);
    BaueAufzug();
    drone.fwd(HalbeBElev + 2);
    BaueAufzug();
    drone.right(HalbeAElev + 2);
    BaueAufzug();
    drone.back(HalbeBElev + 2);
    BaueAufzug();
    drone.left(HalbeAElev + 2);
    drone.back(DistBElev);
    drone.left(DistAElev);
}

// ── One upper storey: iron/glass facade on all 4 sides + elevator core ─────

function BaueStockwerk() {
    for (var j = 0; j < 4; j++) {
        for (var i = 0; i < Breite / 2; i++) {
            drone.back(1);
            drone.box("IRON_BLOCK", 1, HoeheStockwerk, 1);
            drone.back(1);
            drone.box("GLASS", 1, HoeheStockwerk, 1);
        }
        drone.turn(3);
    }
    drone.turn(1);
    BaueAufzugtrakt();
    drone.turn(3);
}

// ── Roof slab ───────────────────────────────────────────────────────────────

function BaueDach() {
    drone.box("QUARTZ_BLOCK", Breite, 1, Breite);
}

// ── Main build sequence ─────────────────────────────────────────────────────

if (AnzahlStockwerke < 1) {
    player.sendMessage("§cNot enough space above you to build the tower. Move to lower ground.");
} else {
player.sendMessage("Building Twin Tower (" + AnzahlStockwerke + " floors, from Y=" + startY + " to Y=" + (startY + fixedHeight + AnzahlStockwerke * HoeheStockwerk) + ") — please wait...");

BaueBodenplatte();
BaueStockwerkeUnten6();
drone.up(6 * HoeheStockwerk);

for (var k = 0; k < AnzahlStockwerke; k++) {
    drone.turn(1);
    BaueZwischendecke();
    drone.turn(3);
    BaueStockwerk();
    drone.up(HoeheStockwerk);
}

drone.turn(1);
BaueDach();

var totalHeight = (6 + AnzahlStockwerke) * HoeheStockwerk + 1;
player.sendMessage("Twin Tower complete! Height: " + totalHeight + " blocks.");
} // end height check
