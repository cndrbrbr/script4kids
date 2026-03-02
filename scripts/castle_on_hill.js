// Raise a hill of dirt then build a sandstone castle on top
drone.chkpt("base");

// Hill: stacked, shrinking boxes
for (var level = 0; level < 6; level++) {
    var size = 28 - level * 4;
    drone.box("GRASS_BLOCK", size, 1, size);
    drone.up(1).right(2).fwd(2);
}

// Castle on top of the hill
drone.castle("SANDSTONE", 12, 8);

player.sendMessage("Castle on a hill complete!");
