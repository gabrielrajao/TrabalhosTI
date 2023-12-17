//Header Authorizantion com o token do usuario (Forma recomendada de enviar tokens de autenticacao)
const headerslist = {
    "Authorization" : 'Bearer ' + sessionStorage.getItem("UserLogado")
  }

  //Remove UserLogado do SessionStorage e atualiza a pagina
function deluserlogado() {
    sessionStorage.removeItem("UserLogado")
    location.reload()
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

async function InsercaoPropriamenteDita(){
    const searchParams = new URLSearchParams(window.location.search);
    var validation = await isUserLogado();
    if(validation == false){
        alert("Não foi possivel validar o usuário, favor logar novamente");
        window.location.href="./Login.html"
    }
    if(searchParams.has("planta")){
        let slug = parseInt(searchParams.get("planta"));
        let nome = document.getElementById("nomeag").value;
        let descricao = document.getElementById("descag").value;
        let luzhi = document.getElementById("luz_hi").value;
        let luzhf = document.getElementById("luz_hf").value;
        let regah = document.getElementById("rega_h").value;
        let regafrq = document.getElementById("rega_frq").value;
        let podah = document.getElementById("poda_h").value;
        let podafrq = document.getElementById("poda_frq").value;
        fetch("http://localhost:6789/agenda/insert/"+slug+"?nome="+nome+"&descricao="+descricao+"&lsi="+luzhi+
        "&lsfim="+luzhf+"&rh="+regah+"&rfrq="+regafrq+"&ph="+podah+"&pfrq="+podafrq,{
            method: "POST",
            mode: "cors",
            headers: headerslist
        }).then(response =>{
            if(response.ok){
                alert("Agenda criada com sucesso!");
                window.location.href="./index.html";

            }  else{
                return response.text();
            }

        }).then(data =>{
            alert(data);
        })
    } else{
        alert("ERRO: Planta nao encontrada");
        window.location.href="./index.html";
    }
}