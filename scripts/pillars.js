// Build a row of 6 oak log pillars, 3 blocks apart
for (var i = 0; i < 6; i++) {
    drone.box("OAK_LOG", 1, 6, 1);
    drone.fwd(3);
}
player.sendMessage("Pillars built.");
