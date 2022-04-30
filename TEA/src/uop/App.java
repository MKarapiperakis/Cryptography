package uop;

import java.util.*;

public class App {

    private static class TEA 
    {
        // per-mutate input hexadecimal
        // according to specified sequence

        char[] x = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] s = {'6','4','c','5','0','7','2','e','1','f','3','d','8','a','9','b'};


        String xor(String a, String b)
        {
            
            // hexadecimal to decimal(base 10)
            long t_a = Long.parseUnsignedLong(a, 16);
            // hexadecimal to decimal(base 10)
            long t_b = Long.parseUnsignedLong(b, 16);
            // xor
            t_a = t_a ^ t_b;
            // decimal to hexadecimal
            a = Long.toHexString(t_a);
            // prepend 0's to maintain length
            while (a.length() < b.length())
                a = "0" + a;
            return a;
        }


        String permutation(int[] sequence, String input)
        {
            
            String output = "";
            
            input = hextoBin(input);
            
            for (int i = 0; i < sequence.length; i++)
            {
                output += input.charAt(sequence[i] - 1);
                
            }
            
            output = binToHex(output);
           
            
            return output;
        }

        // hexadecimal to binary conversion
        String hextoBin(String input)
        {
            int n = input.length() * 4;
            input = Long.toBinaryString(
                Long.parseUnsignedLong(input, 16));
            while (input.length() < n)
                input = "0" + input;
            return input;
        }
 
        // binary to hexadecimal conversion
        String binToHex(String input)
        {
            int n = (int)input.length() / 4;
            input = Long.toHexString(
                Long.parseUnsignedLong(input, 2));
            while (input.length() < n)
                input = "0" + input;
            return input;
        }

        //left shift according to the numBits
        String leftCircularShift(String input, int numBits)
        {            
            int n = input.length() * 4;
            int perm[] = new int[n];
            for (int i = 0; i < n - 1; i++)
                perm[i] = (i + 2);
            perm[n - 1] = 1;
            while (numBits-- > 0)
                input = permutation(perm, input);
            return input;
        }

        //we get our keys
        String[] getKeys(String key)
        {
            //we have 5 keys
            String keys[] = new String[5];

            //our first key
            keys[0] = key;  

            for (int i = 1; i < 5; i++) 
            {                
                //left shift <<2
                key = leftCircularShift(key,2);

                //convert to binary
                key = hextoBin(key);

                //swap bits except the last key
                if(i<4)
                    key = key.substring(0, 4)+ key.substring(8, 12) + key.substring(4,8) + key.substring(12, 16);
                
                //convert to Hex
                key = binToHex(key); 
                
                //we store our key
                keys[i] = key;
                
            }

              

            return keys;
        }

        //substitution
        String sBox(String plaintext)
        {
            
            
            String output = "";
            char temp ;
            for(int i=0;i<plaintext.length();i++)
            {
                temp = plaintext.charAt(i);
                for(int j=0;j<x.length;j++)
                {
                    if(x[j] == temp)                                
                        output = output + s[j];
                }
            }
            //System.out.println(output);
            return output;
        }

        String sBox2(String plaintext)
        {
            String output = "";
            char temp ;
            for(int i=0;i<plaintext.length();i++)
            {
                temp = plaintext.charAt(i);
                for(int j=0;j<s.length;j++)
                {
                    if(s[j] == temp)                                
                        output = output + x[j];
                }
            }
            //System.out.println(output);
            return output;
        }

        String encrypt(String plainText, String key)
        {
            // get round keys
            String keys[] = getKeys(key);   
            System.out.println("For key " + keys[0] + " subkeys are: ");
            for(int k=1;k<keys.length;k++)
                System.out.println(" k"+k+ " = " + keys[k]);
            
            for(int i=0;i<5;i++)
            {
               
                plainText = xor(plainText, keys[i]);
                
                
               
                if(i<4)            
                    plainText = sBox(plainText);
                 
                if(i<3)
                {
                    
                     plainText = hextoBin(plainText);
                     
                     plainText = plainText.substring(0, 1) + plainText.substring(4, 5) + plainText.substring(8, 9) + plainText.substring(12, 13)
                     + plainText.substring(1, 2) + plainText.substring(5,6) + plainText.substring(9,10) + plainText.substring(13,14)
                     + plainText.substring(2,3) + plainText.substring(6,7) + plainText.substring(10,11) + plainText.substring(14,15) 
                     + plainText.substring(3,4) + plainText.substring(7,8) + plainText.substring(11,12) + plainText.substring(15,16);

                     plainText = binToHex(plainText);
                     
                }
               
                System.out.println("round: " + (i+1) + " plaintext is " + plainText );
            }
            System.out.println();
            return plainText;
        }

        String decrypt(String plainText, String key)
        {
            String keys[] = getKeys(key);   
            
            
            for(int i=4;i>-1;i--)
            {

                if(i<3)
                {
                    
                     plainText = hextoBin(plainText);
                     
                     plainText = plainText.substring(0, 1) + plainText.substring(4, 5) + plainText.substring(8, 9) + plainText.substring(12, 13)
                     + plainText.substring(1, 2) + plainText.substring(5,6) + plainText.substring(9,10) + plainText.substring(13,14)
                     + plainText.substring(2,3) + plainText.substring(6,7) + plainText.substring(10,11) + plainText.substring(14,15) 
                     + plainText.substring(3,4) + plainText.substring(7,8) + plainText.substring(11,12) + plainText.substring(15,16);

                     plainText = binToHex(plainText);
                     
                }


                if(i<4)            
                    plainText = sBox2(plainText);
                    
                plainText = xor(plainText, keys[i]);
                
               
                System.out.println("round: " + (5-i) + " plaintext is " + plainText );
            }
            System.out.println();
            return plainText;
        }


    }
    public static void main(String[] args) throws Exception {

        String text = "1234";
        String key = "a1e9";
 
        TEA cipher = new TEA();
        System.out.print("******* Encryption *******\n");
        text = cipher.encrypt(text, key);

        System.out.println("ciphertext is: " + text + "\n");

        System.out.print("******* Decryption *******\n");
        text = cipher.decrypt(text,key);

        System.out.println("plaintext is: " + text + "\n");
        
    }
}
