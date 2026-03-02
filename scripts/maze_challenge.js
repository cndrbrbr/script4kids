// Generate a random maze and tell the player where to find it
drone.fwd(5);   // start maze 5 blocks ahead so the entrance is reachable
drone.maze("STONE_BRICKS", 10, 10, 4);
player.sendMessage("Maze generated! Find the exit at the far side.");
