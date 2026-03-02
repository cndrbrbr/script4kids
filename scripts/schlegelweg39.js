// Schlegelweg 39
// Based on schlegelweg39.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a small house with basement, white wool walls, glass windows,
// a front door, and a gravel roof.

var AnzahlStockwerke = 3;
var Breite           = 9;
var HoeheStockwerk   = 3;

function BaueBodenplatte() {
    drone.box("WHITE_WOOL", Breite, 1, Breite);
    drone.up(1);
}

function BaueZwischendecke() {
    drone.box("WHITE_WOOL", Breite, 1, Breite);
    drone.up(1);
}

function BaueStockwerk() {
    drone.box0("WHITE_WOOL", Breite, HoeheStockwerk, Breite);
    drone.up(HoeheStockwerk);
}

function BaueFenster() {
    drone.down(12);
    drone.right(2);
    drone.box("GLASS", 2, 1, 1);
    drone.right(3);
    drone.down(1);
    drone.box("AIR", 1, 2, 1);
    drone.up(1);
    drone.right(2);
    drone.box("GLASS", 1, 1, 1);
    drone.up(5);
    drone.left(1);
    drone.box("GLASS", 2, 1, 1);
    drone.left(4);
    drone.box("GLASS", 2, 1, 1);
    drone.up(3);
    drone.right(4);
    drone.box("GLASS", 2, 1, 1);
    drone.left(4);
    drone.box("GLASS", 2, 1, 1);
    drone.fwd(Breite);
    drone.turn(2);
    drone.down(8);
    drone.left(5);
    drone.fwd(1);
    drone.box("GLASS", 2, 1, 1);
    drone.right(3);
    drone.down(1);
    drone.box("OAK_DOOR", 1, 2, 1);
    drone.up(1);
    drone.right(2);
    drone.box("GLASS", 2, 1, 1);
    drone.box("GOLD_BLOCK", 1, 1, 2);
}

function BaueKeller() {
    drone.box("AIR", 1, 1, 1);
    drone.down(HoeheStockwerk);
    drone.box("AIR", Breite, HoeheStockwerk, Breite);
    drone.up(HoeheStockwerk);
}

function BaueDach() {
    drone.box("WHITE_WOOL", Breite, 1, Breite);
    drone.up(1);
    drone.box("GRAVEL", Breite, 1, Breite);
}

player.sendMessage("Building Schlegelweg 39...");

BaueKeller();
BaueBodenplatte();
for (var i = 0; i < AnzahlStockwerke; i++) {
    BaueStockwerk();
    BaueZwischendecke();
}
BaueDach();
BaueFenster();

player.sendMessage("Schlegelweg 39 complete!");
