import java.util.*;

public class TileQuadtree {
    private Tile root;
    private static final int MAX_FILENAME_LENGTH = 7;
    private enum Direction{NW, NE, SW, SE};
    private LinkedList<Tile> tilesInQueryBox;

    public TileQuadtree() {
        this.root = new Tile("0", MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
        populateQuadTree();
    }

    /* Populate quadtree through recursion. */
    private void populateQuadTree() {
        populateQuadTreeHelper(root);
    }

    /* Recursively adds children of the given tile. */
    private void populateQuadTreeHelper(Tile tile) {
        if (tile.filename.length() == MAX_FILENAME_LENGTH) {
            tile.northWest = null;
            tile.northEast = null;
            tile.southWest = null;
            tile.southEast = null;
        } else {
            tile.northWest = createChildTile(tile, Direction.NW);
            populateQuadTreeHelper(tile.northWest);

            tile.northEast = createChildTile(tile, Direction.NE);
            populateQuadTreeHelper(tile.northEast);

            tile.southWest = createChildTile(tile, Direction.SW);
            populateQuadTreeHelper(tile.southWest);

            tile.southEast = createChildTile(tile, Direction.SE);
            populateQuadTreeHelper(tile.southEast);
        }
    }

    /* Returns new child tile with appropriate coordinates in the given direction. */
    private Tile createChildTile(Tile tile, Direction direction) {
        String filename = null;
        double ullon = 0;
        double ullat = 0;
        double lrlon = 0;
        double lrlat = 0;

        switch (direction) {

            case NW:
                filename = tile.filename.equals("0") ? "1" : tile.filename + "1";
                ullon = tile.upperLeftLongitude;
                ullat = tile.upperLeftLatitude;
                lrlon = tile.upperLeftLongitude / 2 + tile.lowerRightLongitude / 2;
                lrlat = tile.upperLeftLatitude / 2 + tile.lowerRightLatitude / 2;
                break;
            case NE:
                filename = tile.filename.equals("0") ? "2" : tile.filename + "2";
                ullon = tile.upperLeftLongitude / 2 + tile.lowerRightLongitude / 2;
                ullat = tile.upperLeftLatitude;
                lrlon = tile.lowerRightLongitude;
                lrlat = tile.upperLeftLatitude / 2 + tile.lowerRightLatitude / 2;
                break;
            case SW:
                filename = tile.filename.equals("0") ? "3" : tile.filename + "3";
                ullon = tile.upperLeftLongitude;
                ullat = tile.upperLeftLatitude / 2 + tile.lowerRightLatitude / 2;
                lrlon = tile.upperLeftLongitude / 2 + tile.lowerRightLongitude / 2;
                lrlat = tile.lowerRightLatitude;
                break;
            case SE:
                filename = tile.filename.equals("0") ? "4" : tile.filename + "4";
                ullon = tile.upperLeftLongitude / 2 + tile.lowerRightLongitude / 2;
                ullat = tile.upperLeftLatitude / 2 + tile.lowerRightLatitude / 2;
                lrlon = tile.lowerRightLongitude;
                lrlat = tile.lowerRightLatitude;
                break;
            default:
                throw new IllegalArgumentException("Expected a direction.");
        }

        return new Tile(filename, ullon, ullat, lrlon, lrlat);
    }

    /* Returns the results of the raster query. */
    public Map<String, Object> getResults(Map<String, Double> queryParams, String imgRoot) {
        boolean querySuccess;
        double queryUllon = queryParams.get("ullon");
        double queryUllat = queryParams.get("ullat");
        double queryLrlon = queryParams.get("lrlon");
        double queryLrlat = queryParams.get("lrlat");

        querySuccess = !(queryLrlon < root.upperLeftLongitude
                || queryUllon > root.lowerRightLongitude);
        querySuccess |= !(queryLrlat > root.upperLeftLatitude
                || queryUllat < root.lowerRightLatitude);
        querySuccess |= !(queryUllon > queryLrlon || queryUllat < queryLrlat);

        if (!querySuccess) {
            Map<String, Object> results = new HashMap<>();
            results.put("render_grid", null);
            results.put("raster_ul_lon", null);
            results.put("raster_ul_lat", null);
            results.put("raster_lr_lon", null);
            results.put("raster_lr_lat", null);
            results.put("depth", null);
            results.put("query_success", false);

            return results;
        }

        int depth = getDepth(queryParams);
        findAllTilesInQueryBox(queryParams, depth);

        Map<String, Object> results = getRenderGridMap(imgRoot);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }

    /* Get the level of tree with the appropriate longitudinal distance per pixel. */
    private int getDepth(Map<String, Double> params) {
        double maxLonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");

        Tile firstLvlTile = root.northWest;
        double currentLonDPP = firstLvlTile.lowerRightLongitude - firstLvlTile.upperLeftLongitude;
        currentLonDPP /= MapServer.TILE_SIZE;

        for (int i = 1; i <= MAX_FILENAME_LENGTH; i++) {
            if (currentLonDPP <= maxLonDPP) {
                return i;
            }

            currentLonDPP /= 2;
        }

        return MAX_FILENAME_LENGTH;
    }

    private void findAllTilesInQueryBox(Map<String, Double> queryParams, int depth) {
        tilesInQueryBox = new LinkedList<>();
        findAllTilesInQueryBoxHelper(root, queryParams, depth);
    }

    private void findAllTilesInQueryBoxHelper(Tile tile, Map<String, Double> queryParams,
                                              int depth) {
        boolean tileIntersectsQueryBox = tileIntersectsQueryBox(tile, queryParams);
        if (tile.filename.length() == depth && tileIntersectsQueryBox) {
            tilesInQueryBox.add(tile);
        } else if (tileIntersectsQueryBox) {
            findAllTilesInQueryBoxHelper(tile.northWest, queryParams, depth);
            findAllTilesInQueryBoxHelper(tile.northEast, queryParams, depth);
            findAllTilesInQueryBoxHelper(tile.southWest, queryParams, depth);
            findAllTilesInQueryBoxHelper(tile.southEast, queryParams, depth);
        }
    }

    /* Returns true if the given tile intersects with the query box. */
    private boolean tileIntersectsQueryBox(Tile tile, Map<String, Double> queryParams) {
        double queryUllon = queryParams.get("ullon");
        double queryUllat = queryParams.get("ullat");
        double queryLrlon = queryParams.get("lrlon");
        double queryLrlat = queryParams.get("lrlat");

        if (tile.upperLeftLongitude > queryLrlon || queryUllon > tile.lowerRightLongitude) {
            return false;
        }

        return !(tile.upperLeftLatitude < queryLrlat || queryUllat < tile.lowerRightLatitude);
    }

    /* Returns a map with the render grid relevant data. */
    private Map<String, Object> getRenderGridMap(String imgRoot) {
        Map<String, Object> renderGridMap = new HashMap<>();
        int numOfGridRows = getNumberOfGridRows();
        int numOfGridCols = getNumberOfGridCols();

        tilesInQueryBox.sort(new SortByDescendingUpperLeftLatitude());
        Tile[][] tileGrid = new Tile[numOfGridRows][numOfGridCols];
        for (int i = 0; i < numOfGridRows; i++) {
            for (int j = 0; j < numOfGridCols; j++) {
                tileGrid[i][j] = tilesInQueryBox.removeFirst();
            }
        }
        tilesInQueryBox = null;

        for (int i = 0; i < numOfGridRows; i++) {
            Arrays.sort(tileGrid[i], new SortByAscendingUpperLeftLongitude());
        }

        Tile topLeftTile = tileGrid[0][0];
        Tile lowerRightTile = tileGrid[numOfGridRows - 1][numOfGridCols - 1];
        renderGridMap.put("raster_ul_lon", topLeftTile.upperLeftLongitude);
        renderGridMap.put("raster_ul_lat", topLeftTile.upperLeftLatitude);
        renderGridMap.put("raster_lr_lon", lowerRightTile.lowerRightLongitude);
        renderGridMap.put("raster_lr_lat", lowerRightTile.lowerRightLatitude);

        String[][] renderGrid = new String[numOfGridRows][numOfGridCols];
        for (int i = 0; i < numOfGridRows; i++) {
            for (int j = 0; j < numOfGridCols; j++) {
                renderGrid[i][j] = imgRoot + tileGrid[i][j].filename + ".png";
            }
        }

        renderGridMap.put("render_grid", renderGrid);
        return renderGridMap;
    }

    /* Returns the number of rows of the render grid. */
    private int getNumberOfGridRows() {
        tilesInQueryBox.sort(new SortByAscendingUpperLeftLongitude());

        for (int i = 0; i < tilesInQueryBox.size(); i++) {
            if (i + 1 == tilesInQueryBox.size()) {
                return i + 1;
            }

            if (tilesInQueryBox.get(i).upperLeftLongitude
                    != tilesInQueryBox.get(i + 1).upperLeftLongitude) {
                return i + 1;
            }
        }

        return -1;
    }

    /* Returns the number of columns of the render grid. */
    private int getNumberOfGridCols() {
        tilesInQueryBox.sort(new SortByDescendingUpperLeftLatitude());
        for (int i = 0; i < tilesInQueryBox.size(); i++) {
            if (i + 1 == tilesInQueryBox.size()) {
                return i + 1;
            }

            if (tilesInQueryBox.get(i).upperLeftLatitude
                    != tilesInQueryBox.get(i + 1).upperLeftLatitude) {
                return i + 1;
            }
        }

        return -1;
    }

    private class Tile {
        private String filename;
        private double upperLeftLongitude;
        private double upperLeftLatitude;
        private double lowerRightLongitude;
        private double lowerRightLatitude;
        Tile northWest;
        Tile northEast;
        Tile southWest;
        Tile southEast;

        private Tile(String filename, double ullon, double ullat, double lrlon, double lrlat) {
            this.filename = filename;
            this.upperLeftLongitude = ullon;
            this.upperLeftLatitude = ullat;
            this.lowerRightLongitude = lrlon;
            this.lowerRightLatitude = lrlat;
        }
    }

    private class SortByAscendingUpperLeftLongitude implements Comparator<Tile> {
        @Override
        public int compare(Tile t1, Tile t2) {
            Double t1Ullon = t1.upperLeftLongitude;
            Double t2Ullon = t2.upperLeftLongitude;

            return t1Ullon.compareTo(t2Ullon);
        }
    }


    private class SortByDescendingUpperLeftLatitude implements Comparator<Tile> {
        @Override
        public int compare(Tile t1, Tile t2) {
            Double t1Ullat = t1.upperLeftLatitude;
            Double t2Ullat = t2.upperLeftLatitude;

            return t2Ullat.compareTo(t1Ullat);
        }
    }
}


