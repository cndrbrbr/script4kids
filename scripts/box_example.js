// Build a small hollow oak house in front of you
box0("OAK_PLANKS", 5, 4, 5);

// Add a glass roof on top
drone.up(4).box("GLASS", 5, 1, 5);

player.sendMessage("House built!");
