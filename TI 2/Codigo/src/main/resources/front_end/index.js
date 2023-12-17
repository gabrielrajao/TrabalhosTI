// Recupera os dados salvos no Local Storage
//var plantas = localStorage.getItem("objDados");


//insere informações placeholder (por enquanto) no card
function display(data) {

  /*const dados = {
    plantas: [
      {
        nome: "Orquidea",
        nomecien: "Orchidaceae",
        image: "https://cdn.awsli.com.br/600x450/2446/2446161/produto/188025820/1126e592a0.jpg",
        luz: "3h",
        rega: "12h",
        solo: "Poroso",
        descricao: "Orquídeas são todas as plantas que compõem a família Orchidaceae, pertencente à ordem Asparagales, uma das maiores famílias de plantas existentes. "
      },

      {
        nome: "Tulipa",
        nomecien: "Tulipa",
        image: "https://blog.cobasi.com.br/wp-content/uploads/2021/06/tulipa-rosa.webp",
        luz: "5h",
        rega: "8h",
        solo: "Vermiculita e Perlita",
        descricao: "Tulipa L. é um género de plantas angiospermas (plantas com flores) da família das liláceas."
      },

      {
        nome: "Rosa",
        image: "https://blog.cobasi.com.br/wp-content/uploads/2022/09/tipo-de-rosa-vermelha.webp",
        nomecien: "Rosa",
        luz: "12h",
        rega: "4h",
        solo: "Argiloso",
        descricao: "A rosa (do latim rosa) é uma das flores mais populares no mundo. Vem sendo cultivada pelo homem desde a Antiguidade."
      }
    ]
  }; */

  var data1 = "";
  var data2 = "";
  var numero = 1;
  if (data == "") {
    data1 = '<div class="col-12 "> Sentimos muito, nenhuma planta foi encontrada </div>'
  } else {
    let plantas = data.data;
    let paginas = data.pages;
    data2+=`<div class = " text-center p-5 col-12 g-2" > `
    if(paginas.last != undefined){
      data2 += '<button type="button" class="btn" onclick="carregaPagina('+paginas.last+')" > < </button>';
    }
    data2 += '<button type="button" class="btn" disabled>'+(paginas.current + 1)+' of '+(paginas.total + 1)+' </button>'
    if(paginas.next != undefined){
      data2 += '<button type="button" class="btn" onclick="carregaPagina('+paginas.next+')" > > </button>';
    }
    data2 += "</div>"
    plantas.forEach(element => {
      data1 += `
      <div class = " text-start col-12 g-2" >
                <div class = "col card h-100 p-3">
                <div class= "row">
                <div class="col-md-4">
                <img src="${element.image_url}" class="img-fluid rounded imgcard" alt="...">
                </div>
                <div class="col-md-6">
                <h1 class = "planta${numero}">${element.nomepop}</h1>
                <p class = "nomecientifico"><i> ${element.nomecien} </i></p>
                <div id='AdicionaJardim${numero}'>
                <button class="btn btn-outline-success"  onClick="AdicionarAoJardim('${element.plantId}')">Adicionar ao Jardim</button>
                </div>
                </div>
                </div>
                </div>
      </div>`
      numero++;
    });
    /*data.array.forEach(element => {
      
    });((object) => {

      data1 += `
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
                <div id='AdicionaJardim${numero}'>
                <button class="btn btn-outline-success"  onClick="AdicionarAoJardim(${numero})">Adicionar ao Jardim</button>
                </div>
                </div>
                </div>
                </div>
      </div>
      `
      numero++;
    }); */
  }

  data1 += data2;
      
  document.getElementById("cards").innerHTML = data1;

}

function searchPagina(numero, nome){
  fetch("http://localhost:6789/planta/get/"+nome+"?page=" + numero)
  .then(response =>{
    return response.json()
  }).then(data =>{
    if(data.data == null){
      document.getElementById("cards").innerHTML = "<h2 class='text-center'> 404: Nenhuma planta encontrada :(</h2>"
    }
    else if(data.pages == null){
      document.getElementById("cards").innerHTML = "<h2 class='text-center'> 400: Número de página inválida :(</h2>"
    }
    else{
      display2(data,nome);
    }
  })
}

function display2(data, nome) {

  /*const dados = {
    plantas: [
      {
        nome: "Orquidea",
        nomecien: "Orchidaceae",
        image: "https://cdn.awsli.com.br/600x450/2446/2446161/produto/188025820/1126e592a0.jpg",
        luz: "3h",
        rega: "12h",
        solo: "Poroso",
        descricao: "Orquídeas são todas as plantas que compõem a família Orchidaceae, pertencente à ordem Asparagales, uma das maiores famílias de plantas existentes. "
      },

      {
        nome: "Tulipa",
        nomecien: "Tulipa",
        image: "https://blog.cobasi.com.br/wp-content/uploads/2021/06/tulipa-rosa.webp",
        luz: "5h",
        rega: "8h",
        solo: "Vermiculita e Perlita",
        descricao: "Tulipa L. é um género de plantas angiospermas (plantas com flores) da família das liláceas."
      },

      {
        nome: "Rosa",
        image: "https://blog.cobasi.com.br/wp-content/uploads/2022/09/tipo-de-rosa-vermelha.webp",
        nomecien: "Rosa",
        luz: "12h",
        rega: "4h",
        solo: "Argiloso",
        descricao: "A rosa (do latim rosa) é uma das flores mais populares no mundo. Vem sendo cultivada pelo homem desde a Antiguidade."
      }
    ]
  }; */

  var data1 = "";
  var data2 = "";
  var numero = 1;
  if (data == "") {
    data1 = '<div class="col-12 "> Sentimos muito, nenhuma planta foi encontrada </div>'
  } else {
    let plantas = data.data;
    let paginas = data.pages;
    data2+=`<div class = " text-center p-5 col-12 g-2" > `
    if(paginas.last != undefined){
      data2 += '<button type="button" class="btn" onclick="searchPagina('+paginas.last+',`'+nome+'`)" > < </button>';
    }
    data2 += '<button type="button" class="btn" disabled>'+(paginas.current + 1)+' of '+(paginas.total + 1)+' </button>'
    if(paginas.next != undefined){
      data2 += '<button type="button" class="btn" onclick="searchPagina('+paginas.next+',`'+nome+'`)" > > </button>';
    }
    data2 += "</div>"
    plantas.forEach(element => {
      data1 += `
      <div class = " text-start col-12 g-2" >
                <div class = "col card h-100 p-3">
                <div class= "row">
                <div class="col-md-4">
                <img src="${element.image_url}" class="img-fluid rounded imgcard" alt="...">
                </div>
                <div class="col-md-6">
                <h1 class = "planta${numero}">${element.nomepop}</h1>
                <p class = "nomecientifico"><i> ${element.nomecien} </i></p>
                <div id='AdicionaJardim${numero}'>
                <button class="btn btn-outline-success"  onClick="AdicionarAoJardim('${element.plantId}')">Adicionar ao Jardim</button>
                </div>
                </div>
                </div>
                </div>
      </div>`
      numero++;
    });
    /*data.array.forEach(element => {
      
    });((object) => {

      data1 += `
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
                <div id='AdicionaJardim${numero}'>
                <button class="btn btn-outline-success"  onClick="AdicionarAoJardim(${numero})">Adicionar ao Jardim</button>
                </div>
                </div>
                </div>
                </div>
      </div>
      `
      numero++;
    }); */
  }

  data1 += data2;
      
  document.getElementById("cards").innerHTML = data1;

}




/* PESQUISAR */
const search = () => {
  var contagem = 0;
  /* BUSCAR TEXTO INSERIDO E ATRIBUTOS CARDS */
  //document.getElementById("texto-inicial").style.display = "none";
  const searchbox = document.getElementById("search-item").value.toUpperCase();
  if(searchbox == ""){
    carregaPagina(0);
  } else{
  searchPagina(0, searchbox);
  }

}


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
async function enviaToken() {
  let result = await fetch("http://localhost:6789/usuario/token", {
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
    return result;
    
    
}

//ativa a funcao enviatoken de forma mais eficiente
async function isUserLogado() {

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


//placeholder dnv, por enquanto
async function AdicionarAoJardim(plantId) {
  var m = await isUserLogado() ;
  if(m == true){
    window.location.href = "./adicionaraoJardim.html?planta="+plantId;
  } else{
    alert("ERRO: Não foi possível realizar a validação do usuário, favor logar novamente");
    window.location.href = "./Login.html";
  }


  /*var result = '';
  var logado = isUserLogado();
  var paginaDeDestino = 'login.html';
  if (logado == false) {
    window.location.href = paginaDeDestino;
  } else {
    result = '<a href="calendario.html" class=" m-2 btn btn-outline-success" ">Calendário recomendado</a><a href="cadastrocalendario.html" class=" m-2 btn btn-outline-success">Formulário calendario</a>';
    document.getElementById(`AdicionaJardim${numero}`).innerHTML = result; 
  } */
}

function carregaPagina(pagina){
  fetch("http://localhost:6789/planta/get?page=" + pagina)
  .then(response =>{
    return response.json()
  }).then(data =>{
    if(data.data == null){
      document.getElementById("cards").innerHTML = "<h2 class='text-center'> 404: Nenhuma planta encontrada :(</h2>"
    }
    else if(data.pages == null){
      document.getElementById("cards").innerHTML = "<h2 class='text-center'> 400: Número de página inválida :(</h2>"
    }else{
      display(data);
    }
  })
}

async function Logador(){
  logado = await isUserLogado();
  var result = ""
  //caso user logado retornar false, substitui os icones de informações de conta por botoes de login e registro
  if (logado == false) {
    result = '<a href="Login.html"  ><button class="btn btn-light " >Entrar</button></a><br>' +
      '<p id="registro" style="font-size: 10pt">Não possui uma conta? <a href="cadastro.html">Clique Aqui</a> </p>'
  }
  //se estiver logado, insere os icones de informações de conta
  else {
    result = '<div class="dropdown">' +
      '<a class="btn dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">' +
      '<i class="fa-regular fa-user"></i>' +
      '</a>' +
      '<ul class="dropdown-menu">' +
      '<li><a class="dropdown-item" href="calendario.html">Seu Calendario</a></li>' +
      '<li><a class="dropdown-item" href="SuaConta.html">Sua Conta</a></li>' +
      '<li><a class="dropdown-item" href="avaliacaoProd.html">Avaliação de produtos</a></li>' +
      '<li><button class="dropdown-item" onclick="deluserlogado()">Sair</button></li>' +
      '</ul>' +
      '</div>'
  }
  document.getElementById("barralogin").innerHTML = result;
}

//ativa sempre que a pagina terminar de carregar
$(document).ready(() => {
  carregaPagina(0);
  Logador();
 // display();
})



function ativaFoto(){
  document.getElementById("indexpop").style.display="flex";
}

function hidepopup(){
  document.getElementById("indexpop").style.display="none";

}




function procuraPlanta(){
  if(document.getElementById("fotoplanta").files.length == 0){
    alert("ERRO: Nenhuma Imagem adicionada");
  }
  else if(document.getElementById("fotoplanta").files[0].type != "image/jpeg" && document.getElementById("fotoplanta").files[0].type != "image/png"){
    alert("ERRO: Formato Inválido");
    document.getElementById("fotoplanta").value = "";
  }
  else{
   
    
    let formData = new FormData();
    formData.append('images', document.getElementById("fotoplanta").files[0]);
    formData.append('organs', 'auto');
    fetch("https://my-api.plantnet.org/v2/identify/all?include-related-images=false&no-reject=false&lang=pt&api-key=2b10JXPZk7d4f44fkIMygXRuxO", {
      method: "POST",
      body: formData,
  
    }).then(response =>{
      return (response.json());
    }).then(data =>{
  
        var resultados = "";
        for(let i = 0; i < data.results.length; i++){
          let temp = data.results[i].species.scientificNameWithoutAuthor;
          temp = temp.toLowerCase();
          temp = temp.replaceAll(" ", "-")
          resultados+=temp;
          if(i>0){
            resultados+=",";
          }
        }
        PesquisaBD(resultados);
    }).catch(err =>{
      console.log(err)
    })
  }

}

function PesquisaBD(Slugs){
  fetch("http://localhost:6789/planta/searchres?results="+Slugs).then(response=>{
    if(response.ok){
      return response.json();
    }
    else{
      
      throw "ERRO: PLANTA NAO ENCONTRADA";
    }
  }).then(element =>{
    var final = `<div class = " text-start col-12 g-2" >
          <div class = "col card h-100 p-3">
          <div class= "row">
          <div class="col-md-4">
          <img src="${element.image_url}" class="img-fluid rounded imgcard" alt="...">
          </div>
          <div class="col-md-6">
          <h1 class = "plantaExtra">${element.nomepop}</h1>
          <p class = "nomecientifico"><i> ${element.nomecien} </i></p>
          <div id='AdicionaJardimExtra'>
          <button class="btn btn-outline-success"  onClick="AdicionarAoJardim('${element.plantId}')">Adicionar ao Jardim</button>
          </div>
          </div>
          </div>
          </div>
      </div>`
    document.getElementById("finalpop").innerHTML = final;
  }).catch(err=>{
    document.getElementById("finalpop").innerHTML = '';
      alert(err);
  })
}
















//insere informações placeholder (por enquanto) no card
function displayProd(dataProd) {
  var dataProd = "";
  var data2 = "";
  var numero = 1;
  if (dataProd == "") {
    dataProd = '<div class="col-12 "> Sentimos muito, nenhum produto foi encontrada </div>'
  } else {
    let products = dataProd.data;
    data2+=`<div class = " text-center p-5 col-12 g-2" > `
    data2 += "</div>"
    products.forEach(element => {
      dataProd += `
      <div class = " text-start col-12 g-2" >
                <div class = "col card h-100 p-3">
                <div class= "row">
                <div class="col-md-4">
                <div class="col-md-6">
                <h1 class = "produto${numero}">${element.nome}</h1>
                <p class = "funcao"><i> ${element.funcao} </i></p>
                <a href="${produrl}">funcção = ${produrl}</a>
                </div>
                </div>
                </div>
                </div>
      </div>`
      numero ++;
    });
  }
  dataProd += data2;
      
  document.getElementById("products").innerHTML = dataProd;

}



function carregaProd(){
  fetch("http://localhost:6789/recomendado/get")
  .then(response =>{
    return response.json()
  }).then(data =>{
    if(data.data == null){
      document.getElementById("cards").innerHTML = "<h2 class='text-center'> 404: Nenhum produto encontrado :(</h2>"
    }else{
      displayProd(data);
    }
  })
}


