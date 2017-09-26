import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energyMap;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energyMap = new double[picture.width()][picture.height()];
        populateEnergyMap();
    }

    private void populateEnergyMap() {
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyMap[i][j] = calculateEnergy(i, j);
            }
        }
    }

    /* Returns energy of pixel at (column, row) */
    private double calculateEnergy(int column, int row) {
        double xComponentEnergy, yComponentEnergy;
        int differenceRedX, differenceGreenX, differenceBlueX;
        int differenceRedY, differenceGreenY, differenceBlueY;

        int[] topNeighbour = neighbour(column, row, Direction.UP);
        int[] bottomNeighbour = neighbour(column, row, Direction.DOWN);
        int[] leftNeighbour = neighbour(column, row, Direction.LEFT);
        int[] rightNeighbour = neighbour(column, row, Direction.RIGHT);


        differenceRedX = Math.abs(picture.get(leftNeighbour[0], leftNeighbour[1]).getRed()
                - picture.get(rightNeighbour[0], rightNeighbour[1]).getRed());

        differenceGreenX = Math.abs(picture.get(leftNeighbour[0], leftNeighbour[1]).getGreen()
                - picture.get(rightNeighbour[0], rightNeighbour[1]).getGreen());


        differenceBlueX = Math.abs(picture.get(leftNeighbour[0], leftNeighbour[1]).getBlue()
                - picture.get(rightNeighbour[0], rightNeighbour[1]).getBlue());

        xComponentEnergy = differenceRedX * differenceRedX + differenceGreenX * differenceGreenX
                + differenceBlueX * differenceBlueX;


        differenceRedY = Math.abs(picture.get(topNeighbour[0], topNeighbour[1]).getRed()
                - picture.get(bottomNeighbour[0], bottomNeighbour[1]).getRed());

        differenceGreenY = Math.abs(picture.get(topNeighbour[0], topNeighbour[1]).getGreen()
                - picture.get(bottomNeighbour[0], bottomNeighbour[1]).getGreen());


        differenceBlueY = Math.abs(picture.get(topNeighbour[0], topNeighbour[1]).getBlue()
                - picture.get(bottomNeighbour[0], bottomNeighbour[1]).getBlue());


        yComponentEnergy = differenceRedY * differenceRedY + differenceGreenY * differenceGreenY
                + differenceBlueY * differenceBlueY;

        return xComponentEnergy + yComponentEnergy;
    }

    /* Returns int array that represents the neighbouring pixel in the given direction */
    private int[] neighbour(int column, int row, Direction direction) {
        int[] neighbour = new int[2];

        switch (direction) {

            case UP:
                neighbour[0] = column;
                neighbour[1] = minusOne(row, height());
                break;
            case DOWN:
                neighbour[0] = column;
                neighbour[1] = plusOne(row, height());
                break;
            case LEFT:
                neighbour[0] = minusOne(column, width());
                neighbour[1] = row;
                break;
            case RIGHT:
                neighbour[0] = plusOne(column, width());
                neighbour[1] = row;
                break;
            default:
                throw new IllegalArgumentException("Direction does not exist");
        }
        return neighbour;
    }

    /* Returns the previous index to input */
    private static int minusOne(int index, int length) {
        if (index == 0) {
            return length - 1;
        }

        return index - 1;
    }

    private static int plusOne(int index, int length) {
        if (index == length - 1) {
            return 0;
        }

        return index + 1;
    }

    /* Returns the current picture */
    public Picture picture() {
        return new Picture(picture);
    }

    /* Return the width of the current picture. */
    public int width() {
        return picture.width();
    }

    /* Return the height of the current picture. */
    public int height() {
        return picture.height();
    }

    /* Returns energy of pixel at colum x and row y. */
    public double energy(int x, int y) {
        return energyMap[x][y];
    }

    /* Return sequence of indices for horizontal seam. */
    public int[] findHorizontalSeam() {
        Picture rotatedImage = new Picture(height(), width());

        for (int i = 0; i < rotatedImage.width(); i++) {
            for (int j = 0; j < rotatedImage.height(); j++) {
                Color color = picture.get(rotatedImage.height() - 1 - j, i);
                rotatedImage.set(i, j, color);
            }
        }

        SeamCarver horizontalCarver = new SeamCarver(rotatedImage);
        int[] seam = horizontalCarver.findVerticalSeam();

        reverseIntArray(seam);
        return seam;
    }

    private void reverseIntArray(int[] array) {
        int left = 0;
        int right = array.length - 1;

        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    /* Return sequence of indices for vertical seam. */
    public int[] findVerticalSeam() {
        int[] verticalSeam = new int[height()];
        double[][] minimumCostMatrix = getMinimumCostMatrix();

        int colOfEndMinimumCostPath = 0;
        for (int i = 0; i < width(); i++) {
            if (minimumCostMatrix[i][height() - 1]
                    < minimumCostMatrix[colOfEndMinimumCostPath][height() - 1]) {
                colOfEndMinimumCostPath = i;
            }
        }

        int column = colOfEndMinimumCostPath;
        int row = height() - 1;
        verticalSeam[row] = colOfEndMinimumCostPath;
        while (row > 0) {
            verticalSeam[row - 1] = getSeamPredecessor(minimumCostMatrix, column, row);
            row--;
            column = verticalSeam[row];
        }

        return verticalSeam;
    }

    /* Returns matrix of minimum cost path ending at pixel (i,j) */
    private double[][] getMinimumCostMatrix() {
        double[][] minimumPathCost = new double[width()][height()];
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                if (j == 0) {
                    minimumPathCost[i][j] = energyMap[i][j];
                } else if (i == 0) {
                    minimumPathCost[i][j] = Math.min(
                            minimumPathCost[i][j - 1],
                            minimumPathCost[plusOne(i, width())][j - 1])
                            + energyMap[i][j];

                } else if (i == width() - 1) {
                    minimumPathCost[i][j] = Math.min(
                            minimumPathCost[minusOne(i, width())][j - 1],
                            minimumPathCost[i][j - 1])
                            + energyMap[i][j];
                } else {
                    minimumPathCost[i][j] = Math.min(
                            Math.min(minimumPathCost[i - 1][j - 1], minimumPathCost[i][j - 1]),
                            minimumPathCost[i + 1][j - 1])
                            + energyMap[i][j];
                }
            }
        }
        return minimumPathCost;
    }

    /* Returns the column of the preceding pixel in the seam */
    private int getSeamPredecessor(double[][] minimumCostMatrix, int column, int row) {
        if (column == 0) {
            if (column == width() - 1) {
                return column;
            }
            if (minimumCostMatrix[column][row - 1] < minimumCostMatrix[column + 1][row - 1]) {
                return column;
            }
            return column + 1;

        } else if (column == width() - 1) {
            if (minimumCostMatrix[column - 1][row - 1] < minimumCostMatrix[column][row - 1]) {
                return column - 1;
            }
            return column;
        } else {
            double minCost = Math.min(
                    Math.min(minimumCostMatrix[column - 1][row - 1],
                            minimumCostMatrix[column + 1][row - 1]),
                    minimumCostMatrix[column][row - 1]);

            for (int i = -1; i <= 1; i++) {
                if (minCost == minimumCostMatrix[column + i] [row - 1]) {
                    return column + i;
                }
            }
        }

        return -1;
    }

    /* Remove horizontal seam from picture. */
    public void removeHorizontalSeam(int[] seam) {
        for (int index : seam) {
            if (index < 0 || index >= height()) {
                throw new IllegalArgumentException("Invalid index in seam");
            }
        }

        this.picture = SeamRemover.removeHorizontalSeam(picture, seam);
        populateEnergyMap();
    }

    /* Remove vertical seam from picture. */
    public void removeVerticalSeam(int[] seam) {
        for (int index : seam) {
            if (index < 0 || index >= width()) {
                throw new IllegalArgumentException("Invalid index in seam");
            }
        }

        this.picture = SeamRemover.removeVerticalSeam(picture, seam);
        populateEnergyMap();
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }
}
