
/* JSON DEFINIR ATRIBUTOS PLANTAS */
let plantas  = [
    {
        "nome": "Orquídea Rosa",
        "nomeCientifico": "orchidaceae"
    },

    {
        "nome": "Orquídea Branca",
        "nomeCientifico": "orchidaceae"
    },

    {
        "nome": "Tulipa Branca",
        "nomeCientifico": "tulipa"
    },

    {
        "nome": "Tulipa Laranja", 
        "nomeCientifico": "tulipa"
    }
]


/* Colocar Informações nos Cards */
const funcaoInicial = () => {

const product = document.querySelectorAll(".product")

/* NOME */
for (var i = 0; i < product.length; i++) {
    var id = "#tituloCard" + (i+1) + " h5";
    var element = document.querySelector(id).innerText = plantas[i].nome;
}

/* NOME CIENTÍFICO */
for (var i = 0; i < product.length; i++) {
    var id = "#nomeCienCard" + (i+1) + " p";
    var element = document.querySelector(id).innerText = plantas[i].nomeCientifico;
}
}


/* PESQUISAR */
const search = () => {
    var contagem = 0;
    /* BUSCAR TEXTO INSERIDO E ATRIBUTOS CARDS */
    document.getElementById("texto-inicial").style.display = "none";
    const searchbox = document.getElementById("search-item").value.toUpperCase();
    const storeitems = document.getElementById("product-list")
    const product = document.querySelectorAll(".product")
    const pname = document.getElementsByTagName("h5")
    
    
    for (var i = 0; i < pname.length; i++) {
        let match = product[i].getElementsByTagName('h5')[0];
        
        /* VER SE O VALOR PROCURADO É IGUAL AO "BANCO DE DADOS" */
        if (match) {
            let textvalue = match.textContent || match.innerHTML

            /* COLOCAR ELEMENTOS CORRETOS VISÍVEIS */
            if (textvalue.toUpperCase().indexOf(searchbox) > -1) {
                product[i].style.display = "block";
                contagem++;
                document.getElementById("resultadoNegativo").style.display = "";
                document.getElementById("texto-resultado").style.display = "block";
            }
            else {
                product[i].style.display = "";
            }

            if (contagem == 0) {
                document.getElementById("resultadoNegativo").style.display = "block";
                document.getElementById("texto-resultado").style.display = "none";
            }
        }
    }

}


