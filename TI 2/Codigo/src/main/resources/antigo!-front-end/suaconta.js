function deluserlogado(){
    localStorage.removeItem("userLogado")
    location.reload()
  }

$(document).ready(()=>{
    var logado = JSON.parse(window.localStorage.getItem('userLogado'))
    if( logado == null){
        window.location.href = "index.html"
    } else{
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
    Jardim()
})

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

function Jardim(){
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
  var jardim = '  <div class="card-header text-center">'+
  '<button class="btn btn-outline-success">Adicionar novas plantas</button>'+
'</div>';
  jardim += '<ul class="list-group list-group-flush"> ' + data1 + '</ul>';
  document.getElementById("cardconta").innerHTML=jardim;
}
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
function acesso(){
  var acesso = `<ul class="list-group list-group-flush">
  <li class="list-group-item">
  <label>Alterar usuario</label>
  <input type="text" class="form-control"/>
  </li>
  <li class="list-group-item">
  <label>Alterar email</label>
  <input type="email" class="form-control"/>
  </li>
  <li class="list-group-item">
  <label>Alterar senha</label>
  <input type="password" class="form-control"/>
  <label>Confirmar nova senha</label>
  <input type="password" class="form-control"/>
  </li>
  <li class="list-group-item">
  <label>Digite sua senha atual para salvar</label>
  <input type="password" class="form-control"/>
  </li>
  <li class="list-group-item text-end">
  <button class="mt-2 btn btn-outline-primary"> Salvar </button>
  </li>
  </ul>`;
  document.getElementById("cardconta").innerHTML=acesso;
}