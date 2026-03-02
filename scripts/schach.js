// Schach (Chess pattern)
// Based on schach.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a 6×6 striped grid alternating diamond and dirt blocks,
// rising one block per row.

var dgrff;

function speicher() {
    dgrff = [11, 12, 11, 12, 11, 12];
}

function schach() {
    drone.fwd(1);
    speicher();
    for (var count = 0; count < 6; count++) {
        for (var i = 1; i <= 6; i++) {
            if (dgrff[(i - 1)] == 11) {
                drone.box("DIAMOND_BLOCK", 1, 1, 1);
            } else {
                drone.box("DIRT", 1, 1, 1);
            }
            drone.fwd(1);
        }
        drone.up(1);
        drone.turn(2);
        drone.fwd(1);
    }
}

schach();
player.sendMessage("Schach pattern complete!");
