// Build a 10x10 checkerboard floor of two materials
var mat1 = "WHITE_CONCRETE";
var mat2 = "BLACK_CONCRETE";
var size = 10;

for (var row = 0; row < size; row++) {
    drone.chkpt("row_start");
    for (var col = 0; col < size; col++) {
        var mat = ((row + col) % 2 === 0) ? mat1 : mat2;
        drone.box(mat, 1, 1, 1);
        drone.right(1);
    }
    drone.move("row_start").fwd(1);
}

player.sendMessage("Checkerboard floor complete!");
