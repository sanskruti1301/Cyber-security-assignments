#include <math.h>
#include <stdio.h>

// a ^ b mod P
long long int power(long long int a, long long int b,
                    long long int P)
{
    if (b == 1)

        return a;

    else
        return (((long long int)pow(a, b)) % P);
}


int main()
{
    long long int P, G, x, a, y, b, ka, kb;


    P = 23;
    printf("The value of P : %lld\n", P);

    G = 9;
    printf("The value of G : %lld\n\n", G);


    a = 4; // private key alice
    printf("Alice private key : %lld\n", a);
    x = power(G, a, P); // gets the generated key


    b = 3; // private key bob
    printf("Bob private key : %lld\n\n", b);
    y = power(G, b, P); // generated key

    //secret keys after exchange
    ka = power(y, a, P); // Alice
    kb = power(x, b, P); // Bob

    printf("Alice secret key : %lld\n", ka);
    printf("Bob secret key: %lld\n", kb);

    return 0;
}
