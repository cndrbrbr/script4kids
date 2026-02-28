// Baue Canada (Build Canada)
// Based on Bauecanada9.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a 10×10 grid of 10-storey quartz apartment buildings.
// Warning: this is a very large structure — run it on flat, open ground.

var AnzahlStockwerke = 10;
var Breite           = 8;
var Tiefe            = 5;
var HoeheStockwerk   = 4;

function Etage() {
    drone.box0("QUARTZ_BLOCK", Breite, HoeheStockwerk, Tiefe);
}

function Boden() {
    drone.box("STONE", Breite, 1, Tiefe);
}

function BaueHaus() {
    Boden();
    for (var j = 0; j < AnzahlStockwerke; j++) {
        drone.up(1);
        Etage();
        drone.up(HoeheStockwerk);
        Boden();
    }
}

var totalHeight = 1 + AnzahlStockwerke * (1 + HoeheStockwerk);

player.sendMessage("Building Canada grid (10×10 buildings) — please wait...");

for (var k = 0; k < 10; k++) {
    for (var i = 0; i < 10; i++) {
        BaueHaus();
        drone.down(totalHeight);
        drone.box("GLOWSTONE", 1, 1, 1);
        drone.fwd(11);
    }
    drone.turn(2);
    drone.fwd(110);
    drone.turn(1);
    drone.fwd(15);
    drone.turn(1);
}

player.sendMessage("Canada grid complete!");
