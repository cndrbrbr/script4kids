// Trans Pride Flag
// Based on tprideflag.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a transgender pride flag using beacons on diamond bases
// with colored stained glass on top: blue, pink, white, pink, blue.

function beaconBlue() {
    drone.box("DIAMOND_BLOCK", 3, 1, 3);
    drone.up(1);
    drone.fwd(1);
    drone.turn(1);
    drone.fwd(1);
    drone.box("BEACON", 1, 1, 1);
    drone.up(1);
    drone.box("CYAN_STAINED_GLASS", 1, 1, 1);
}

function beaconPink() {
    drone.box("DIAMOND_BLOCK", 3, 1, 3);
    drone.up(1);
    drone.fwd(1);
    drone.turn(1);
    drone.fwd(1);
    drone.box("BEACON", 1, 1, 1);
    drone.up(1);
    drone.box("PINK_STAINED_GLASS", 1, 1, 1);
}

function beaconWhite() {
    drone.box("DIAMOND_BLOCK", 3, 1, 3);
    drone.up(1);
    drone.fwd(1);
    drone.turn(1);
    drone.fwd(1);
    drone.box("BEACON", 1, 1, 1);
    drone.up(1);
    drone.box("WHITE_STAINED_GLASS", 1, 1, 1);
}

function gonext() {
    drone.turn(2);
    drone.fwd(1);
    drone.turn(1);
    drone.down(2);
    drone.fwd(2);
}

beaconBlue();
gonext();
beaconPink();
gonext();
beaconWhite();
gonext();
beaconPink();
gonext();
beaconBlue();

player.sendMessage("Trans Pride Flag complete!");
