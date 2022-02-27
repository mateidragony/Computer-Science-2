package MnistLoader;

public class MnistMatrix {

    private int [][] data;

    private int nRows;
    private int nCols;

    private int label;

    public MnistMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        data = new int[nRows][nCols];
    }

    public int getValue(int r, int c) {
        return data[r][c];
    }

    public void setValue(int row, int col, int value) {
        data[row][col] = value;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public int getNumberOfColumns() {
        return nCols;
    }

    public double[] getDataInArrayForm()
    {
        double[] output = new double[nRows*nCols];
        int counter=0; 
        
        for(int r=0; r<nRows; r++)
            for(int c=0; c<nCols; c++)
            {
                output[counter] = data[r][c]/255.0;
                counter++;
            }
        
        return output;
    }
    
}