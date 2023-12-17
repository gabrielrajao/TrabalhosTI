/*
    OBS: Restrições de entrada devem ser tratadas no backend o frontend é mais vulnerável e não é seguro depender dele para isso

    Exemplo de restrição de entrada: maximo de 8 caracteres, string deve ser um email, etc

    O correto é receber as informações no backend e retornar um status de erro quando os argumentos são ilegais
    só fiquei com preguiça de fazer isso hoje :P


*/

//inputs, labels e validade das informações nos inputs
let campoNome = document.querySelector('#campoNome');
let labelNome = document.querySelector('#labelNome');
let validNome = true;

let campoEmail = document.querySelector('#campoEmail');
let labelEmail = document.querySelector('#labelEmail');
let validEmail = true;

let campoSenha = document.querySelector('#campoSenha');
let labelSenha = document.querySelector('#labelSenha');
let validSenha = false;
/*
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
*/

//Aplica restrições ao campo de Senha (Como dito no OBS, nao é o ideal)
campoSenha.addEventListener('keyup', () => {
  if (campoSenha.value.length < 8) {
    labelSenha.setAttribute('style', 'color: red');
    labelSenha.innerHTML = 'Insira no mínimo 8 caracteres';
    document.getElementById('campoSenha').style.boxShadow = "0px 3px 5px red";
    validSenha = false;
  } else {
    labelSenha.setAttribute('style', 'color: #09a946');
    labelSenha.innerHTML = 'Senha';
    document.getElementById('campoSenha').style.boxShadow = "0px 3px 5px #03782f";
    validSenha = true;
  }

})

//Aplica restrições ao campo de email (Como dito no OBS, nao é o ideal)
campoEmail.addEventListener('keyup', () => {
  if(!validEmail){
    validEmail = true;
    labelEmail.innerHTML = 'Email';
    labelEmail.setAttribute('style', 'color: #09a946');
    document.getElementById('campoEmail').style.boxShadow = "0px 3px 5px #03782f";
  }
})

//Aplica restrições ao campo de nome/login (Como dito no OBS, nao é o ideal)
campoNome.addEventListener('keyup', () => {
  if(!validNome){
    validNome = true;
    labelNome.innerHTML = 'Nome';
    labelNome.setAttribute('style', 'color: #09a946');
    document.getElementById('campoNome').style.boxShadow = "0px 3px 5px #03782f";
  }
})

/*



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
 */

//funcao para o post propriamente dito
function enviaCadastro() {
  //pega senha do input
  let senha = document.getElementById('campoSenha').value;
  //pega email do input
  let email = document.getElementById('campoEmail').value;
  //pega login do input
  let login = document.getElementById('campoNome').value;

  //checa se são validos (nao é o ideal)
  if (validEmail && validNome && validSenha) {
    //envia as informações digitadas para o backend por meio de queryparams
    fetch("http://localhost:6789/empresa/insert?senha=" + senha + "&email=" + email + "&login=" + login, {
      //metodo definido no spark
      method: "POST",
      //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
      mode: "cors"
    })
      //promise
      .then(response => {
        //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
        if (response.ok) {
          //envia o empresa para a pagina de login para que ele possa logar (o melhor era fazer o login automatico, mas preguica)
          window.location.href = "./LoginEmpresa.html"
        } 
        //erro 409 sinaliza conflito (ou seja, existe outra pessoa com esse login ou email)
        else if (response.status == 409) {
          //pegar o body de resposta para tratamento correto do erro
          return response.text();
        } 
        //erro 500 sinaliza um erro interno, nao da parte de empresa
        else if (response.status == 500) {
          alert("ERRO no servidor");
        }
      })
      //promise para tratar erro do empresa
      .then(data => {
        //pega no body da response a parte que sinaliza se o problema é com o email, login ou ambos
        let erro = data.split(" ")[1];
        //se for com email, marcar input e label do email
        if (erro === "EMAIL") {
          labelEmail.setAttribute('style', 'color: red');
          labelEmail.innerHTML = 'Email já em uso';
          document.getElementById('campoEmail').style.boxShadow = "0px 3px 5px red";
          validEmail = false;
        }
        //se for com o login, marcar input e label do login
        else if (erro === "LOGIN") {
          labelNome.setAttribute('style', 'color: red');
          labelNome.innerHTML = 'Usuário já em uso';
          validNome = false;
          document.getElementById('campoNome').style.boxShadow = "0px 3px 5px red";
        } 
        //caso seja com ambos, marcar ambos
        else{
          labelNome.setAttribute('style', 'color: red');
          labelNome.innerHTML = 'Usuário já em uso';
          validNome = false;
          document.getElementById('campoNome').style.boxShadow = "0px 3px 5px red";
          labelEmail.setAttribute('style', 'color: red');
          labelEmail.innerHTML = 'Email já em uso';
          document.getElementById('campoEmail').style.boxShadow = "0px 3px 5px red";
          validEmail = false;
        }
      })
  } 
  //enquanto o empresa nao alterar os campos vermelhos, impedir envio de informações
  else {
    alert("Corrija os erros em vermelho por favor!");
  }
}


// Configura os botões

document.getElementById('btnCadastro').addEventListener('click', enviaCadastro);

