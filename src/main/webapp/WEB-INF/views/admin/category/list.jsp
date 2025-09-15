<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <title>Lista de Categorias</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/assets/css/admin/category/list.css">
    </head>
    <body>
        <tag:header />
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading list-heading">
                    <h1>Categorias</h1>
                    <a class="btn btn-success new-button" href="/admin/category/new">Cadastrar nova</a>
                </div>
                <table class="panel-body table table-hover">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Código</th>
                        <th>Ordem</th>
                        <th>Cor</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${categories}" var="category">
                        <tr>
                            <td>${category.name()}</td>
                            <td>${category.code()}</td>
                            <td>${category.order()}</td>
                            <td><span class="color-dot" style="background-color: ${category.color()}"></span></td>
                            <td><a class="btn btn-primary" href="/admin/category/edit/${category.id()}">Editar</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>