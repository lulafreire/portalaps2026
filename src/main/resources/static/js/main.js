document.addEventListener("DOMContentLoaded", function () {
  const sidebar = document.getElementById("sidebar");
  const toggleBtn = document.getElementById("sidebarToggle");

  toggleBtn.addEventListener("click", function () {
    sidebar.classList.toggle("collapsed");

    // Opcional: Salvar a preferência do usuário no navegador
    const isCollapsed = sidebar.classList.contains("collapsed");
    localStorage.setItem(
      "sidebarState",
      isCollapsed ? "collapsed" : "expanded",
    );
  });

  // Restaurar estado ao carregar a página
  if (localStorage.getItem("sidebarState") === "collapsed") {
    sidebar.classList.add("collapsed");
  }
});
