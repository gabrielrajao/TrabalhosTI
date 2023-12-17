function logar() {
  var pegaUsuario = document.getElementById('usuario').value;
  var pegaSenha = document.getElementById('senha').value;
  let validaLogin = false;
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
  }
}