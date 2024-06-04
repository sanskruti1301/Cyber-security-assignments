import java.util.ArrayList;
import java.util.Scanner;

public class SimplifiedAES {
	
	int [] key= {0,1,0,0,1,0,1,0,1,1,1,1,0,1,0,1};
	
	String [][] sbox= {
			{"0000","1001"},{"0001","0100"},{"0010","1010"},{"0011","1011"},{"0100","1101"},{"0101","0001"},{"0110","1000"},{"0111","0101"},
			{"1000","0110"},{"1001","0010"},{"1010","0000"},{"1011","0011"},{"1100","1100"},{"1101","1110"},{"1110","1111"},{"1111","0111"}
	};
	
	int [][]Me= {{1,4},{4,1}};
	int [][]invMe= {{9,2},{2,9}};
	
	int []key1=new int[16];
	int []key2=new int[16];
	int []key3=new int[16];
	
	
//	this function considers key matrix and generates all w0,w1,w2,w3,w4,w5 and by using that
//	it generate 3 final key like key-1,key-2,key3
	
	void keyGeneration(){
	
		int []W0=new int[8];
		int []W1=new int[8];
		int []W2=new int[8];
		int []W3=new int[8];
		int []W4=new int[8];
		int []W5=new int[8];
		
		int []exor1= {1,0,0,0,0,0,0,0};
		int []exor2= {0,0,1,1,0,0,0,0};     //we have 2 array for doing the xor 
		
		for(int i=0;i<8;i++)
		{
			W0[i]=key[i];
			W1[i]=key[i+8];
		}
		
		W2=function_(W0,W1,exor1);
		
		W3=exor(W2,W1);
		
		W4=function_(W2,W3,exor2);
		
		W5=exor(W4,W3);
		
		for(int i=0;i<8;i++)
		{
			key1[i]=W0[i];
			key1[i+8]=W1[i];
			key2[i]=W2[i];
			key2[i+8]=W3[i];
			key3[i]=W4[i];
			key3[i+8]=W5[i];
		}
		
		System.out.println("Your key-1 is :");
		printarray(key1);
		System.out.println();
		System.out.println("Your key-2 is :");
		printarray(key2);
		System.out.println();
		System.out.println("Your key-3 is :");
		printarray(key3);
	}
	
//	this function prints the elements of  array
	
	void printarray(int []ar)
	{
		for(int i=0;i<ar.length;i++)
		{
			System.out.print(ar[i]);
			if((i+1)%4==0)
				System.out.print(" ");
		}
		System.out.println();
	}
	
//	this function takes two array and does nibble rotation 
//	nibble substitution and xor with constant and returns single array
	
	int [] function_(int []X0,int []X1,int []exoraray)
	{
		int []nibrotate=rotateNib(X1,4);
		
		int []left4=new int[4];
		int []right4=new int[4];
		
		for(int i=0;i<4;i++)
		{
			left4[i]=nibrotate[i];
			right4[i]=nibrotate[i+4];
		}
		
		left4=subNib(left4,0);
		right4=subNib(right4,0);
		
		int []afterNibsubstitue=new int[8];
		for(int i=0;i<4;i++)
		{
			afterNibsubstitue[i]=left4[i];
			afterNibsubstitue[i+4]=right4[i];
		}
		
		int []temp=exor(afterNibsubstitue,exoraray);
		
		return exor(temp,X0);
	}

//	this functions rotates the nibble of 4
	
	int [] rotateNib(int []ar,int n)
	{
		int []l=new int[n];
		int []r=new int[n];
		int []output=new int[8];
		
		for(int i=0;i<n;i++)
		{
			l[i]=ar[i];
			r[i]=ar[i+n];
		}
		
		for(int i=0;i<n;i++)
		{
			output[i]=r[i];
			output[i+n]=l[i];
		}
		return output;
	}
	
//	this function does xor of two array 
//	and returns single array as output
	
	int [] exor(int[] x,int [] y)
	{
		int []output=new int[x.length];
		
		for(int i=0;i<x.length;i++)
		{
			output[i]=x[i]^y[i];
		}

		return output;
	}
	
//	this function takes array of  8 element and substitute the nibble of 4 using global 
//	sbox substitution matrix flag variable is basically for forward (flag=0)and reverse(flag=1) substitution
//	useful during both encryption and decryption
	
	int[] subNib(int [] ar,int flag)
	{
		StringBuilder str=new StringBuilder();
		String str1=new String();
		for(int i=0;i<4;i++)
		{
			str.append(ar[i]);
		}
		
		for(int i=0;i<16;i++)
		{
			if(str.toString().equals(sbox[i][flag-0]))
			{
			    str1=sbox[i][1-flag];
				break;
			}
		}
		
		int []output=new int[4];
		
		for(int i=0;i<4;i++)
		{
			output[i]=Character.getNumericValue(str1.charAt(i));
		}
		
		return output;
	}
	
//	this is the main function for encryption,takes plain text as input call another different
//	functions and generates cipher text
	
	int[] encryption_(int []plaintext)
	{
		int []ARK1=new int[16];
		  
		ARK1=exor(key1,plaintext);
		
		int []afterowshift=new int[16];
		
		afterowshift=subsNibleAndRowShift(ARK1);
		
		
		int []MixCol=matrixmultiplication(afterowshift,Me);
		
		int []ARK2=exor(MixCol,key2);
		
		afterowshift=subsNibleAndRowShift(ARK2);
		
		int []ciphertext=exor(afterowshift,key3);
		
		return ciphertext;
	}
	
//	this function takes array of 16 element and does nibble substitution along with row shift 
//	and returns the array of 16 element
	
	int [] subsNibleAndRowShift(int []ar)
	{
		int []output=new int[ar.length];
		
		int [] _1=new int[4];
		int [] _2=new int[4];
		int [] _3=new int[4];
		int [] _4=new int[4];
		
		for(int i=0;i<4;i++)
		{
			_1[i]=ar[i];
			_2[i]=ar[i+4];
			_3[i]=ar[i+8];
			_4[i]=ar[i+12];
		}
		
//		here nibble substitution is happening for 4 elements at a time
//		flag is 0 for forward substitution
		
		_1=subNib(_1,0);
		_2=subNib(_2,0);
		_3=subNib(_3,0);
		_4=subNib(_4,0);
		
		
		//here row shift is happening
		
		for(int i=0;i<4;i++)
		{
			output[i]=_1[i];
			output[i+4]=_4[i];  //<--
			output[i+8]=_3[i];
			output[i+12]=_2[i];  //<--
		}
		
		return output;
	}
	
//	this function takes array of 16 elements and one 2x2 matrix and does matrix multiplication
//	after all it returns the array of 16 elements

	
	int[] matrixmultiplication(int []ar,int [][]Me_)
	{
		int []output=new int[ar.length];
	
		int [] _1=new int[4];
		int [] _2=new int[4];
		int [] _3=new int[4];
		int [] _4=new int[4];
		
		for(int i=0;i<4;i++)
		{
			_1[i]=ar[i];
			_2[i]=ar[i+4];
			_3[i]=ar[i+8];
			_4[i]=ar[i+12];
		}
		
		int [] S00=new int[4];
		int [] S10=new int[4];
		int [] S01=new int[4];
		int [] S11=new int[4];
				
//		here we are doing matrix multiplication for each s00,s10,s01,s11
		
		S00=exor(multiplyReduceTo4(new int[]{1}, Me[0][0]), multiplyReduceTo4(new int[]{2}, Me[0][1]));
		S10=exor(multiplyReduceTo4(new int[]{1}, Me[1][0]), multiplyReduceTo4(new int[]{2}, Me[1][1]));
		S01=exor(multiplyReduceTo4(new int[]{3}, Me[0][0]), multiplyReduceTo4(new int[]{4}, Me[0][1]));
		S11=exor(multiplyReduceTo4(new int[]{3}, Me[1][0]), multiplyReduceTo4(new int[]{4}, Me[1][1]));

		
		for(int i=0;i<4;i++)
		{
			output[i]=S00[i];
			output[i+4]=S10[i];
			output[i+8]=S01[i];
			output[i+12]=S11[i];
		}
		
		return output;
	}
	
//	this function takes array of 4 elements and integer (integer then convert to binary)
//	after polynomial multiplication we are reducing it to only 4 bits 
//	and returns this 4 bits as array
	
	int [] multiplyReduceTo4(int []ar,int n)
	{
		String str=Integer.toBinaryString(n);
		char []arr=str.toCharArray();
		int []num =new int[arr.length];
		
		for(int i=0;i<arr.length;i++)
		{
			num[i]=Character.getNumericValue(arr[i]);
		}
		
	
		int []prod=new int[num.length+ar.length-1];
		
		for(int i=0;i<num.length+ar.length-1;i++)
		{
			prod[i]=0;
		}
		
		for(int i=0;i<num.length;i++)
		{
			for(int j=0;j<ar.length;j++)
			{
				prod[i+j]+=num[i]*ar[j];
			}
		}
		
		ArrayList<Integer> array=new ArrayList<Integer>();
	
		for(int i=0;i<prod.length;i++)
		{
			array.add(prod[i]%2==0?0:1);
		}
		
		int []reduce4= {1,0,0,1,1};	
		//this is polynomial irreducible equation for degree 4 
		
//		this while loop reduces the polynomial to only 4 bits using reduce4 array
		
		while(array.size()>4)
		{
			while(!array.isEmpty()&&array.get(0)==0)
			{
				array.remove(0);
			}
			
			if(array.size()<=4)
				break;
			
			for(int i=0;i<reduce4.length;i++)					
			{
				array.set(i, array.get(i)^reduce4[i]);
			}
			
			while(!array.isEmpty()&&array.get(0)==0)
			{
				array.remove(0);
			}
		}
		
		int []output=new int[4];
		for(int i=array.size()-1,j=3;i>=0;i--)
		{
			output[j--]=array.get(i);
		}
		
		return output;
	}
	
//	this is the main function for decryption
//	here we are taking cipher text as input and at the end prints decrypted text
//	here we have used all ready made functions that we have defined already
	
	void decryption(int []arr)
	{
		int []ARK3=exor(arr,key3);
		
		int []afterinvNibShift=inverseShiftNibSubs(ARK3);
		
		int []ARK2=exor(afterinvNibShift,key2);
		
		int []invMixCol=matrixmultiplication(ARK2,invMe);
		
		afterinvNibShift=inverseShiftNibSubs(invMixCol);
		
		int []ARK1=exor(afterinvNibShift,key1);
		
		System.out.println("Your Decrypted text is:");  
		printarray(ARK1);								//printing the decrypted text
	}

//	initially this function reverses the nibbles and then substitutes this nibble one by one
//	and the array of 16 elements 
	
	int []inverseShiftNibSubs(int []arr)
	{
		int []output=new int[16];
		

		int [] _1=new int[4];
		int [] _2=new int[4];
		int [] _3=new int[4];
		int [] _4=new int[4];
		
		for(int i=0;i<4;i++)
		{
			_1[i]=arr[i];
			_2[i]=arr[i+12];  //shift
			_3[i]=arr[i+8];
			_4[i]=arr[i+4];  //shift
		}
		
//		flag is 1 for reverse substitution
		
		_1=subNib(_1,1);
		_2=subNib(_2,1);
		_3=subNib(_3,1);
		_4=subNib(_4,1);
		
		
		for(int i=0;i<4;i++)
		{
			output[i]=_1[i];
			output[i+4]=_2[i];  
			output[i+8]=_3[i];
			output[i+12]=_4[i];  
		}
		
		return output;
	}
	
	
	public static void main(String[] args) {			
		SimplifiedAES obj=new SimplifiedAES();							
		Scanner sc = new Scanner(System.in);
		                            
		System.out.println("Enter the 16 bit plain Text:");
		int i;
		int [] plaintext= new int[16];
		for(i=0;i<16;i++)
		{
			plaintext[i]=sc.nextInt();

		}
		
		System.out.println();
		System.out.println("Your plain Text is:");
		obj.printarray(plaintext);	
		
		obj.keyGeneration();
		
		System.out.println();
		int []ciphertext=obj.encryption_(plaintext);    
		System.out.println("Your Cipher Text Is:");
		obj.printarray(ciphertext);						
		
		System.out.println();
		obj.decryption(ciphertext);						
	}

}