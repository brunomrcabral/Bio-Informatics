import java.util.*;

public class viterbi{
	public static void main(String args[]){
		LinkedList<String> estados = new LinkedList<String>();
		LinkedList<String> observ = new LinkedList<String>();
		Scanner in = new Scanner(System.in);
	//Inputs
		System.out.println("Espaço de observações:");
		int n_observ = in.nextInt();
		for(int i = 0; i<n_observ; i++)
			observ.add(in.next());

		System.out.println("Quantos estados?");
		int n_estados = in.nextInt();
		for(int i = 0; i<n_estados;i++)
			estados.add(in.next());

		System.out.println("Sequência:");
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

				viterbi(estados,observ,trans,emiss,seq,iniciais);
			}

			public static void viterbi(LinkedList<String> estados, LinkedList<String> observ, double trans[][], double emiss[][],String seq,double iniciais[]){

				int linhas = estados.size()+1;
				int colunas = seq.length()+1;
				double viterbi_matr[][] = new double [linhas][colunas];
				int viterbi_matr2[][] = new int [linhas][colunas];

	//inicialização
				viterbi_matr[0][0] = 1;
				for(int i = 1; i<linhas;i++)
					viterbi_matr[i][0] = 0;

	// viterbi_matriterbi ALgorithm
				for(int i = 1; i<linhas; i++)
					viterbi_matr[i][1] = iniciais[i-1] * emiss[i-1][getIndex(Character.toString(seq.charAt(0)),observ)];

				for(int i = 2; i < colunas; i++){
					int obsIndex = getIndex(Character.toString(seq.charAt(i-1)),observ);
					for(int j = 1; j < linhas;j++){
						double resultado [] = new double[2];
						resultado = max_iter(i,j,viterbi_matr,colunas,linhas,trans,obsIndex,emiss);

						viterbi_matr[j][i] = resultado[0];
						viterbi_matr2[j][i] = (int) Math.round(resultado[1]);
					}
				}
				Print_Result(estados,linhas,colunas,viterbi_matr,viterbi_matr2);
			}
	// Obtenção de Index
			public static int getIndex(String c,LinkedList<String>cs){
				for(int i = 0; i<cs.size();i++){
					if(c.equals(cs.get(i)))
						return i;
				}

				return 0;
			}

	// Iteracção de algoritmos 
			public static double [] max_iter(int i, int j,double viterbi_matr[][], int colunas, int linhas,double trans[][],int obsIndex,double emiss[][]){
				double max=0;
				double max_k =0;
				for(int k = 1; k<linhas;k++){
					double emit = emiss[j-1][obsIndex];
					double tmp = viterbi_matr[k][i-1] * trans[k-1][j-1] * emit;
					if(Double.compare(tmp,max) >  0){
						max = tmp;
						max_k= (double) k-1;
					}
				}

				double ret [] = new double [2];
				ret[0] = max;
				ret[1] = max_k;
				return ret;
			}

	// Impressão de Output
			public static void Print_Result(LinkedList<String> estados, int linhas, int colunas, double viterbi_matr[][], int viterbi_matr2[][]){
				System.out.println("----------Output---------");
				System.out.println("Matriz de probabilidades:");

				for(int i = 0; i<linhas; i++){
					for(int j = 0; j<colunas; j++)
						System.out.print(viterbi_matr[i][j] + " ");
					System.out.println();
				}

				System.out.println();
				System.out.println("Matriz de TraceBack:");

				for(int i = 0; i<linhas; i++){
					for(int j = 0; j<colunas; j++)
						System.out.print(viterbi_matr2[i][j] + " ");
					System.out.println();
				}

				System.out.println();
				double prob_max = 0;
				int first = 0;
				for(int i = 1; i<linhas; i++){
					if(viterbi_matr[i][colunas-1]>prob_max){
						prob_max = viterbi_matr[i][colunas-1];
						first=i;
					}
				}

				String trace_back = estados.get(first-1);

				for(int i = colunas-2; i>0; i--){
					trace_back = estados.get(viterbi_matr2[first][i+1]) + " " + trace_back;
					first = viterbi_matr2[first][i+1]+1;
				}

				System.out.println("O caminho de estados mais provável é " + trace_back + " com probabilidade " + prob_max);
				System.out.println();	
			}
		}
