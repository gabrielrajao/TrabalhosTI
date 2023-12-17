let campoNome = document.querySelector('#campoNome');
let labelNome = document.querySelector('#labelNome');
let validNome = false;

let campoEmail = document.querySelector('#campoEmail');
let labelEmail = document.querySelector('#labelEmail');
let validEmail = false;

let campoSenha = document.querySelector('#campoSenha');
let labelSenha = document.querySelector('#labelSenha');
let validSenha = false;

let cadastro = localStorage.getItem("login");

campoNome.addEventListener('keyup', () => {
  if (campoNome.value.length <= 2) {
    labelNome.setAttribute('style', 'color: red');
    labelNome.innerHTML = 'Nome *Insira no mínimo 3 caracteres';
    validNome = false;
  } else {
    labelNome.setAttribute('style', 'color: white');
    labelNome.innerHTML = 'Nome';

    let objDados = leDados();
    let contatos = objDados.contatos;
    let nome = campoNome.value;
    let nomeEmUso = contatos.some(contato => contato.nome === nome);

    if (nomeEmUso) {
      labelNome.setAttribute('style', 'color: red');
      labelNome.innerHTML = 'Nome *Usuário já em uso';
      validNome = false;
    } else {
      validNome = true;
      labelNome.innerHTML = 'Nome';
    }
  }
})


campoEmail.addEventListener('keyup', () => {
  // Verifica se o email possui um formato válido
  let emailFormatoValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(campoEmail.value);
  if (!emailFormatoValido) {
    labelEmail.setAttribute('style', 'color: red');
    labelEmail.innerHTML = 'Email *Insira um email válido';
    validEmail = false;
    return;
  }

  // Verifica se o email já está em uso
  let objDados = leDados();
  let contatos = objDados.contatos;
  let email = campoEmail.value;
  let emailEmUso = contatos.some(contato => contato.email === email);

  if (emailEmUso) {
    labelEmail.setAttribute('style', 'color: red');
    labelEmail.innerHTML = 'Email já em uso';
    validEmail = false;
  } else {
    validEmail = true;
    labelEmail.setAttribute('style', 'color: white');
    labelEmail.innerHTML = 'E-mail';
  }
});

campoSenha.addEventListener('keyup', () => {
  if (campoSenha.value.length < 6) {
    labelSenha.setAttribute('style', 'color: red');
    labelSenha.innerHTML = 'Senha *Insira no mínimo 3 caracteres';
    validSenha = false;
  } else {
    labelSenha.setAttribute('style', 'color: white');
    labelSenha.innerHTML = 'Senha';
    validSenha = true;
  }

})






function leDados() {
  let strDados = localStorage.getItem('login');
  let objDados = {};

  if (strDados) {
    objDados = JSON.parse(strDados);
  }
  else {
    objDados = {
      contatos: [
        { nome: "João da Silva", email: "joao@gmail.com", senha: "123" },
        { nome: "Maria das Graças", email: "maria@gmail.com", senha: "123" },
        { nome: "Pedro Gomes", email: "pedro@gmail.com", senha: "123" }
      ]
    }
  }

  return objDados;
}

function salvaDados(dados) {
  localStorage.setItem('login', JSON.stringify(dados));
}

function incluirContato() {
  // Verifica se o nome de usuário já existe
  let objDados = leDados();
  let strNome = document.getElementById('campoNome').value;
  let cadastroExistente = objDados.contatos.find(contato => contato.nome === strNome);

  if (cadastroExistente) {
    alert("Nome de usuário já existe");
    return;
  }

  // Verifica se o email já está em uso
  let strEmail = document.getElementById('campoEmail').value;
  let emailExistente = objDados.contatos.find(contato => contato.email === strEmail);

  if (emailExistente) {
    alert("Email já está em uso");
    return;
  }


  if (!emailExistente && !cadastroExistente) {
    alert("Sucesso");
  }

  // Cria um novo objeto contato com os dados preenchidos no formulário
  let objContato = {
    nome: strNome,
    email: strEmail,
    senha: document.getElementById('campoSenha').value
  };

  // Adiciona o novo objeto contato ao array de contatos
  objDados.contatos.push(objContato);

  // Salva os dados atualizados no localStorage
  salvaDados(objDados);

  // Redireciona para a página de login
  window.location.href = "Login.html";
}



// Configura os botões

document.getElementById('btnCadastro').addEventListener('click', incluirContato);

