package com.jsmn.plugin.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.graalvm.polyglot.HostAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ScriptCraft-style Drone API exposed to pupil JavaScript.
 * Starts at the player's location facing the same direction.
 *
 * Directions: 0=east(+X), 1=south(+Z), 2=west(-X), 3=north(-Z)
 * For each dir: [fwdX, fwdZ, rightX, rightZ]
 */
public class DroneAPI {

    private static final int[][] DIR_VECTORS = {
        { 1,  0,  0,  1},  // 0 = east:  fwd=+X, right=+Z
        { 0,  1, -1,  0},  // 1 = south: fwd=+Z, right=-X
        {-1,  0,  0, -1},  // 2 = west:  fwd=-X, right=-Z
        { 0, -1,  1,  0},  // 3 = north: fwd=-Z, right=+X
    };

    private final org.bukkit.World world;
    private int x, y, z, dir;
    private final Map<String, int[]> checkpoints = new HashMap<>();

    public DroneAPI(Player player) {
        this.world = player.getWorld();
        this.x = player.getLocation().getBlockX();
        this.y = player.getLocation().getBlockY();
        this.z = player.getLocation().getBlockZ();
        this.dir = yawToDir(player.getLocation().getYaw());
    }

    private static int yawToDir(float yaw) {
        yaw = ((yaw % 360) + 360) % 360;
        if (yaw >= 315 || yaw < 45)  return 1; // south
        if (yaw < 135)               return 2; // west
        if (yaw < 225)               return 3; // north
        return 0;                               // east
    }

    private Material parseMaterial(String name) {
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.OAK_PLANKS;
        }
    }

    // ── Block placement ───────────────────────────────────────────────────────

    /** Solid box: width × height × depth starting at drone position. */
    @HostAccess.Export
    public DroneAPI box(String material, int width, int height, int depth) {
        Material mat = parseMaterial(material);
        int[] v = DIR_VECTORS[dir];
        for (int dd = 0; dd < depth; dd++) {
            for (int dh = 0; dh < height; dh++) {
                for (int dw = 0; dw < width; dw++) {
                    int bx = x + v[0] * dd + v[2] * dw;
                    int by = y + dh;
                    int bz = z + v[1] * dd + v[3] * dw;
                    world.getBlockAt(bx, by, bz).setType(mat);
                }
            }
        }
        return this;
    }

    /** Hollow box: four walls, no floor or ceiling. */
    @HostAccess.Export
    public DroneAPI box0(String material, int width, int height, int depth) {
        // front wall
        box(material, width, height, 1);
        fwd(depth - 1);
        // back wall
        box(material, width, height, 1);
        back(depth - 1);
        // left wall (width=1, full depth)
        box(material, 1, height, depth);
        right(width - 1);
        // right wall
        box(material, 1, height, depth);
        left(width - 1);
        return this;
    }

    // ── Turning ───────────────────────────────────────────────────────────────

    /** Turn n * 90 degrees clockwise (negative = anti-clockwise). */
    @HostAccess.Export
    public DroneAPI turn(int n) {
        dir = ((dir + n) % 4 + 4) % 4;
        return this;
    }

    @HostAccess.Export public DroneAPI turn()      { return turn(1); }
    @HostAccess.Export public DroneAPI turnRight() { return turn(1); }
    @HostAccess.Export public DroneAPI turnLeft()  { return turn(-1); }

    // ── Movement ──────────────────────────────────────────────────────────────

    @HostAccess.Export
    public DroneAPI fwd(int n) {
        x += DIR_VECTORS[dir][0] * n;
        z += DIR_VECTORS[dir][1] * n;
        return this;
    }

    @HostAccess.Export
    public DroneAPI back(int n) {
        return fwd(-n);
    }

    @HostAccess.Export
    public DroneAPI right(int n) {
        x += DIR_VECTORS[dir][2] * n;
        z += DIR_VECTORS[dir][3] * n;
        return this;
    }

    @HostAccess.Export
    public DroneAPI left(int n) {
        return right(-n);
    }

    @HostAccess.Export
    public DroneAPI up(int n) {
        y += n;
        return this;
    }

    @HostAccess.Export
    public DroneAPI down(int n) {
        y -= n;
        return this;
    }

    // Single-step shorthands for use in JS
    @HostAccess.Export public DroneAPI fwd()  { return fwd(1); }
    @HostAccess.Export public DroneAPI back() { return back(1); }
    @HostAccess.Export public DroneAPI right(){ return right(1); }
    @HostAccess.Export public DroneAPI left() { return left(1); }
    @HostAccess.Export public DroneAPI up()   { return up(1); }
    @HostAccess.Export public DroneAPI down() { return down(1); }

    /** Convenience: single block box. */
    @HostAccess.Export
    public DroneAPI box(String material) {
        return box(material, 1, 1, 1);
    }

    // ── Maze ─────────────────────────────────────────────────────────────────

    /**
     * Generate and place a random maze using recursive backtracking.
     * @param material wall block material
     * @param cols     number of corridor cells wide
     * @param rows     number of corridor cells deep
     * @param height   wall height in blocks (minimum 1)
     */
    @HostAccess.Export
    public DroneAPI maze(String material, int cols, int rows, int height) {
        Material mat = parseMaterial(material);
        boolean[][] grid = generateMaze(cols, rows);
        int gridW = grid[0].length; // 2*cols+1
        int gridD = grid.length;    // 2*rows+1
        int[] v = DIR_VECTORS[dir];

        for (int dd = 0; dd < gridD; dd++) {
            for (int dw = 0; dw < gridW; dw++) {
                Material blockMat = grid[dd][dw] ? mat : Material.AIR;
                for (int dh = 0; dh < height; dh++) {
                    int bx = x + v[0] * dd + v[2] * dw;
                    int by = y + dh;
                    int bz = z + v[1] * dd + v[3] * dw;
                    world.getBlockAt(bx, by, bz).setType(blockMat);
                }
            }
        }
        return this;
    }

    /** maze with default wall height of 3. */
    @HostAccess.Export
    public DroneAPI maze(String material, int cols, int rows) {
        return maze(material, cols, rows, 3);
    }

    private boolean[][] generateMaze(int cols, int rows) {
        int gridW = 2 * cols + 1;
        int gridD = 2 * rows + 1;
        boolean[][] grid = new boolean[gridD][gridW];

        // Start fully walled
        for (boolean[] row : grid) java.util.Arrays.fill(row, true);

        // Carve passages using recursive backtracking
        boolean[][] visited = new boolean[rows][cols];
        carve(grid, visited, 0, 0, rows, cols);

        // Entrance (front wall, left of centre) and exit (back wall, right of centre)
        grid[0][1] = false;
        grid[0][2] = false;
        grid[gridD - 1][gridW - 2] = false;
        grid[gridD - 1][gridW - 3] = false;

        return grid;
    }

    private void carve(boolean[][] grid, boolean[][] visited, int row, int col, int rows, int cols) {
        visited[row][col] = true;
        grid[row * 2 + 1][col * 2 + 1] = false;

        List<int[]> dirs = new ArrayList<>(List.of(new int[][]{{-1,0},{1,0},{0,-1},{0,1}}));
        Collections.shuffle(dirs);

        for (int[] d : dirs) {
            int nr = row + d[0];
            int nc = col + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                // Remove wall between current cell and neighbour
                grid[row * 2 + 1 + d[0]][col * 2 + 1 + d[1]] = false;
                carve(grid, visited, nr, nc, rows, cols);
            }
        }
    }

    // ── Sphere ────────────────────────────────────────────────────────────────

    /**
     * Build a solid sphere centred at drone position + radius (bottom of sphere
     * sits at drone y).
     */
    @HostAccess.Export
    public DroneAPI sphere(String material, int radius) {
        if (radius > 64) radius = 64;
        Material mat = parseMaterial(material);
        int[] v = DIR_VECTORS[dir];
        int cy = y + radius;

        for (int yOff = -radius; yOff <= radius; yOff++) {
            int sliceR = (int) Math.round(Math.sqrt((double) radius * radius - (double) yOff * yOff));
            for (int dr = -sliceR; dr <= sliceR; dr++) {
                for (int df = -sliceR; df <= sliceR; df++) {
                    if ((long) dr * dr + (long) df * df <= (long) sliceR * sliceR) {
                        world.getBlockAt(
                            x + v[2] * dr + v[0] * df,
                            cy + yOff,
                            z + v[3] * dr + v[1] * df
                        ).setType(mat);
                    }
                }
            }
        }
        return this;
    }

    /** Hollow sphere: only the outer shell, 1 block thick. */
    @HostAccess.Export
    public DroneAPI sphere0(String material, int radius) {
        if (radius > 64) radius = 64;
        Material mat = parseMaterial(material);
        int[] v = DIR_VECTORS[dir];
        int cy = y + radius;
        long r2     = (long) radius * radius;
        long inner2 = (long) (radius - 1) * (radius - 1);

        for (int yOff = -radius; yOff <= radius; yOff++) {
            for (int dr = -radius; dr <= radius; dr++) {
                for (int df = -radius; df <= radius; df++) {
                    long dSq = (long) yOff * yOff + (long) dr * dr + (long) df * df;
                    if (dSq >= inner2 && dSq <= r2) {
                        world.getBlockAt(
                            x + v[2] * dr + v[0] * df,
                            cy + yOff,
                            z + v[3] * dr + v[1] * df
                        ).setType(mat);
                    }
                }
            }
        }
        return this;
    }

    @HostAccess.Export public DroneAPI sphere(String material)  { return sphere(material, 10); }
    @HostAccess.Export public DroneAPI sphere0(String material) { return sphere0(material, 10); }

    // ── Rainbow ───────────────────────────────────────────────────────────────

    private static final String[] RAINBOW_COLORS = {
        "RED_WOOL", "ORANGE_WOOL", "YELLOW_WOOL", "LIME_WOOL",
        "CYAN_WOOL", "BLUE_WOOL", "PURPLE_WOOL"
    };

    /**
     * Build a rainbow arch in the vertical plane facing the player.
     * @param radius outer radius of the rainbow in blocks (default 18)
     */
    @HostAccess.Export
    public DroneAPI rainbow(int radius) {
        if (radius < 7) radius = 7;
        int[] v = DIR_VECTORS[dir];
        int outerR = radius;

        for (String color : RAINBOW_COLORS) {
            if (outerR < 1) break;
            int innerR = outerR - 2;
            Material mat = parseMaterial(color);
            for (int rOff = -outerR; rOff <= outerR; rOff++) {
                for (int uOff = 0; uOff <= outerR; uOff++) {
                    long dSq = (long) rOff * rOff + (long) uOff * uOff;
                    if (dSq >= (long) innerR * innerR && dSq <= (long) outerR * outerR) {
                        world.getBlockAt(
                            x + v[2] * rOff,
                            y + uOff,
                            z + v[3] * rOff
                        ).setType(mat);
                    }
                }
            }
            outerR -= 2;
        }
        return this;
    }

    /** rainbow with default radius of 18. */
    @HostAccess.Export
    public DroneAPI rainbow() {
        return rainbow(18);
    }

    // ── Checkpoints ───────────────────────────────────────────────────────────

    /** Save the drone's current position and direction under a name. */
    @HostAccess.Export
    public DroneAPI chkpt(String name) {
        checkpoints.put(name, new int[]{x, y, z, dir});
        return this;
    }

    /** Restore a previously saved position and direction. */
    @HostAccess.Export
    public DroneAPI move(String name) {
        int[] s = checkpoints.get(name);
        if (s != null) { x = s[0]; y = s[1]; z = s[2]; dir = s[3]; }
        return this;
    }

    // ── Castle ────────────────────────────────────────────────────────────────

    /**
     * Build a castle with corner towers, curtain walls, battlements and a gate.
     * @param material block material for walls and towers
     * @param side     inner wall length between corner towers (default 12)
     * @param height   curtain wall height (default 10)
     */
    @HostAccess.Export
    public DroneAPI castle(String material, int side, int height) {
        if (side < 10) side = 10;
        if (height < 6) height = 6;

        int tw    = 6;              // corner tower width/depth
        int th    = height + 4;     // corner tower height
        int total = 2 * tw + side;  // total outer castle side
        int xtra  = th - height;    // tower height above curtain wall

        chkpt("_castle");

        // ── Curtain wall ─────────────────────────────────────────────────────
        box0(material, total, height, total);

        // ── Corner towers (box0 top section + battlements) ───────────────────
        // Each offset is [rightBlocks, fwdBlocks] from the castle origin
        int[][] towerOffsets = {
            {0,         0        },   // front-left
            {side + tw, 0        },   // front-right
            {0,         side + tw},   // back-left
            {side + tw, side + tw},   // back-right
        };

        int[] v = DIR_VECTORS[dir];
        for (int[] off : towerOffsets) {
            int savedX = x, savedY = y, savedZ = z;
            x = savedX + v[2] * off[0] + v[0] * off[1];
            y = savedY + height;
            z = savedZ + v[3] * off[0] + v[1] * off[1];
            box0(material, tw, xtra, tw);
            y += xtra;
            merlonsAround(parseMaterial(material), tw);
            x = savedX; y = savedY; z = savedZ;
        }

        // ── Curtain wall battlements ──────────────────────────────────────────
        y += height;
        merlonsAround(parseMaterial(material), total);
        y -= height;

        // ── Gate: 2-wide × 3-tall opening in front wall centre ───────────────
        int gx = x + v[2] * (total / 2 - 1);
        int gz = z + v[3] * (total / 2 - 1);
        for (int dh = 0; dh < 3; dh++) {
            world.getBlockAt(gx,          y + dh, gz         ).setType(Material.AIR);
            world.getBlockAt(gx + v[2],   y + dh, gz + v[3]  ).setType(Material.AIR);
        }

        move("_castle");
        return this;
    }

    /** castle with default dimensions. */
    @HostAccess.Export
    public DroneAPI castle(String material) {
        return castle(material, 12, 10);
    }

    /**
     * Place alternating merlons (battlements) around all four sides of a
     * square perimeter of the given side length, at the drone's current y.
     * Does not move the drone.
     */
    private void merlonsAround(Material mat, int side) {
        int[] v = DIR_VECTORS[dir];
        // Four corners of the square, and the direction to walk along each wall
        int[] cornerX = {
            x,
            x + v[2] * (side - 1),
            x + v[0] * (side - 1) + v[2] * (side - 1),
            x + v[0] * (side - 1)
        };
        int[] cornerZ = {
            z,
            z + v[3] * (side - 1),
            z + v[1] * (side - 1) + v[3] * (side - 1),
            z + v[1] * (side - 1)
        };
        int[][] wallDir = {
            { v[2],  v[3]},   // front wall  → right
            { v[0],  v[1]},   // right side  → fwd
            {-v[2], -v[3]},   // back wall   → left
            {-v[0], -v[1]}    // left side   → back
        };
        for (int wall = 0; wall < 4; wall++) {
            for (int i = 0; i < side; i += 2) {
                world.getBlockAt(
                    cornerX[wall] + wallDir[wall][0] * i,
                    y,
                    cornerZ[wall] + wallDir[wall][1] * i
                ).setType(mat);
            }
        }
    }
}
