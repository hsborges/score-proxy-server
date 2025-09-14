package br.ufms.facom.proxy.utils;

public class FakeGenerator {

    // Gera um CPF fake válido (com dígitos verificadores corretos)
    public static String cpf() {
        java.util.Random rand = new java.util.Random();
        int[] n = new int[9];
        for (int i = 0; i < 9; i++) {
            n[i] = rand.nextInt(10);
        }
        // Calcula o primeiro dígito verificador
        int d1 = 0;
        for (int i = 0; i < 9; i++) {
            d1 += n[i] * (10 - i);
        }
        d1 = 11 - (d1 % 11);
        if (d1 >= 10) d1 = 0;

        // Calcula o segundo dígito verificador
        int d2 = 0;
        for (int i = 0; i < 9; i++) {
            d2 += n[i] * (11 - i);
        }
        d2 += d1 * 2;
        d2 = 11 - (d2 % 11);
        if (d2 >= 10) d2 = 0;

        StringBuilder cpf = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            cpf.append(n[i]);
        }
        cpf.append(d1).append(d2);
        return cpf.toString();
    }

}
