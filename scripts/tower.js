// Build a 3x15 stone tower with a glass observation room on top
drone.box("STONE_BRICKS", 3, 15, 3)   // shaft
     .up(15)
     .box0("GLASS", 3, 3, 3);         // glass room
player.sendMessage("Tower complete.");
