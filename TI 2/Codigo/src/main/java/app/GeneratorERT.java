// Esta classe é responsável por gerar os tokens de registro de empresas

package app;

import auth.ERegisterToken;

import java.util.Scanner;

import com.auth0.jwt.interfaces.DecodedJWT;

public class GeneratorERT {
	
	//chama objeto da classe scanner
	public static Scanner sc = new Scanner(System.in);
	
	//main
	public static void main(String[]args) {
		
		//RECEBE NOME DA EMPRESA
		System.out.println("Digite o nome da empresa: ");
		String nomeempresa =  sc.nextLine();
		System.out.println("Nome da empresa recebido: " + nomeempresa);
		
		//CODIFICANDO TOKEN E O RELACIONANDO A EMPRESA
		String token = ERegisterToken.TokenGenerator(nomeempresa);
		//Imprime o token propriamente dito
		System.out.println("Token: " + token);
		
		//TOKEN APOS A DECODIFICACAO 
		//Diego, use uma funcao de decodificacao pareceida com essa (salvando em uma string ao inves de printar)
		//para o cadastro (assim a empresa nao precisa digitar o nome dela ao cadastrar, so colocar o token e a senha)
		DecodedJWT result = ERegisterToken.verificaToken(token);
		System.out.println("Nome da empresa associada ao token: " + result.getSubject());
		
	}
}
