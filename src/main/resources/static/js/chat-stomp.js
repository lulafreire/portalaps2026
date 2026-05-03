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
  const input = document.getElementById("message-input");
  const texto = input.value;
  const tipo = document.getElementById("tipo-conversa").value; // 'PRIVADO' ou 'GRUPO'

  if (texto && stompClient && stompClient.connected) {
    const mensagem = {
      conteudo: texto,
      remetente: { email: emailLogado },
      dataEnvio: new Date(),
    };

    if (tipo === "PRIVADO") {
      mensagem.destinatario = { email: destinatarioAtual };
      stompClient.send("/app/chat.privado", {}, JSON.stringify(mensagem));
    } else {
      // Para grupos, o ID do grupo vai na URL do @MessageMapping
      stompClient.send(
        "/app/chat.grupo." + grupoAtivoId,
        {},
        JSON.stringify(mensagem),
      );
    }

    exibirMensagem(mensagem);
    input.value = "";
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
