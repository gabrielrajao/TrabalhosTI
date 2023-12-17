// Recupera os dados salvos no Local Storage
//var plantas = localStorage.getItem("objDados");



function display() {

  const dados = {
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
  };

  var data1 = "";
  var numero = 1;
  if (dados == null) {
    data1 = '<div class="col-12 "> Sentimos muito, nenhuma planta foi encontrada </div>'
  } else {
    let dataDisplay = dados.plantas.map((object) => {

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
    });
  }

  document.getElementById("cards").innerHTML = data1;

}

display();


/* PESQUISAR */
const search = () => {
  var contagem = 0;
  /* BUSCAR TEXTO INSERIDO E ATRIBUTOS CARDS */
  //document.getElementById("texto-inicial").style.display = "none";
  const searchbox = document.getElementById("search-item").value.toUpperCase();
  const storeitems = document.getElementById("cards")
  const product = document.querySelectorAll(".product")
  const pname = document.getElementsByTagName("h1")


  for (var i = 0; i < pname.length; i++) {
    let match = product[i].getElementsByTagName('h1')[0];

    /* VER SE O VALOR PROCURADO É IGUAL AO "BANCO DE DADOS" */
    if (match) {
      let textvalue = match.textContent || match.innerHTML

      /* COLOCAR ELEMENTOS CORRETOS VISÍVEIS */
      if (textvalue.toUpperCase().indexOf(searchbox) > -1) {
        product[i].style.display = "block";
        contagem++;
        //document.getElementById("resultadoNegativo").style.display = "";
        //document.getElementById("texto-resultado").style.display = "block";
      }
      else {
        product[i].style.display = "none";
      }

      if (contagem == 0) {
        //document.getElementById("resultadoNegativo").style.display = "block";
        //document.getElementById("texto-resultado").style.display = "none";
      }
    }
  }

}

function deluserlogado() {
  localStorage.removeItem("userLogado")
  location.reload()
}





function isUserLogado(Userlogado) {
  var Userlogado = JSON.parse(window.localStorage.getItem('userLogado'));
  if (Userlogado == null) {
    return false;
  } else {
    return true;
  }
}

function AdicionarAoJardim(numero) {
  var result = '' ;
  var logado = isUserLogado();
  var paginaDeDestino = 'login.html';
  if(logado == false){
    window.location.href = paginaDeDestino;
  } else {
    result = '<a href="calendario.html" class=" m-2 btn btn-outline-success" ">Calendário recomendado</a><a href="cadastrocalendario.html" class=" m-2 btn btn-outline-success">Formulário calendario</a>';
    document.getElementById(`AdicionaJardim${numero}`).innerHTML = result;
  }
}

$(document).ready(() => {
  logado = isUserLogado();
  var result = ""
  if (logado == false) {
    result = '<a href="Login.html"  ><button class="btn btn-light " >Entrar</button></a><br>' +
      '<p id="registro" style="font-size: 10pt">Não possui uma conta? <a href="cadastro.html">Clique Aqui</a> </p>'
  } else {
    result = '<div class="dropdown">' +
      '<a class="btn dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">' +
      '<i class="fa-regular fa-user"></i>' +
      '</a>' +
      '<ul class="dropdown-menu">' +
      '<li><a class="dropdown-item" href="calendario.html">Seu Calendario</a></li>' +
      '<li><a class="dropdown-item" href="SuaConta.html">Sua Conta</a></li>' +
      '<li><button class="dropdown-item" onclick="deluserlogado()">Sair</button></li>' +
      '</ul>' +
      '</div>'
  }
  document.getElementById("barralogin").innerHTML = result;
  display();
})




