// Treppe (Staircase)
// Based on treppe26.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a spiral staircase out of orange wool.
// Goes up 1 block and forward 1 block per step, turning every 5 steps.

drone.box("ORANGE_WOOL", 1, 1, 1);

for (var count2 = 0; count2 < 5; count2++) {
    for (var count = 0; count < 5; count++) {
        drone.up(1);
        drone.fwd(1);
        drone.box("ORANGE_WOOL", 1, 1, 1);
    }
    drone.turn(1);
}

player.sendMessage("Staircase complete!");
