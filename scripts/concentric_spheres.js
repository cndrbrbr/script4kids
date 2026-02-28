// Build three hollow spheres inside each other
drone.chkpt("centre");
drone.sphere0("GLASS", 12);
drone.move("centre");
drone.sphere0("GOLD_BLOCK", 8);
drone.move("centre");
drone.sphere0("DIAMOND_BLOCK", 4);
player.sendMessage("Spheres complete.");
