
//Remove UserLogado do SessionStorage e atualiza a pagina
function deluserlogado() {
  sessionStorage.removeItem("UserLogado")
  location.reload()
}


//Header Authorizantion com o token do usuario (Forma recomendada de enviar tokens de autenticacao)
const headerslist = {
  "Authorization" : 'Bearer ' + sessionStorage.getItem("UserLogado")
}


//Funcao testa validade do token pelo backend
function enviaToken() {
  fetch("http://localhost:6789/usuario/token", {
    //header com o token
    headers: headerslist,
    //metodo do fetch
    method: "GET"
  })
    //promise
    .then(response => {
      //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
      if (response.ok) {
        return true;
      } 
      //caso o codigo de status nao tenha sido de sucesso, avisar o usuario, remover o token do sessionstorage e retornar false
      else {
        alert("Token inválido/expirado, faça login novamente");
        deluserlogado();
        return false;
      }
    })
}

//ativa a funcao enviatoken de forma mais eficiente
function isUserLogado() {
  //pega as informações de user logado
  var Userlogado = sessionStorage.getItem("UserLogado");
  //checa se tais informações realmente estão no sessionstorage
  if (Userlogado == null) {
    //caso nao, retorna false
    return false;
  } 
  //caso estejam, ativa a função enviatoken para testar validade de tais informações
  else {
    return enviaToken();
  }
}

//ativa sempre que a pagina terminar de carregar
$(document).ready(()=>{
    //caso user logado retornar false, envia o usuario de volta para o inicio
    if( isUserLogado() == false ){
        window.location.href = "index.html"
    } 
    //gera os icones para acessar informações da conta
    else{
        var result = '<div class="dropdown">'+
        '<a class="btn dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">'+
          '<i class="fa-regular fa-user"></i>'+
        '</a>'+
        '<ul class="dropdown-menu">'+
          '<li><a class="dropdown-item" href="calendario.html">Seu Calendario</a></li>'+
          '<li><a class="dropdown-item" href="SuaConta.html">Sua Conta</a></li>'+
          '<li><button class="dropdown-item" onclick="deluserlogado()">Sair</button></li>'+
        '</ul>'+
      '</div>'
      document.getElementById("barralogin").innerHTML+=result;
    }
    //inicia a função jardim
    Jardim()
})
/*
const dados = {
  plantas: [
      {   nome: "Orquidea", 
          nomecien: "Orchidaceae", 
          image: "https://cdn.awsli.com.br/600x450/2446/2446161/produto/188025820/1126e592a0.jpg",
          luz: "3h", 
          rega: "12h", 
          solo: "Poroso", 
          descricao: "Orquídeas são todas as plantas que compõem a família Orchidaceae, pertencente à ordem Asparagales, uma das maiores famílias de plantas existentes. " },

      {   nome: "Tulipa", 
          nomecien: "Tulipa", 
          image: "https://blog.cobasi.com.br/wp-content/uploads/2021/06/tulipa-rosa.webp",
          luz: "5h", 
          rega: "8h", 
          solo: "Vermiculita e Perlita", 
          descricao: "Tulipa L. é um género de plantas angiospermas (plantas com flores) da família das liláceas." },

      {   nome: "Rosa",
          image : "https://blog.cobasi.com.br/wp-content/uploads/2022/09/tipo-de-rosa-vermelha.webp", 
          nomecien: "Rosa", 
          luz: "12h", 
          rega: "4h", 
          solo: "Argiloso", 
          descricao: "A rosa (do latim rosa) é uma das flores mais populares no mundo. Vem sendo cultivada pelo homem desde a Antiguidade." }
  ]
};
*/
//por enquanto so placeholder, porem esta função devera conter todas as agendas de cuidados do usuario
async function getDados(){
  return await fetch("http://localhost:6789/agenda/calendario", {
    headers: headerslist,
    method: "GET",
    mode: "cors"
  }).then(response =>{
    if(response.ok){
      return response.json();
    } else{
      throw("Jardim Vazio");
    }
    
  }).then(data =>{
    return data;
  }).catch(erro =>{
    return null
  })
}
function delecaoAgenda(str){
  fetch("http://localhost:6789/agenda/delete/"+str, {
    headers: headerslist,
    method: "DELETE",
    mode: "cors"
  }).then(response =>{
    if(response.ok){
      alert("Item excluido com sucesso!");
      Jardim();
    } else{
      alert("ERRO ao excluir item");
      Jardim();
    }
  })
}


async function Jardim(){
  var data1 = ''
  var numero = 1;
  let dados = await  getDados();
  if(dados == null){
    document.getElementById("cardconta").innerHTML= '<div class="card-header text-center">'+
    '<a href="./index.html"><button class="btn btn-outline-success">Adicionar novas plantas</button></a>'+
  '</div>';
    document.getElementById("cardconta").innerHTML+="<h4 class='text-center p-5'>Jardim vazio :( </h4>";
  } else{
    let dataDisplay = dados.map((object) => {
      console.log(object);
      data1 += `
      <li class="list-group-item">
      <div class = " text-start col-12 g-2" >
                <div class = "col card h-100 p-3">
                <div class= "row">
                <div class="col-md-4">
                <img src="${object.imagemurl}" class="img-fluid rounded imgcard" alt="...">
                </div>
                <div class="col-md-6">
                <h1 class = "planta${numero}">${object.nomeplanta}</h1>
                <p class = "nomecientifico"><i> ${object.slug} </i></p>
                <button class="btn btn-outline-primary">Alterar calendario</button>
                <button class="btn btn-outline-danger" onclick="delecaoAgenda('${object.slug}')">Deletar</button>
                </div>
                </div>
                </div>
      </div>
      </li>
      `
      numero++;
    });
    var jardim = '  <div class="card-header text-center">'+
    '<a href="./index.html"><button class="btn btn-outline-success">Adicionar novas plantas</button></a>'+
  '</div>';
    jardim += '<ul class="list-group list-group-flush"> ' + data1 + '</ul>';
    document.getElementById("cardconta").innerHTML=jardim;
  }
  }


/*
function Calendarios(){
  var data1 = ''
  var numero = 1;
  let dataDisplay = dados.plantas.map((object) => {
    
    data1 += `
    <li class="list-group-item">
    <div class = " text-start col-12 g-2" >
              <div class = "col card h-100 p-3">
              <div class= "row">
              <div class="col-md-4">
              <img src="${object.image}" class="img-fluid rounded imgcard" alt="...">
              </div>
              <div class="col-md-6">
              <h1 class = "planta${numero}">${object.nome}</h1>
              <p class = "nomecientifico"><i> ${object.nomecien} </i></p>
              <p> Exposição a Luz diária: ${object.luz} | Intervalo de rega: ${object.rega} | Tipo de solo ideal: ${object.solo}</p>
              <p class = "descricao"> ${object.descricao} </p>
              <button class="btn btn-outline-success">Adicionar ao jardim</button>
              <button class="btn btn-outline-primary">Alterar calendario</button>
              <button class="btn btn-outline-danger">Deletar</button>
              </div>
              </div>
              </div>
    </div>
    </li>
    `
    numero++;
  });
  var calendarios = '<ul class="list-group list-group-flush"> ' + data1 + '</ul>';
  document.getElementById("cardconta").innerHTML=calendarios;
}
*/




//funcao de atualização da conta propriamente dita
function updateconta(){
  //pega os dados previamente digitados (funcao atualizaconta)
  let senha = document.getElementById("altsenha").value;
  let login = document.getElementById("altuser").value;
  let email = document.getElementById("altemail").value;
  let senhaatual = document.getElementById("suasenha").value;

  //realiza a operação fetch de update, esta possui a senha atual do usuario previamente digitada, novo email, novo login e nova senha
  //enviados como query params  (exemplo de query params: ?senha =simfa & email = mjsaida & &)
  fetch("http://localhost:6789/usuario/update?novasenha="+senha+"&email="+email+"&senha="+senhaatual+"&login="+login, {
    //envia o token de autenticação do usuario para verificação do login
    headers: headerslist,
    //metodo da funcao no spark
    method: "PUT"
  })
    //promise
    .then(response => {
      //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
      if (response.ok) {
        alert("Alterações realizadas com sucesso, faça login novamente para continuar");
        deluserlogado();
      } 
      //age de forma especifica quando o status é 401 ou 404, os status mudam conforme o tratamento de exceções no spark
      else if (response.status == 401) {
        alert("Token inválido/expirado, faça login novamente");
        sessionStorage.removeItem("UserLogado");
        return false;
      } 
      else if (response.status == 404){
        alert("senha invalida");
      }
    })

}

//função para deleção de conta propriamente dita

function deletaconta(){
  //pega os dados previamente digitados da senha atual do usuario (atualizaconta)
  let senhaatual = document.getElementById("suasenha").value;

  //fetch com query param contendo a senha atual do usuario
  fetch("http://localhost:6789/usuario/delete?senha="+senhaatual, {
    //header contendo o token para autenticação do usuario
    headers: headerslist,
    //metodo da função definido no spark
    method: "DELETE"
  })
    //promise
    .then(response => {
      //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
      if (response.ok) {
        alert("Conta exlcuida com sucesso");
        deluserlogado();
      }
      //age de forma especifica quando o status é 401 ou 404, os status mudam conforme o tratamento de exceções no spark 
      else if (response.status == 401) {
        alert("Token inválido/expirado, faça login novamente");
        sessionStorage.removeItem("UserLogado");
        return false;
      } else if (response.status == 404){
        alert("senha invalida");
      }
    })
}


var isAcesso = false;

//função de acesso, basicamente é a interface visual para update/delete da conta
function acesso(objeto){
  let senha = document.getElementById("suasenha").value;
  var acesso = `<ul class="list-group list-group-flush">
  <li class="list-group-item">
  <label>Alterar usuario</label>
  <input id="altuser" type="text" value="`+objeto.login+`" class="form-control"/>
  </li>
  <li class="list-group-item">
  <label>Alterar email</label>
  <input id="altemail" value="`+objeto.email+`" type="text" class="form-control"/>
  </li>
  <li class="list-group-item">
  <label>Alterar senha</label>
  <input id="altsenha" value="`+senha+`" type="text" class="form-control"/>
  </li>
  <li class="list-group-item text-end" >

    <div class="text-end">
    <button onclick="updateconta()" class="mt-2 btn btn-outline-primary"> Salvar </button>
    </div>
    <div class="text-end">
    <p> </p>
    </div>

  </li>

  <li class="list-group-item text-start" >

  <div class="text-end">
  <p> </p>
  </div>

  <div class="text-start">
  <button onclick="deletaconta()" class="mt-2 btn btn-outline-danger"> Deletar conta </button>
  </div>

</li>
 


  </ul>`;
  document.getElementById("cardconta").innerHTML=acesso;
  isAcesso = true;
}

//chama o popup que vai requisitar a senha atual do usuario
function logarAcesso(){
  document.getElementById("popup").style.display = "flex";
}

//esconde o popup que vai requisitar a senha atual do usuario 
function hidepopup(){
  document.getElementById("popup").style.display = "none";
}


//funcao do popup
function atualizaconta(){
  //senha atual do usuario digitada no campo do popup
  let senha = document.getElementById("suasenha").value

  //verificacao da senha do usuario com base no token dele
  fetch("http://localhost:6789/usuario/token/"+senha, {
    headers: headerslist,
    method: "GET"
  })
    .then(response => {
      if (response.ok) {
        //se o usuario for autenticado com sucesso retorna o json contendo as informações do usuario (enviado pelo spark)
        //esses dados serão adicionados como os values dos campos de atualização de conta e podem ser alterados para mudança de informações
        return response.json();
      } 
      //caso o token do usuario for invalido
      else if (response.status == 401) {
        alert("Token inválido/expirado, faça login novamente");
        deluserlogado();
        return false;
      //outros erros
      } else if (response.status == 404){
        alert("senha invalida");
      }
    })
    //promise enviada pelo response.json()
    .then( data =>{
      //esconde o popup
      hidepopup();
      //vai para a tela da funcao acesso, enviando os dados da conta do usuario recebidos pela response
      acesso(data);

    })
}
