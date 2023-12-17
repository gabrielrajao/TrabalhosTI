
//Header Authorizantion com o token do usuario (Forma recomendada de enviar tokens de autenticacao)
const headerslist = {
  "Authorization" : 'Bearer ' + sessionStorage.getItem("UserLogado")
}

//funcao para o post propriamente dito
function enviaProduto() {
  //console.log("alalalalal");
    //pega senha do input
    let funcao = document.getElementById('funcao').value;
    //pega email do input
    let preco = document.getElementById('preco').value;
    
    //pega login do input
    
    let tipo = document.getElementById('tipo').value;
   
    //pega senha do input
    let nome = document.getElementById('nome').value;
    //pega email do input
    let produrl = document.getElementById('produrl').value;
    console.log(produrl);

    if (tipo == "0") {
      alert("Preencha o campo Tipo corretamente");
    } else {
      fetch("http://localhost:6789/produto/insert?funcao=" + funcao + "&preco=" + preco + "&tipo=" + tipo + "&nome=" + nome + "&produrl=" + produrl, {
        headers: headerslist,
        //metodo definido no spark
        method: "POST",
        //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
        mode: "cors"
      }) //promise 
      .then(response => {
        //erro 500 sinaliza um erro interno, nao da parte de empresa
        if (response.status == 500) {
          alert("ERRO no servidor");
        }
      })
    }


}
//document.getElementById('btnProduto').addEventListener('click', enviaProduto);