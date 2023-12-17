function leDados() {
    let strDados = localStorage.getItem('db');
    let objDados = {};

    if (strDados) {
        objDados = JSON.parse(strDados);
    }
    else {
        objDados = {
            plantas: [
                {   nome: "Orquidea", 
                    nomecien: "Orchidaceae", 
                    luz: "3h", 
                    rega: "12h", 
                    solo: "Poroso", 
                    descricao: "Orquídeas são todas as plantas que compõem a família Orchidaceae, pertencente à ordem Asparagales, uma das maiores famílias de plantas existentes. " },

                {   nome: "Tulipa", 
                    nomecien: "Tulipa", 
                    luz: "5h", 
                    rega: "8h", 
                    solo: "Vermiculita e Perlita", 
                    descricao: "Tulipa L. é um género de plantas angiospermas (plantas com flores) da família das liláceas." },

                {   nome: "Rosa", 
                    nomecien: "Rosa", 
                    luz: "12h", 
                    rega: "4h", 
                    solo: "Argiloso", 
                    descricao: "A rosa (do latim rosa) é uma das flores mais populares no mundo. Vem sendo cultivada pelo homem desde a Antiguidade." }
            ]
        }
    }

    return objDados;
}

function salvaDados(dados) {
    localStorage.setItem('db', JSON.stringify(dados));
}

function incluirContato() {
    //ler os dados do localStorage
    let objDados = leDados();

    //incluir um novo contato
    let strNome = document.getElementById('campoNome').value;
    let strNomecien = document.getElementById('campoNomecien').value;
    let strLuz = document.getElementById('campoLuz').value;
    let strRega = document.getElementById('campoRega').value;
    let strSolo = document.getElementById('campoSolo').value;
    let strDescri = document.getElementById('campoDescri').value;
    let novaPlanta = {
        nome: strNome,
        nomecien: strNomecien,
        luz: strLuz,
        rega: strRega,
        solo: strSolo,
        descricao: strDescri
    };
    objDados.plantas.push(novaPlanta);

    //salvar os dados no localStorage novamente
    salvaDados(objDados);

    //imprimir novos dados
    imprimeDados();

    //alerta
    alert('Planta adicionada com sucesso');

}

function imprimeDados() {
    let tela = document.getElementById('tela');
    let baseTabela = '';
    let strHtml = '';
    let objDados = leDados();

    for (i = 0; i < objDados.plantas.length; i++) {
        strHtml +=
            `
        Nome: ${objDados.plantas[i].nome} <br> 
        Nome CIentífico: ${objDados.plantas[i].nomecien}  <br>
        Tempo de Exposição à Luz: ${objDados.plantas[i].luz} <br>
        Intervalo de Rega: ${objDados.plantas[i].rega} <br>
        Tipo de Solo: ${objDados.plantas[i].solo} <br>
        Descrição: ${objDados.plantas[i].descricao} <br>
        <br>
        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        <br>
        <br>
 
        `
    }

    tela.innerHTML = strHtml;
}

// Configura os botões
document.getElementById('btnCarregaDados').addEventListener('click', imprimeDados);
document.getElementById('btnIncluirContato').addEventListener('click', incluirContato);

