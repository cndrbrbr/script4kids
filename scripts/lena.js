// LENA Text
// Based on LENA Text.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Draws the letters L-E-N-A in quartz blocks, one block at a time.
// Stand facing the direction you want the text to face before running.

function Lena() {
    // L
    drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.fwd(3);
    drone.down(6);
    drone.box("QUARTZ_BLOCK");
    drone.turn(2);
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.turn(2);
    drone.fwd(4);
    drone.box("QUARTZ_BLOCK");

    // E
    drone.turn(3);
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.box("QUARTZ_BLOCK");
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.down(3);
    drone.turn(1);
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.turn(3);
    drone.down(3);
    drone.box("QUARTZ_BLOCK");
    drone.turn(3);
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.fwd(1); drone.box("QUARTZ_BLOCK");

    // N
    drone.down(1);
    drone.fwd(2);
    drone.turn(3);
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.down(1);
    drone.turn(1);
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.fwd(1); drone.box("QUARTZ_BLOCK");
    drone.turn(3);
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");

    // A
    drone.turn(1);
    drone.fwd(2); drone.box("QUARTZ_BLOCK");
    drone.fwd(2); drone.box("QUARTZ_BLOCK");
    drone.fwd(2); drone.box("QUARTZ_BLOCK");
    drone.fwd(2); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.down(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.box("QUARTZ_BLOCK");
    drone.down(1); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.fwd(3); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.up(1); drone.box("QUARTZ_BLOCK");
    drone.turn(1);
    drone.fwd(1);
    drone.turn(1);
    drone.down(2); drone.box("QUARTZ_BLOCK");
    drone.turn(3);
    drone.fwd(1); drone.box("QUARTZ_BLOCK");
}

Lena();
player.sendMessage("LENA complete!");
