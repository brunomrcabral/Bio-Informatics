import java.util.*;

public class bw{
	public static void main(String args[]){

		LinkedList<String> estados = new LinkedList<String>();
		LinkedList<String> observ = new LinkedList<String>();
		LinkedList<String> seqs = new LinkedList<String>();
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

		System.out.println("Quantas sequências?");
		int n_sequencias = in.nextInt();
		for(int i = 0; i<n_sequencias;i++)
			seqs.add(in.next());

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

				System.out.println("Threshold:");
				double threshold = Double.parseDouble(in.next());

				System.out.println("Máximo de Iterações:");
				int maxIter = in.nextInt();

				System.out.println();
				baumWelch(estados,observ,trans,emiss,iniciais,seqs,threshold,maxIter);
			}

			public static void baumWelch(LinkedList<String> estados, LinkedList<String> observ, double trans[][], double emiss[][],double iniciais[],LinkedList<String> seqs,double threshold,int maxIter){
				double like;
				double prevLike = 0.0;
				double diff = 1;

				LinkedList<Double> diffs = new LinkedList<Double>();
				LinkedList<Double> likes = new LinkedList<Double>();
				int iter=0;

	//Forward 
				while(Math.abs(diff)>threshold && iter<maxIter){

					double A[][] = new double[estados.size()][estados.size()];
					double E[][] = new double[estados.size()][observ.size()];
					LinkedList<Double> probs = new LinkedList<Double>();

					for(int s = 0; s<seqs.size();s++){
						String seq = seqs.get(s);
						int linhas = estados.size()+1;
						int colunas = seq.length()+1;
						double foward_matrix[][] = new double [linhas][colunas];
						double backward_matrix[][] = new double [linhas][colunas];
						int sts[][] = new int[linhas][colunas];

		//Inicialização    
						foward_matrix[0][0] = 1;
						for(int i = 1; i<linhas;i++)
							foward_matrix[i][0] = 0;

						for(int i = 1; i<linhas; i++)
							foward_matrix[i][1] = iniciais[i-1] * emiss[i-1][getIndex(Character.toString(seq.charAt(0)),observ)];

						for(int i = 2; i < colunas; i++){
							int obsIndex = getIndex(Character.toString(seq.charAt(i-1)),observ);
							for(int j = 1; j < linhas;j++){
								double resultado = sumit_foward(i,j,foward_matrix,colunas,linhas,trans,obsIndex,emiss);
								foward_matrix[j][i] = resultado;
							}
						}

		// Print de controlo
		/*
		System.out.println("Matriz forward:");

		for(int i = 0; i<linhas; i++){
		    for(int j = 0; j<colunas; j++)
			System.out.print(foward_matrix[i][j] + " ");
		    System.out.println();
		}

		System.out.println();
		*/

		double pseq = 0;
		for(int i = 1; i<linhas;i++){
			pseq += foward_matrix[i][colunas-1];
		}

		probs.add(pseq);

		/*System.out.println();
		System.out.println("A prob é " +pseq);
		System.out.println();
		*/
		
		// Backward 
		for(int i = 1; i<linhas;i++)
			backward_matrix[i][colunas-1] = 1;

		for(int i = colunas-2; i>0; i--){
			for(int j = 1; j<linhas;j++){
				int obsIndex = getIndex(Character.toString(seq.charAt(i)),observ);
				backward_matrix[j][i] = sumit_backward(i,j,backward_matrix,colunas,linhas,trans,obsIndex,emiss);
			}
		}

		// Print de controlo
		/* 
		System.out.println("Matriz backward:");

		for(int i = 0; i<linhas; i++){
		    for(int j = 0; j<colunas; j++)
			System.out.print(backward_matrix[i][j] + " ");
		    System.out.println();
		}

		System.out.println();
		*/

		// Updates
		// Update A	
		for(int k = 0; k<linhas-1; k++){
			for(int l = 0; l<linhas-1;l++){
				double esq = 1/pseq;
				double dir = 0;
				for(int i =1; i<seq.length(); i++){
					dir += foward_matrix[k+1][i] * trans[k][l] * emiss [l][getIndex(Character.toString(seq.charAt(i)),observ)] * backward_matrix[l+1][i+1]; 
				}
				A[k][l] += esq * dir;
			}
		}

		// Update E
		for(int k = 0; k<linhas-1; k++){
			for(int bb =0; bb<observ.size();bb++){
				double esq = 1/pseq;
				double dir =0;
				for(int i =1; i<=seq.length();i++){
					if(getIndex(Character.toString(seq.charAt(i-1)),observ) == bb){
						dir += foward_matrix[k+1][i] * backward_matrix[k+1][i];
					}
				}
				E[k][bb] +=esq * dir;
			}
		}
		// Print de controlo
		/*System.out.println();
		System.out.println("A:");

		for(int i = 0; i<estados.size();i++){
		    for(int j =0; j<estados.size(); j++){
			System.out.print(A[i][j] + " ");
		    }
		    System.out.println();
		}

		System.out.println();
		System.out.println("E");
	    
		for(int i = 0; i<estados.size();i++){
		    for(int j =0; j<observ.size(); j++){
			System.out.print(E[i][j] + " ");
		    }
		    System.out.println();
		}
		System.out.println();
		*/

	}

	    //Update Transicao
	for(int k = 0; k<estados.size(); k++){
		for(int l = 0; l<estados.size();l++){
			double sum_A = 0;
			for(int ll = 0; ll<estados.size();ll++)
				sum_A+= A[k][ll];
			trans[k][l] = A[k][l] / sum_A;
		}
	}

	    //Update Emissao
	for(int k = 0; k<estados.size(); k++){
		for(int l = 0; l<observ.size();l++){
			double sum_E = 0;
			for(int ll = 0; ll<observ.size();ll++)
				sum_E+= A[k][ll];
			emiss[k][l] = A[k][l] / sum_E;
		}
	}

	like=0;

	    //System.out.println("Probs:");
	    //System.out.println(probs);

	for(Double p : probs){
		like+=Math.log(p);
	}

	    //System.out.println(like);

	likes.add(like);

	diff = like-prevLike;
	    //System.out.println(Math.abs(diff));
	diffs.add(Math.abs(diff));
	prevLike = like;
	iter++;

	    //System.out.println(diffs);
}
Print_Result(estados,observ,trans,emiss);
}

public static int getIndex(String c,LinkedList<String>cs){
	for(int i = 0; i<cs.size();i++){
		if(c.equals(cs.get(i)))
			return i;
	}

	return 0;
}

public static double sumit_foward(int i, int j,double foward_matrix[][], int colunas, int linhas,double trans[][],int obsIndex,double emiss[][]){
	double sum=0;
	for(int k = 1; k<linhas;k++){
		double emit = emiss[j-1][obsIndex];
		double temp = emit * foward_matrix[k][i-1] * trans[k-1][j-1];
		sum+=temp;
	}
	return sum;
}

public static double sumit_backward(int i, int j,double backward_matrix[][], int colunas, int linhas,double trans[][],int obsIndex,double emiss[][]){
	double sum=0;
	for(int k = 1; k<linhas;k++){
		double emit = emiss[k-1][obsIndex];
		double temp = emit * backward_matrix[k][i+1] * trans[j-1][k-1];
		sum+=temp;
	}
	return sum;
}

public static void Print_Result(LinkedList<String> estados,LinkedList<String> observ, double trans[][],double emiss[][]) {
	System.out.println("----------Output---------");
	System.out.println("Transições:");

	for(int i = 0; i<estados.size();i++){
		for(int j = 0; j<estados.size(); j++)
			System.out.print(trans[i][j] + " ");
		System.out.println();
	}

	System.out.println();
	System.out.println("Emissões:");
	for(int i = 0; i<estados.size();i++){
		for(int j = 0; j<observ.size(); j++)
			System.out.print(emiss[i][j] + " ");
		System.out.println();
	}
	System.out.println();
}
}
