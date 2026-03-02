// Build three rainbows of different sizes in a row
drone.chkpt("start");

drone.rainbow(12);
drone.move("start").right(28);
drone.rainbow(18);
drone.move("start").right(64);
drone.rainbow(12);

player.sendMessage("Rainbow valley complete!");
