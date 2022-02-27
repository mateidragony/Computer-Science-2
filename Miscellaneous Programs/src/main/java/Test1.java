public class Test1 {


    public static void main(String[] args){

        int[][] gregory = {{2,1},{3,2}};
        int[][] thomas = gregory;
        int[][] paul = new int[2][2];

        for(int r=0; r<2; r++){
            for(int c=0; c<2; c++){
                paul[r][c] = gregory[r][c];
            }
        }



        for(int i[]:thomas){
            for(int j:i){
                System.out.print(j+", ");
            }
            System.out.println();
        }


        for(int i[]:paul){
            for(int j:i){
                System.out.print(j+", ");
            }
            System.out.println();
        }

    }

}
