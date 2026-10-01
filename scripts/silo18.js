// ============================================================================
// SILO 18 - Hugh-Howey-inspiriertes Untergrund-Silo fuer JSMN / script4kids
// ============================================================================
//
// Massstab fuer Raeume und Details: ca. 1,5 Minecraft-Bloecke pro Meter.
// Die 144 Buch-Level werden vertikal komprimiert:
//   72 begehbare Minecraft-Etagen x 5 Bloecke Hoehe
//   = je 2 Buch-Level pro Minecraft-Etage.
//
// Benutzung:
//   1. Auf eine freie, moeglichst ebene Flaeche stellen.
//   2. Die Spielerposition wird zum Mittelpunkt des Berges/Silos.
//   3. /runscript silo18
//
// ACHTUNG:
// Dieses Skript veraendert mehrere Millionen Bloecke.
// Erst in einer Testwelt ausprobieren und vorher ein Backup machen.
// ============================================================================

player.sendMessage("SILO 18: Bau beginnt.");
player.sendMessage("Bitte nicht erneut starten, bis der erste Lauf fertig ist.");

var SCALE = 1.5;

// ---------------------------------------------------------------------------
// Dimensionen
// ---------------------------------------------------------------------------

var FLOORS = 72;             // 72 Minecraft-Etagen = 144 Buch-Level
var FLOOR_HEIGHT = 5;        // 4 Bloecke Raum + 1 Block Decke/Boden
var SILO_RADIUS = 34;        // Aussendurchmesser 69 Bloecke
var INNER_RADIUS = 32;
var STAIR_RADIUS = 7;

var playerY = Math.floor(player.getY());

// Berg so hoch wie sinnvoll, aber unter der normalen Baugrenze Y=320 bleiben.
var mountainHeight = 235;
if (playerY + mountainHeight > 310) {
    mountainHeight = 310 - playerY;
}
if (mountainHeight < 80) {
    mountainHeight = 80;
}

var topFloorOffset = mountainHeight - 8;
var shaftHeight = (FLOORS - 1) * FLOOR_HEIGHT + 5;
var bottomOffset = topFloorOffset - ((FLOORS - 1) * FLOOR_HEIGHT);

// ---------------------------------------------------------------------------
// Materialien
// ---------------------------------------------------------------------------

var ROCK = "STONE";
var OUTER_WALL = "DEEPSLATE_BRICKS";
var FLOOR = "SMOOTH_STONE";
var STAIR = "STONE_BRICKS";
var LIGHT = "SEA_LANTERN";

var ADMIN = "SMOOTH_QUARTZ";
var RESIDENTIAL = "STONE_BRICKS";
var FARM = "MOSS_BLOCK";
var SUPPLY = "POLISHED_ANDESITE";
var MECHANICAL = "POLISHED_DEEPSLATE";

// ---------------------------------------------------------------------------
// Startpunkt
// ---------------------------------------------------------------------------

drone.chkpt("surface");

player.sendMessage("Berg: " + mountainHeight + " Bloecke hoch.");
player.sendMessage("Silo: " + FLOORS + " Etagen, " + shaftHeight + " Bloecke Innenhoehe.");

// ============================================================================
// 1. BERG
// ============================================================================

player.sendMessage("1/7 - Berg wird aufgebaut ...");

// Natuerlicher, deterministisch unregelmaessiger Berg.
// Radius 100 ergibt genug Fels um das Silo herum, ohne die Welt unnoetig
// mit zig Millionen Bloecken zu belasten.
drone.mountain(ROCK, 100, mountainHeight);

// ============================================================================
// 2. SILO-SCHACHT UND AUSSENWAND
// ============================================================================

player.sendMessage("2/7 - Siloschacht wird ausgehoehlt ...");

drone.move("surface").up(bottomOffset).chkpt("silo_bottom");

// Innenraum ueber die gesamte Hoehe ausraeumen.
drone.cylinder("AIR", INNER_RADIUS, shaftHeight);

// Massive zylindrische Aussenwand.
drone.move("silo_bottom");
drone.cylinder0(OUTER_WALL, SILO_RADIUS, shaftHeight);

// ============================================================================
// 3. ETAGEN
// ============================================================================

player.sendMessage("3/7 - 72 Etagen werden eingezogen ...");

for (var floor = 0; floor < FLOORS; floor++) {
    var y = floor * FLOOR_HEIGHT;

    drone.move("silo_bottom").up(y);

    // Etagenplatte
    drone.disc(FLOOR, INNER_RADIUS);

    // Zentrales Atrium / Treppenauge wieder oeffnen
    drone.disc("AIR", STAIR_RADIUS - 1);

    // Heller Ring um das Atrium
    drone.ring("IRON_BLOCK", STAIR_RADIUS);

    // Beleuchtung an vier Punkten
    drone.right(10).box(LIGHT);
    drone.left(20).box(LIGHT);
    drone.right(10).fwd(10).box(LIGHT);
    drone.back(20).box(LIGHT);
}

// ============================================================================
// 4. GROSSE ZENTRALE WENDELTREPPE
// ============================================================================

player.sendMessage("4/7 - Zentrale Wendeltreppe wird gebaut ...");

drone.move("silo_bottom");
drone.helix(STAIR, STAIR_RADIUS - 1, shaftHeight);

// Zentrale Stuetzsaeule
drone.move("silo_bottom");
drone.box(OUTER_WALL, 2, shaftHeight, 2);

// ============================================================================
// 5. FUNKTIONSBEREICHE
// ============================================================================

player.sendMessage("5/7 - Bereiche werden eingerichtet ...");

function room(floor, material, right, forward, width, depth) {
    drone.move("silo_bottom")
         .up(floor * FLOOR_HEIGHT + 1)
         .right(right)
         .fwd(forward)
         .box0(material, width, 4, depth);
}

// Die Nummerierung hier laeuft von unten (0) nach oben (71).
// Ein Minecraft-Floor repraesentiert jeweils zwei Buch-Level.

// Mechanik: ganz unten
for (var f = 0; f <= 9; f += 3) {
    room(f, MECHANICAL, 12, -7, 12, 14);
}

// Supply / Recycling
for (var f = 10; f <= 18; f += 4) {
    room(f, SUPPLY, 12, -6, 11, 12);
}

// Farmen / Hydroponik
for (var f = 19; f <= 35; f += 4) {
    room(f, FARM, 11, -8, 13, 16);

    drone.move("silo_bottom")
         .up(f * FLOOR_HEIGHT + 1)
         .right(14)
         .back(4)
         .box("WATER", 3, 1, 8);
}

// Wohnbereiche / Bazaar / Schule / Klinik
for (var f = 36; f <= 57; f += 4) {
    room(f, RESIDENTIAL, 11, -7, 12, 14);
}

// IT / Verwaltung / Sheriff im oberen Bereich
room(58, "GRAY_CONCRETE", 11, -7, 13, 14);
room(62, ADMIN, 11, -7, 13, 14);
room(66, ADMIN, 11, -7, 13, 14);
room(69, "POLISHED_BLACKSTONE_BRICKS", 11, -7, 13, 14);

// ============================================================================
// 6. OBERSTE EBENE: CAFETERIA UND SENSORWAND
// ============================================================================

player.sendMessage("6/7 - Cafeteria und Sensorbereich ...");

var topFloor = FLOORS - 1;

room(topFloor, "SMOOTH_QUARTZ", 10, -10, 18, 20);

// Dunkle Sensor-/Bildschirmwand
drone.move("silo_bottom")
     .up(topFloor * FLOOR_HEIGHT + 1)
     .right(16)
     .fwd(8)
     .box("BLACK_CONCRETE", 10, 3, 1);

// Kleiner oberirdischer Sensorkopf nahe der Bergspitze
drone.move("surface").up(mountainHeight - 3);
drone.sphere0("IRON_BLOCK", 2);

// ============================================================================
// 7. OBERFLAECHENZUGANG
// ============================================================================

player.sendMessage("7/7 - Zugangsschleuse ...");

// Eine kleine Schleuse knapp unter der Bergspitze.
drone.move("surface")
     .up(mountainHeight - 12)
     .right(4)
     .back(4)
     .box0("IRON_BLOCK", 9, 5, 9);

// kurzer Verbindungsschacht zur obersten Siloebene
drone.move("surface")
     .up(topFloorOffset)
     .right(2)
     .box("AIR", 5, 5, 18);

// ============================================================================
// FERTIG
// ============================================================================

drone.move("surface");

player.sendMessage("----------------------------------------");
player.sendMessage("SILO 18 fertig.");
player.sendMessage("Massstab innen: ca. " + SCALE + " Bloecke pro Meter.");
player.sendMessage("144 Buch-Level -> 72 Minecraft-Etagen.");
player.sendMessage("Etagenhoehe: " + FLOOR_HEIGHT + " Bloecke.");
player.sendMessage("Aussendurchmesser: " + (SILO_RADIUS * 2 + 1) + " Bloecke.");
player.sendMessage("Bergshoehe: " + mountainHeight + " Bloecke.");
player.sendMessage("----------------------------------------");
