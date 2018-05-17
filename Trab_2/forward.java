import java.util.*;

public class forward{
	public static void main(String args[]){
		LinkedList<String> estados = new LinkedList<String>();
		LinkedList<String> observ = new LinkedList<String>();
		LinkedList<String> seqs = new LinkedList<String>();
		Scanner in = new Scanner(System.in);
	// Inputs
		System.out.println("Espaço de observações:");
		int n_observ = in.nextInt();
		for(int i = 0; i<n_observ; i++)
			observ.add(in.next());
		
		System.out.println("Quantos estados?");
		int n_estados = in.nextInt();
		for(int i = 0; i<n_estados;i++)
			estados.add(in.next());

		System.out.println("Sequencia:");
		String seq = in.next();

		double trans[][] = new double[n_estados][n_estados];
		System.out.println("Matriz de transição");
		for(int i = 0; i<n_estados; i++)
			for(int j = 0; j<n_estados; j++)
				trans[i][j] = Double.parseDouble(in.next());

			System.out.println("Matriz de emissão");
			double emiss[][] = new double[n_estados][n_observ];
			for(int i = 0; i<n_estados; i++)
				for(int j = 0; j<n_observ; j++)
					emiss[i][j]=Double.parseDouble(in.next());

				System.out.println("Array de transições iniciais");
				double iniciais[] = new double[n_estados];
				for(int i = 0; i<n_estados;i++)
					iniciais[i]=Double.parseDouble(in.next());

				System.out.println();
				foward_alg(estados,observ,trans,emiss,iniciais,seq);
			}

			public static void foward_alg(LinkedList<String> estados, LinkedList<String> observ, double trans[][], double emiss[][],double iniciais[],String seq){

				double like;
				double prevLike = 0.0;
				double diff = 1;

				LinkedList<Double> diffs = new LinkedList<Double>();
				LinkedList<Double> likes = new LinkedList<Double>();
				int iter=0;
				int linhas =0 ; 
				int colunas=0;
				double forward_matrix[][] = new double[0][0];
	//forward part

				linhas = estados.size()+1;
				colunas = seq.length()+1;
				forward_matrix = new double [linhas][colunas];
		// inicialização
				forward_matrix[0][0] = 1;
				for(int i = 1; i<linhas;i++)
					forward_matrix[i][0] = 0;

				for(int i = 1; i<linhas; i++)
					forward_matrix[i][1] = iniciais[i-1] * emiss[i-1][getIndex(Character.toString(seq.charAt(0)),observ)];
				
				for(int i = 2; i < colunas; i++){
					int obsIndex = getIndex(Character.toString(seq.charAt(i-1)),observ);
					for(int j = 1; j < linhas;j++){
						double resultado = sumit_foward(i,j,forward_matrix,colunas,linhas,trans,obsIndex,emiss);
						forward_matrix[j][i] = resultado;
					}
				}	
				Print_Result(forward_matrix,linhas,colunas);
			}

			public static int getIndex(String c,LinkedList<String>cs){
				
				for(int i = 0; i<cs.size();i++){
					if(c.equals(cs.get(i)))
						return i;
				}

				return 0;
			}

			public static double sumit_foward(int i, int j,double forward_matrix[][], int colunas, int linhas,double trans[][],int obsIndex,double emiss[][]){
				
				double sum=0;
				for(int k = 1; k<linhas;k++){
					double emit = emiss[j-1][obsIndex];
					double tmp = emit *forward_matrix[k][i-1] * trans[k-1][j-1] ;
					sum+=tmp;
				}
				return sum;
			}

			public static void Print_Result(double forward_matrix[][],int linhas,int colunas) {
				System.out.println("----------Output---------");
				System.out.println("Matriz forward:");

				for(int i = 0; i<linhas; i++){
					for(int j = 0; j<colunas; j++)
						System.out.print(forward_matrix[i][j] + " ");
					System.out.println();
				}
				
				double pseq = 0;
				for(int i = 1; i<linhas;i++){
					pseq += forward_matrix[i][colunas-1];
				}
				System.out.println();
				System.out.println("A probabilidade é " +pseq);
				System.out.println();	
			}
		}
