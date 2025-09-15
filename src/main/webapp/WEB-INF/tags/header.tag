<%@ tag pageEncoding="UTF-8" %>
<style>
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .header h1 {
        margin: 16px 0;
    }

    .nav-links a {
        margin: auto 12px;
        color: #ffffff;
        font-size: 1.6em;
    }

</style>
<header class="panel panel-primary">
    <div class="header panel-heading">
        <h1>Admin</h1>
        <nav class="nav-links">
            <a href="/admin/categories">Categorias</a>
            <a href="/admin/courses">Cursos</a>
            <a href="/">Sair</a>
        </nav>
        <h1>Alura</h1>
    </div>
</header>
