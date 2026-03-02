// Tech Crown
// Based on techcrown.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a decorative crown out of gold, with emerald, diamond,
// and iron gem inlays.

function techcrown() {
    drone.box("GOLD_BLOCK", 9, 2, 1);
    drone.box("GOLD_BLOCK", 1, 2, 9);
    drone.fwd(8);
    drone.turn(1);
    drone.box("GOLD_BLOCK", 1, 2, 9);
    drone.fwd(8);
    drone.turn(1);
    drone.box("GOLD_BLOCK", 1, 2, 9);
    drone.up(2);
    drone.box("GOLD_BLOCK", 1, 2, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 2, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 2, 1);
    drone.turn(1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 3, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 2, 1);
    drone.turn(1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 2, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 2, 1);
    drone.turn(1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 3, 1);
    drone.fwd(2);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(4);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("GOLD_BLOCK", 1, 1, 1);
    // Gem inlays
    drone.down(1);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("DIAMOND_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("IRON_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("DIAMOND_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.turn(1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("IRON_BLOCK", 1, 1, 1);
    drone.fwd(2);
    drone.box("EMERALD_BLOCK", 1, 1, 1);
}

techcrown();
player.sendMessage("Tech Crown complete!");
