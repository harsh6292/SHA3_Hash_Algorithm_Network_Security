/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package sha3_new;

/**
 *
 * @author HARSH
 */


import java.util.*;

public class SHA3 
{
    int capacity=512;
    int bitrate=1088;
    int count_of_blocks=0;
    StringBuilder[] P=new StringBuilder[200];
    StringBuilder[][][] substring=new StringBuilder[200][5][5];
    public String[][] S=new String[5][5];
    String[][] P_str=new String[5][5];
    int count=0;
    int x=0, y=0;
    int i=0;
    
    public static void main(String[] args) 
    {
        
        
        System.out.println("Enter a string to find its hash using SHA3 : ");
        String input="The quick brown fox jumps over the lazy dog";
        Scanner sc = new Scanner(System.in);
        input=sc.nextLine();
        //System.out.println(input);
        SHA3 obj = new SHA3();
        String output=obj.Keccak(input);
        System.out.println("Output in binary string : "+output);

        String[] str=new String[64];
        String hex_out="";
        int out_block=0,cnt=0,b=0;

        for (b = 0; b < output.length(); b+=4)
        {
            cnt=b+4;
            str[out_block]=new String();
            str[out_block]=output.substring(b, cnt);
            out_block+=1;
        }
        
        for (b = 0; b < out_block; b++)
        {
            
            hex_out+=Integer.toHexString(Integer.parseInt(str[b],2));
            
        }
        System.out.println("Output in hex : "+hex_out);
        
    }
    
    
    
    
    //===================================== K  E  C  C  A  K =================================================================
    
    
    
    public String Keccak(String input)
    {
       int exlen=0;
       int j=0;
       char[] cArray=input.toCharArray();

        StringBuilder sb=new StringBuilder();
        //System.out.println("input string : "+input);
        for(char c:cArray)
        {
            String cBinaryString=Integer.toBinaryString((int)c);
            //System.out.println("binary : "+cBinaryString);
            sb.append(cBinaryString);
        }
        
        
        if(sb.length()%1088!=0)
            exlen=sb.length()%1088;
        
        sb.append(1);
        
        for(j=1088;j>exlen+1;j--)
            sb.append(0);
       
        for(i=0;i<sb.length();i+=1088)
        {
            count=i+1088;
            String sub=sb.substring(i,count);
            P[count_of_blocks]=new StringBuilder();
            P[count_of_blocks].append(sub);
            count_of_blocks+=1;
                
        }
        
        StringBuilder sx=new StringBuilder();
        for(j=0;j<64;j++)
            sx.append(0);
        
        
        for(i=0;i<count_of_blocks;i++)
        {
            for(j=0;j<511;j++)
                P[i].append(0);
            P[i].append(1);
            //System.out.println("block "+i+" : "+P[i]+"\n len of block : "+P[i].length());
            int start=0;
            int end=64;
            for(x=0;x<=4;x++)
            {
                for(y=0;y<=4;y++)
                {
                    //System.out.println(start+"\t"+end);
                    substring[i][x][y]=new StringBuilder();
                    P_str[x][y]=new String();
                    substring[i][x][y].append(P[i].substring(start, end));
                    start=end;
                    end+=64;
                    //System.out.println("substring["+i+"]["+x+"]["+y+"] :"+substring[i][x][y]+"\n len of substring : "+substring[i][x][y].length());
                    P_str[x][y]=substring[i][x][y].toString();
                }
                    
            }
            
        }
        
        
                
        //System.out.println("initial s : "+sx+"\n  : "+sx.length());
        
        for(x=0;x<=4;x++)
        {
            for(y=0;y<=4;y++)
            {
               // obj.S[x][y]=new StringBuilder();
                S[x][y]=sx.toString();
                //System.out.println("S["+x+"]["+y+"] : "+S[x][y]);
            }
        }
        
        
        
        //Absorbing Phase
     
        for(i=0;i<count_of_blocks;i++)
        {
            for(x=0;x<5;x++)         //x+5y< r/w=1088/64=17
            {
                for(y=0;y<5;y++)
                {
                    
                    //System.out.println("S["+x+"]["+y+"] :"+S[x][y]);
                    //System.out.println("P["+x+"]["+y+"] :"+P_str[x][y]);
                    
                    S[x][y]=XORSTR(S[x][y],P_str[x][y]);
                    
                    //System.out.println("S["+x+"]["+y+"] :"+S[x][y]+"\n len : "+S.length);
                }
                
            }
            
            S=Keccak_f(S);      
        }

                
       //Squeezing Phase
       String Z=new String();
       
       for(x=0;x<=4;x++)
        {
            for(y=0;y<=4;y++)
            {
                //System.out.println("S["+x+"]["+y+"] : "+S[x][y]);
                Z+=S[x][y];
                S=Keccak_f(S);
                //System.out.println("S["+x+"]["+y+"] : "+S[x][y]);
            }
        }
        //System.out.println("AFTERRRRR performing SQUEEZING phase!!!!!!!!");
        //System.out.println("S["+x+"]["+y+"] : "+S[x][y]);
       //System.out.println("Z : "+Z+"\n len : "+Z.length());
        
       String MD=new String();
        MD=Z.substring(0,256);
        
        
              return MD;
    }
    
    
    
    
    //===================================== X  O  R =================================================================
    
    
    public char XOR(char a, char b)
    {
        int c=a^b;
       
        if(c==0)
            return '0'; //d='0';
        else
            return '1';//                    d='1';
    }
    
    
    
    //============================= X  O  R  --  S  T  R  I  N  G =================================================================
    
    public String XORSTR(String A, String B)
    {
        char[] A_char=A.toCharArray();
        char[] B_char=B.toCharArray();
        for(int j=0;j<64;j++)
                    {
                        char d=XOR(A_char[j],B_char[j]);
                        A_char[j]=d;
                    }
        String A_str=String.valueOf(A_char);
        return A_str;
    }
    
    
    //===================================== N  O  T =================================================================
    
    
    public char NOT(char a)
    {
        int b=~a;
       
        if(b==0)
            return '0'; //d='0';
        else
            return '1';//                    d='1';
    }
    
    
    
    //======================== N  O  T  --  S  T  R  I  N  G =================================================================
    
    
    public String NOTString(String A)
    {
        
        char[] A_char=A.toCharArray();
        for(int j=0;j<64;j++)
                    {
                        char d=NOT(A_char[j]);
                         A_char[j]=d;
                    }
        String A_str=String.valueOf(A_char);
        return A_str;
        
    }
    
    
    
    
    //===================================== A  N  D =================================================================
    
    
    public char AND(char a, char b)
    {
        int c=a&b;
       
        if(c==0)
            return '0'; //d='0';
        else
            return '1';//                    d='1';
    }
    
    
    
    
    //=========================== A  N  D --  S  T  R  I  N  G =================================================================
    
    
    public String ANDString(String A, String B)
    {
        char[] A_char=A.toCharArray();
        char[] B_char=B.toCharArray();
        for(int j=0;j<64;j++)
                    {
                        char d=AND(A_char[j],B_char[j]);
                         A_char[j]=d;
                    }
        String A_str=String.valueOf(A_char);
        return A_str;
    }
    
    
    
    
    //================================= R  O  T  A  T  E =================================================================
    
    
    public String Rotate(String A, int i)
    {
        int k=0;
        char[] A_char=A.toCharArray();
        char temp;
        for(int j=0;j<i;j++)
        {
            temp=A_char[0];
            for(k=1;k<64;k++)
            {
                
                A_char[k-1]=A_char[k];
                
            }
            A_char[k-1]=temp;
        }
        
        String A_str=String.valueOf(A_char);
        //System.out.println("inside rotate : "+A_str+"\n len :"+A_str.length());
        return A_str;
        
    }
    
    
    
    
    //===================================== K  E  C  C  A  K - F =================================================================
    
    
    public String[][] Keccak_f(String[][] A)
    {
        
        String RC[]=new String[24];
        for(int j=0;j<24;j++)
            RC[j]=new String();
        
        RC[0]="0000000000000000000000000000000000000000000000000000000000000001";
        RC[1]="0000000000000000000000000000000000000000000000001000000010000010";
        RC[2]="1000000000000000000000000000000000000000000000001000000010001010";
        RC[3]="1000000000000000000000000000000010000000000000001000000000000000";
        RC[4]="0000000000000000000000000000000000000000000000001000000010001011";
        RC[5]="1000000000000000000000000000000010000000000000000000000000000001";
        RC[6]="1000000000000000000000000000000010000000000000001000000010000001";
        RC[7]="1000000000000000000000000000000000000000000000001000000000001001";
        RC[8]="0000000000000000000000000000000000000000000000000000000010001010";
        RC[9]="0000000000000000000000000000000000000000000000000000000010001000";
        RC[10]="0000000000000000000000000000000010000000000000001000000000001001";
        RC[11]="0000000000000000000000000000000010000000000000000000000000001010";
        RC[12]="0000000000000000000000000000000010000000000000001000000010001011";
        RC[13]="1000000000000000000000000000000000000000000000000000000010001011";
        RC[14]="1000000000000000000000000000000000000000000000001000000010001001";
        RC[15]="1000000000000000000000000000000000000000000000001000000000000011";
        RC[16]="1000000000000000000000000000000000000000000000001000000000000010";
        RC[17]="1000000000000000000000000000000000000000000000000000000010000000";
        RC[18]="0000000000000000000000000000000000000000000000001000000000001010";
        RC[19]="1000000000000000000000000000000010000000000000000000000000001010";
        RC[20]="1000000000000000000000000000000010000000000000001000000010000001";
        RC[21]="1000000000000000000000000000000000000000000000001000000010000000";
        RC[22]="0000000000000000000000000000000010000000000000000000000000000001";
        RC[23]="1000000000000000000000000000000010000000000000001000000000001000";
        
        
        
         
        for(int i=0;i<24;i++)
        {
            //System.out.println("round : "+(i+1));
            A=Round(A,RC[i]);
        }
        return A;
    }
    
 
    
    
    
    //===================================== R  O  U  N  D =================================================================
    
    
    public String[][] Round(String A[][],String RC)
    {
        //Theta Step
        String[] C=new String[5];
        String[] D=new String[5];
        String[][] B=new String[5][5];
        int x=0,y=0;
        
        for(x=0;x<=4;x++)
        {
            String temp=new String();
            C[x]=new String();
            //System.out.println("inside for");
            //System.out.println("A["+x+"][0] : "+A[x][0]);
            //System.out.println("A["+x+"][1] : "+A[x][1]);
            //System.out.println("A["+x+"][2] : "+A[x][2]);
            //System.out.println("A["+x+"][3] : "+A[x][3]);
            //System.out.println("A["+x+"][4] : "+A[x][4]);
            temp=XORSTR(A[x][0],A[x][1]);
            //System.out.println("A["+x+"][1] : "+temp);
            temp=XORSTR(temp,A[x][2]);
            temp=XORSTR(temp,A[x][3]);
            C[x]=XORSTR(temp,A[x][4]);
            //System.out.println("C["+x+"] : "+C[x]);
        }
        
        for(x=0;x<=4;x++)
        {
            D[x]=new String();
            /*System.out.println("rott : "+Rotate(C[(x+1)%5],1));
            int r=5;
            System.out.println(((r+1)%5));
            System.out.println("C"+x+"-1 : "+C[(x-1)%5]);*/
            if(x==0)
                D[x]=XORSTR(C[4],Rotate(C[(x+1)%5],1));
            else
                D[x]=XORSTR(C[(x-1)%5],Rotate(C[(x+1)%5],1));   
            //System.out.println("D["+x+"] : "+D[x]);
        }
        
        
        int r[][]={
                    {0,36,3,41,18},
                    {1,44,10,45,2},
                    {62,6,43,15,61},
                    {28,55,25,21,56},
                    {27,20,39,8,14}
        };
        
        
        for(x=0;x<=4;x++)
        {
            for(y=0;y<=4;y++)
            {
                A[x][y]=XORSTR(A[x][y],D[x]);
                //System.out.println("A["+x+"]["+y+"] : "+A[x][y]);
            }
        }
        
        
        
        //RHO step
        //System.out.println("/n/n/n RHO STEP \n\n");
        for(x=0;x<=4;x++)
        {
            for(y=0;y<=4;y++)
            {
                B[x][y]=new String();
                B[x][y]=Rotate(A[x][y],r[x][y]);
                //System.out.println("B["+x+"]["+y+"] : "+B[x][y]);
            }
        }
        
        
        //PI step
        //System.out.println("/n/n/n PI STEP \n\n");
        
        for(x=0;x<=4;x++)
        {
            for(y=0;y<=4;y++)
            {
                B[y][(2*x+3*y)%5]=B[x][y];
            //    System.out.println("B["+y+"]["+x+"] : "+B[y][x]);
            }
        }
        
        
        //CHI Step
        
       // System.out.println("/n/n/n CHI STEP \n\n");
        for(x=0;x<=4;x++)
        {
            for(y=0;y<=4;y++)
            {
                String temp=new String();
                temp=NOTString(B[(x+1)%5][y]);
                A[x][y]=XORSTR(B[x][y],ANDString(temp,B[(x+2)%5][y]));
         //       System.out.println("A["+x+"]["+y+"] : "+A[x][y]);
            }
        }
        
        
        //IOTA step
        //System.out.println("/n/n/n IOTA STEP \n\n");
        A[0][0]=XORSTR(A[0][0],RC);
        //System.out.println("A[0][0] : "+A[0][0]);
        
        return A;
    }
}
