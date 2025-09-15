<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/tld/custom-functions.tld" prefix="util" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <title>Lista de Cursos</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/assets/css/admin/course/list.css">
    </head>
    <body>
        <tag:header />
        <div class="container">
            <div class="panel panel-primary">
                <div class="panel-heading list-heading">
                    <h1>Cursos</h1>
                    <a class="btn btn-success new-button" href="/admin/course/new">Cadastrar novo</a>
                </div>
                <table class="panel-body table table-hover">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Código</th>
                        <th>Instrutor</th>
                        <th>Categoria</th>
                        <th>Descrição</th>
                        <th>Status</th>
                        <th>Data de inativação</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${courses}" var="course">
                        <tr>
                            <td>${course.name()}</td>
                            <td>${course.code()}</td>
                            <td>${course.instructor()}</td>
                            <td>
                                <c:set var="categoryColor" value="${course.category().getColor()}" />
                                <span class="badge" style="background-color: ${categoryColor}; color: ${util:getFontColor(categoryColor)};">
                                        ${course.category().getName()}
                                </span>
                            </td>
                            <td>${course.description()}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${course.status() == 'Ativo'}">
                                        <span class="badge badge-active">${course.status()}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-inactive">${course.status()}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${course.inactivatedAt()}</td>
                            <td><a class="btn btn-primary" href="/admin/course/edit/${course.id()}">Editar</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>