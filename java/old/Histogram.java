class Start {

    public static void main(String[] args) {


        int [][] neighbours = {{5,3,3,3,5},{3,2,1,2,3},{3,1,0,1,3},{3,2,1,2,3},{5,3,3,3,5}};
        boolean [][] occupancy = new boolean[7][12];
        int[][] houses = {{2,2},{1,4},{2,6},{3,6},{5,10}};
        for (int [] coords:houses) {
            occupancy[coords[0]][coords[1]] = true;
        }

        Histogram h = new Histogram();
        h.setNeighboursTable(neighbours);
        h.setOccupancyTable(occupancy);

        for (int hist:h.getHistogram()) {
            System.out.print(hist + " ");
        }
        System.out.print("\n"+h.noNeighbours());
    }
}

class Histogram extends HistogramBase{

    private int max(){
        int max = 0;

        for (int i = 0; i < neighbours.length/2+1; i++) {
            for (int j = 0; j < neighbours.length/2; j++) {
                if (neighbours[i][j] >max)
                    max = neighbours[i][j];
            }
        }
        int middle = neighbours[neighbours.length/2][neighbours.length/2];
        if (middle >max)
            max = middle;
        return max;
    }

    private boolean checkOccupancy(int x, int y){
        if (x<0 | y<0 | x>occupancy.length-1 | y>occupancy[0].length-1){
            return false;
        }
        return occupancy[x][y];
    }

    @Override
    public int[] getHistogram() {
        int max_value = max();
        int [] histogram = new int[max_value+1];

        for (int i = 0; i < occupancy.length; i++) {
            for (int j = 0; j < occupancy[0].length; j++) {
                if (checkOccupancy(i,j)){
                    for (int k = 0; k < neighbours.length; k++) {
                        for (int l = 0; l <  neighbours.length; l++) {
                            if (k!=neighbours.length/2 || l != neighbours.length/2){
                                if (checkOccupancy(i+k-neighbours.length/2,j+l - neighbours.length/2)){
                                    histogram[neighbours[k][l]]+=1;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < max_value+1; i++) {
            histogram[i] = histogram[i]/2;
        }
        return histogram;
    }

    @Override
    public int noNeighbours() {
        int counter = 0;
        for (int i = 0; i < occupancy.length; i++) {
            for (int j = 0; j < occupancy[0].length; j++) {
                if (checkOccupancy(i,j)){
                    boolean noNeighbour = true;
                    for (int k = 0; k < neighbours.length; k++) {
                        for (int l = 0; l <  neighbours.length; l++) {
                            if (k!=neighbours.length/2 || l != neighbours.length/2){
                                if (checkOccupancy(i+k-neighbours.length/2,j+l - neighbours.length/2)){
                                    noNeighbour = false;
                                }
                            }
                        }
                    }
                    if (noNeighbour)
                        counter+=1;
                }
            }
        }
        return counter;
    }
}
