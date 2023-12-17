function carregaProd(){
    fetch("http://localhost:6789/rating/recomendar",{
    headers: headerslist,
        //metodo definido no spark
        method: "GET",
        //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
        mode: "cors"
    })

    .then(response =>{
      return response.json()
    }).then(data =>{
      showProdutos(data)
    })
  }
  
  async function  showProdutos(prods){
    var temp = prods.Results.output1;
    var produtos =  []
    for(i = 1; i < 6; i++){
       var index = temp[0];
       await fetch( "http://localhost:6789/produto/get/"+index["Recommended Item "+i]).then(response=>{
        return response.json();
       }).then(data=>{
        produtos.push(data);
       })
        
    }
    recomendacaoProd(produtos)
  }

  function recomendacaoProd(produtos){
    var data1 = '<div class="row text-center text-success "><h1>Produtos Recomendados</h1> </div>'

    console.log(produtos);
    for(i = 0; i < produtos.length; i++){
      var element = produtos[i];
      data1 += `
      <div class = " text-start col-12 g-2 mt-5" >
                <div class = "col card h-100 p-3">
                <div class= "row">
                <div class="col-md-6">
                <h1 class = "prod">${element.nome}</h1>
                <p class = "funcao"><i> ${element.descricao} </i></p>
                <p class = "preco"><i> R$${element.preco} </i></p>
                <button class="btn btn-outline-success" onclick="CompraProduto( '${element.prodid}' , '${element.produrl}' )">Comprar Produto</button>
                </div>
                </div>
                </div>
      </div>`
    }

    document.getElementById("cards").innerHTML = data1;
  }

  async function CompraProduto(idprod, urlprod){
    await fetch("http://localhost:6789/rating/insert/"+idprod, {
      headers: headerslist,
      //metodo definido no spark
      method: "POST",
      //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
      mode: "cors"
    }).then(response=>{
      if(response.ok){
        alert("Produto adicionado com sucesso!");
      }
    })

    window.location.href=urlprod;
  }


  function display(products) {
    var data1 = "";
    var numero = 1;
    console.log(products)
    if (products.length == 0) {
      data1 = '<div class="col-12 "> Sentimos muito, nenhum produto foi encontrado </div>'
    } else {
      products.forEach(element => {
        data1 += `
        <div class = " text-start col-12 g-2" >
                  <div class = "col card h-100 p-3">
                  <div class= "row">
                  <div class="col-md-6">
                  <h1 class = "prod${numero}">${element.nome}</h1>
                  <p class = "funcao"><i> ${element.funcao} </i></p>
                  <p class = "preco"><i> ${element.preco} </i></p>
                  <div id='AdicionaJardim${numero}'>
                  <label for="customRange2" class="form-label">Example range</label>
                  <input type="range" class="form-range" min="0" max="5" id="customRange2">
                  <button class="btn btn-outline-success"  onClick="definirRating()">Avaliar Produto</button>
                  </div>
                  </div>
                  </div>
                  </div>
        </div>`
        numero++;
      });
    }
    document.getElementById("products").innerHTML = data1;
  }


function definirRating() {
    // Obter o elemento de input do tipo range
    var rangeInput = document.getElementById("customRange2");

    // Obter o valor atual do input range
    var valorAvaliacao = rangeInput.value;

    enviaRating(valorAvaliacao);
}



function enviaRating(valorAvaliacao){
  fetch("http://localhost:6789/recomendado/update?rating="+ valorAvaliacao)
  .then(response =>{
    return response.json()
  })
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

  function add(ths, rap, sno) {
    for (var i = 1; i <= 5; i++) {
      var cur = document.getElementById("star"+rap + i)
      cur.className = "far fa-star"
    }
    for (var i = 1; i <= sno; i++) {
      var cur = document.getElementById("star"+rap + i)
      if (cur.className == "far fa-star") {
        cur.className = "fas fa-star"
      }
    }
  }
  
  function mostraRatings(products){
    var data1 = "";
    var numero = 0;
    console.log(products)
    if (products.length == 0) {
      data1 = '<div class="col-12 "> Sentimos muito, nenhum produto foi encontrado </div>'
    } else {
      products.forEach(element => {
  
        data1 += `
        <div class = " text-start col-12 g-2" >
                  <div class = "col card h-100 p-3">
                  <div class= "row">
                  <div class="col-md-6">
                  <h1 class = "prod${numero}">${element.nomeProd}</h1>
        `
        if(element.rating == -1){
          data1 += `<h3 class="text-danger">Não avaliado!</h3>`
          data1 += `<div class="form-group">
          <label for="">Avaliação</label>
          <div class="star-rating">
            <i class="far fa-star" id="star${numero}1" onclick="add(this,${numero},1)"></i>
            <i class="far fa-star" id="star${numero}2" onclick="add(this,${numero},2)"></i>
            <i class="far fa-star" id="star${numero}3" onclick="add(this,${numero},3)"></i>
            <i class="far fa-star" id="star${numero}4" onclick="add(this,${numero},4)"></i>
            <i class="far fa-star" id="star${numero}5" onclick="add(this,${numero},5)"></i>
          </div>
        </div>`
        } else{
          data1 += `<h3 class="text-success">Avaliado</h3>`
          data1+=`<div class="form-group">
          <label for="">Avaliação</label>
          <div class="star-rating">`
          let contador = 1;
          while(contador <= element.rating){
            data1 += `<i class="fas fa-star" id="star${numero}${contador}" onclick="add(this,${numero},${contador})"></i>`
            contador++
          }
          while(contador <= 5){
            data1 += `<i class="far fa-star" id="star${numero}${contador}" onclick="add(this,${numero},${contador})"></i>`
            contador++
          }
          data1+= ` </div>
        </div>`
        }
        data1 += `<button class="btn btn-outline-success"  onClick="updateRating(${numero}, ${element.produto})">Avaliar Produto</button>`
        data1+= `</div>`
        data1+= "</div>"
        data1+= "</div>"
        data1+= "</div>"
        numero++;
      });
    }
    document.getElementById("products").innerHTML = data1;
  }

  async function updateRating(num, prodid){
    console.log(num);
    console.log(prodid);
    let rating = 0;
    for(i = 1; i < 6; i++){
      if($("#star"+num+i).hasClass("fas")){
        rating++;
      }

    }
    var url = "http://localhost:6789/rating/update/"+prodid+"?rating="+rating
    console.log(url);
    await fetch(url, {
      headers: headerslist,
      //metodo definido no spark
      method: "PUT",
      //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
      mode: "cors"
    }).then(response=>{
      if(response.ok){
        alert("Rating atualizado com sucesso!");
        window.location.href="./avaliacaoProd.html"
      }
    })

  }

  async function carregaRatings(){
    await fetch("http://localhost:6789/rating/", {
      headers: headerslist,
      //metodo definido no spark
      method: "GET",
      //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
      mode: "cors"
    }).then(response=>{
      return response.json();
    }).then(data=>{
      mostraRatings(data);
    })
  }


  //ativa sempre que a pagina terminar de carregar
  $(document).ready(() => {
    carregaProd();
    Logador();
    carregaRatings();
   // display();
  })


  
  
  
  function ativaFoto(){
    document.getElementById("indexpop").style.display="flex";
  }
  
  function hidepopup(){
    document.getElementById("indexpop").style.display="none";
  
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





 