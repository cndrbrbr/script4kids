// Glow Tower
// Based on GlowTow13.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// A skyscraper with a diamond base, glowstone floor slabs,
// hollow glass walls on each floor, and a glowstone + diamond roof.

var AnzahlStockwerke = 50;  // number of floors
var Breite           = 60;  // width and depth of the tower footprint
var HoeheStockwerk   = 4;   // height of each floor in blocks

function BaueBodenplatte() {
    drone.box("DIAMOND_BLOCK", Breite, 1, Breite);
    drone.up(1);
}

function BaueZwischendecke() {
    drone.box("GLOWSTONE", Breite, 1, Breite);
    drone.up(1);
}

function BaueStockwerk() {
    drone.box0("GLASS", Breite, HoeheStockwerk, Breite);
    drone.up(HoeheStockwerk);
}

function BaueDach() {
    drone.box("GLOWSTONE", Breite, 1, Breite);
    drone.up(1);
    drone.box("DIAMOND_BLOCK", Breite, 1, Breite);
}

player.sendMessage("Building Glow Tower (" + AnzahlStockwerke + " floors) — please wait...");

BaueBodenplatte();
for (var i = 0; i < AnzahlStockwerke; i++) {
    BaueStockwerk();
    BaueZwischendecke();
}
BaueDach();

player.sendMessage("Glow Tower complete!");
