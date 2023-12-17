

//funcao simples login
function logar() {
  //pega o campo usuario
  var pegaUsuario = document.getElementById('usuario').value;

  //pega o campo senha
  var pegaSenha = document.getElementById('senha').value;

  //envia os campos para teste de autenticação no backend por meio de queryParams 
  //(a funcao foi feita de forma que ambos email e nome sejam aceitos)
    fetch("http://localhost:6789/empresa/login?login=" + pegaUsuario + "&senha=" + pegaSenha)
    .then(response => {
      //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
      if(response.ok){
        //pega o header authorization da resposta (contem o token de autenticacao do usuario)
        const valorDoCabecalho = response.headers.get('Authorization');
        //salvar o token de autenticacao no session storage 
        //(a pratica comum é enviar antes do token a string: "Bearer ", o split separa os dois pelo espaço)
        //Exemplo de token: Bearer FK@$(_)KJ@)(M)FMJR)$J@M
        sessionStorage.setItem("UserLogado", valorDoCabecalho.split(" ")[1] );
        //retorna para a pag inicial, agora autenticado
        window.location.href = "./InsercaoProduto.html"
      }
      //em caso de erro avisa que o login ou a senha estão errados
       else if (response.status == 404){
        alert("ERRO: Login/Senha incorretos");
      } 
    })




  /*let validaLogin = false;
  // Obter os dados do localStorage
  var armazenadoItens = (JSON.parse(localStorage.getItem("login"))).contatos;
  // Verificar se os dados do usuário estão corretos
  for (let i = 0; i < armazenadoItens.length; i++) {
    if (pegaUsuario === armazenadoItens[i].nome && pegaSenha === armazenadoItens[i].senha) {
      validaLogin = true;
      var result = {
        "nome": pegaUsuario,
        "senha": pegaSenha
      }
      break;
    }
  }

  if (validaLogin == true) {
    alert("sucesso");
    localStorage.setItem("userLogado", JSON.stringify(result))
    location.href = 'index.html';
  } else {
    alert("Usuário ou senha incorreta");
  } */
}