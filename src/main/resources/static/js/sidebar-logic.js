document.addEventListener("DOMContentLoaded", function () {
  const sidebar = document.getElementById("sidebar");
  const toggleBtn = document.getElementById("sidebarToggle");
  let tooltipInstances = [];

  function clearTooltips() {
    tooltipInstances.forEach((t) => t.dispose());
    tooltipInstances = [];
  }

  function initTooltipsIfCollapsed() {
    clearTooltips();
    if (sidebar.classList.contains("collapsed")) {
      const targets = document.querySelectorAll('[data-bs-toggle="tooltip"]');
      targets.forEach((el) => tooltipInstances.push(new bootstrap.Tooltip(el)));
    }
  }

  // Toggle Sidebar e LocalStorage
  toggleBtn.addEventListener("click", function () {
    sidebar.classList.toggle("collapsed");
    localStorage.setItem(
      "sidebarState",
      sidebar.classList.contains("collapsed") ? "collapsed" : "expanded",
    );
    setTimeout(initTooltipsIfCollapsed, 250); // Delay para acompanhar a transição CSS
  });

  // Estado inicial
  if (localStorage.getItem("sidebarState") === "collapsed") {
    sidebar.classList.add("collapsed");
  }
  initTooltipsIfCollapsed();
});
