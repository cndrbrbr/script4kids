// Roboter (Robot)
// Based on roboter.js by UmaruDoma
// https://github.com/UmaruDoma/Scriptcraft
//
// Builds a large robot figure out of wool blocks.
// Make sure you have enough open space (about 15×20×15 blocks).

function roboter() {
    // Body
    drone.box("GRAY_WOOL", 9, 12, 8);
    drone.down(6);

    // Legs
    drone.right(2);
    drone.fwd(3);
    drone.box("GRAY_WOOL", 2, 6, 2);
    drone.right(4);
    drone.box("GRAY_WOOL", 2, 6, 2);

    // Arms
    drone.up(6 + 4);
    drone.right(3);
    drone.box("GRAY_WOOL", 2, 7, 2);
    drone.left(11);
    drone.box("GRAY_WOOL", 2, 7, 2);

    // Head
    drone.up(5 + 4);
    drone.right(3);
    drone.back(2);
    drone.box("GRAY_WOOL", 7, 7, 6);

    // Eye whites
    drone.up(1);
    drone.right(1);
    drone.box("WHITE_WOOL", 5, 1, 1);

    // Eye detail: left eye
    drone.up(2);
    drone.box("WHITE_WOOL", 2, 2, 1);
    drone.right(1);
    drone.box("BLACK_WOOL");

    // Eye detail: right eye
    drone.right(2);
    drone.box("WHITE_WOOL", 2, 2, 1);
    drone.right(1);
    drone.box("BLACK_WOOL");

    // Antennas
    drone.left(1);
    drone.up(4);
    drone.fwd(3);
    drone.box("GRAY_WOOL", 1, 4, 1);
    drone.up(4);
    drone.box("RED_WOOL");
    drone.down(4);
    drone.left(2);
    drone.box("GRAY_WOOL", 1, 4, 1);
    drone.up(4);
    drone.box("RED_WOOL");
}

player.sendMessage("Building robot...");
roboter();
player.sendMessage("Robot complete!");
