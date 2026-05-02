let stompClient = null;
let destinatarioAtual = null;

function conectar() {
  const socket = new SockJS("/ws-portal"); // Endpoint configurado no WebSocketConfig
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log("Conectado: " + frame);

    // Se inscreve para receber mensagens privadas
    stompClient.subscribe(
      "/user/" + emailLogado + "/queue/messages",
      function (mensagem) {
        exibirMensagem(JSON.parse(mensagem.body));
        atualizarBadgeNotificacao();
      },
    );
  });
}

function enviarMensagem() {
  const texto = document.getElementById("message-input").value;
  if (texto && stompClient) {
    const mensagem = {
      conteudo: texto,
      remetente: { email: emailLogado },
      destinatario: { email: destinatarioAtual }, // VARCHAR conforme regra do projeto
      dataEnvio: new Date(),
    };

    stompClient.send("/app/chat.privado", {}, JSON.stringify(mensagem)); // Rota do Controller
    exibirMensagem(mensagem); // Mostra na própria tela
    document.getElementById("message-input").value = "";
  }
}

function exibirMensagem(msg) {
  const area = document.getElementById("messages-area");
  const msgElement = document.createElement("div");
  msgElement.className =
    msg.remetente.email === emailLogado ? "msg-enviada" : "msg-recebida";
  msgElement.innerText = msg.conteudo;
  area.appendChild(msgElement);
}

// Inicia a conexão ao carregar a página
document.addEventListener("DOMContentLoaded", conectar);
