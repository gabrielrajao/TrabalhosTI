//Header Authorizantion com o token do usuario (Forma recomendada de enviar tokens de autenticacao)
const headerslist = {
    "Authorization" : 'Bearer ' + sessionStorage.getItem("UserLogado")
  }
  

function exibirDadosNaTela(data) {
  const produtoInfoDiv = document.getElementById("produtoInfo");
  if (produtoInfoDiv && data.data && data.data.length > 0) {
    produtoInfoDiv.innerHTML = ''; // Limpa o conteúdo existente

    data.data.forEach(produto => {
      const produtoDiv = document.createElement('div');
      produtoDiv.classList.add('card'); // Adiciona a classe "card" ao div
      produtoDiv.classList.add('cardprod');
      produtoDiv.classList.add('col-3');
      produtoDiv.innerHTML = `
        <a class="link-titulo"><h2 class="titulo">Nome: ${produto.nome}</h2></a>
        <p class="preco">Preço: R$ ${produto.preco.toFixed(2)}</p>
        <p class="tipo">Tipo: ${produto.tipo}</p>
        <button class="botao" onclick="deleteProduto(${produto.prodid})">Excluir</button>
      `;

      produtoInfoDiv.appendChild(produtoDiv);
    });
  } else{
    produtoInfoDiv.innerHTML = '';
  }

}


  

  //Funcao testa validade do token pelo backend
  function produtosEmpresa() {
    fetch("http://localhost:6789/produto/get", {
      //header com o token
      headers: headerslist,
      //metodo do fetch
      method: "GET"
    }).then(response =>{
        return response.json();

    }).then(data => {
      console.log("Dados recebidos:", data);
      // Aqui, você pode usar os dados para atualizar sua página
      exibirDadosNaTela(data);
    })
    .catch(error => {
      console.error("Erro na requisição:", error);
    });
}


// Função para deletar os produtos
function deleteProduto(prodid) {
  console.log(prodid);
  if(confirm("Tem certeza que deseja deletar")){
    fetch(`http://localhost:6789/produto/delete/${prodid}`, {
    headers: headerslist,
    method: "DELETE",
    mode: "cors"
  })
    .then(response => {
      if (response.ok) {
        // Produto excluído com sucesso, você pode lidar com a resposta aqui
        produtosEmpresa(); // Se desejar, redirecione ou atualize os produtos após a exclusão
      } else if (response.status == 409) {
        return response.text();
      } else if (response.status == 500) {
        alert("ERRO no servidor");
      }
    })
    .catch(error => {
      console.error("Erro na requisição:", error);
    });
  }
}


//funcao para o post propriamente dito
function getProdutoEspecifico(prodid) {
//checa se são validos (nao é o ideal)
  fetch(`http://localhost:6789/produto/${prodid}`, {
    headers: headerslist,
    //metodo definido no spark
    method: "GET",
    //enviando por CORS (regula as permissões de entrada, saida, etc do backend)
    mode: "cors"
  })
    //promise
    .then(response => {
      //response.ok pega a range de codigos de status http que sinalizam sucesso (entre 200 e 299) 
      if (response.ok) {
        //envia o empresa para a pagina de login para que ele possa logar (o melhor era fazer o login automatico, mas preguica)
        window.location.href = "./exibeProdEspecifico.html"
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
} 
  

document.onload = produtosEmpresa();

