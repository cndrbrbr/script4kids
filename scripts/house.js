// A simple house: wooden walls with a stone brick roof
var w = 7;   // width
var d = 9;   // depth
var h = 4;   // wall height

drone.chkpt("house_start");

// Walls
drone.box0("OAK_PLANKS", w, h, d);

// Roof
drone.up(h).box("STONE_BRICKS", w, 1, d);

// Door opening (2 wide, 2 tall, in the front wall centre)
drone.move("house_start");
drone.right(2).box("AIR", 2, 2, 1);

player.sendMessage("House built!");
